package com.jhh.dc.baika.service.contract;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.common.util.*;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.dc.baika.mapper.contract.ContractMapper;
import com.jhh.dc.baika.mapper.manager.RepaymentPlanMapper;
import com.jhh.dc.baika.mapper.product.ProductMapper;
import com.jhh.dc.baika.api.contract.ElectronicContractService;
import com.jhh.dc.baika.api.loan.CompanyAService;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.entity.app.Product;
import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.entity.contract.Contract;
import com.jhh.dc.baika.entity.contract.IdEntity;
import com.jhh.dc.baika.entity.manager_vo.PrivateVo;
import com.jhh.dc.baika.common.enums.ContractDataCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import tk.mybatis.mapper.entity.Example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wanzezhong on 2017/11/23.
 *
 * @author carl.wan
 */
@Slf4j
@Service
public class ElectronicContractServiceImpl implements ElectronicContractService {

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private ProductMapper productMapper;

    @Value("${contract.url}")
    private String contractUrl;

    @Value("${contract.url.generate}")
    private String generateUrl;

    @Value("${contract.url.preview}")
    private String previewUrl;

    @Value("${contract.url.download}")
    private String downloadUrl;

    @Value("${contract.token}")
    private String token;

    @Value("${dfsUrl}")
    private String dfsUrl;

    @Value("${baseUrl}")
    private String baseUrl;

    @Autowired
    CompanyAService companyAService;


    @Override
    public String createElectronicContract(Integer borrId) {
        Integer contractStatus = Constants.ContractStatus.EXCEPTION;
        String result = "";
        try {
            //1..获取产品相关信息product
            Product product = queryProductByBorrId(borrId);
            Assertion.notNull(product,"产品不存在");
            //2.获取应执行的封装数据data
            Map data = queryContractDataByProdId(product,borrId);
            Assertion.notNull(data,"合同异常");

            Map param = new HashMap();
            param.put("token", token);
            param.put("productId", product.getContractPrdouctId());
            param.put("contractNo", data.get("#borrNum#"));
            param.put("cardNo", data.get("#cardId#"));
            param.put("name", data.get("#name#"));
            param.put("mobile", data.get("#phone#"));
            param.put("data", JSONObject.toJSONString(data));
            result = HttpUtils.sendPost(contractUrl + generateUrl, HttpUtils.toParam(param));
            if (Detect.notEmpty(result)) {
                JSONObject jsonResult = JSONObject.parseObject(result);
                JSONObject jsonData = jsonResult.getJSONObject("data");
                if (jsonData.getString("code").equals("10000")) {
                    contractStatus = Constants.ContractStatus.CREATEING;
                }
            }
            //创建合同
            creatContract(borrId, contractStatus, result);
        } catch (Exception e) {
            creatContract(borrId, contractStatus, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public String queryElectronicContract(Integer borrId) {
        String result = "";
        //1..获取产品相关信息product
        Product product=queryProductByBorrId(borrId);
        if(null==product){
            return result;
        }
        //2.获取应执行的封装数据data
        Map data=queryContractDataByProdId(product,borrId);
        if(null==data){
            return result;
        }
        Map param = new HashMap();
        param.put("token", token);
        param.put("productId", product.getContractPrdouctId());
        param.put("serialNo", borrId);
        param.put("data", JSONObject.toJSONString(data));

        try {
            result = HttpUtils.sendPost(contractUrl + previewUrl, HttpUtils.toParam(param));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String downElectronicContract(String borrNum) {
        Assertion.notEmpty(borrNum, "合同号不能为空");
        NoteResult noteResult = new NoteResult();
        noteResult.setCode(CodeReturn.FAIL_CODE);
        noteResult.setInfo("合同下载文件正在生成中，请两分钟后再试...");
        //查找本地合同是否存在
        Contract contract = new Contract();
        contract.setBorrNum(borrNum);
        contract = contractMapper.selectOne(contract);
        if (contract != null) {
            Product product=queryProductByBorrId(contract.getBorrId());
            if(null==product){
                noteResult.setInfo("合同下载文件失败，查询产品信息");
                return  JSONObject.toJSONString(noteResult);
            }
            if (contract.getStatus().equals(Constants.ContractStatus.SUCESS)) {
                noteResult.setCode(CodeReturn.SUCCESS_CODE);
                noteResult.setData(contract.getContractUrl());
                noteResult.setInfo("");
                return JSONObject.toJSONString(noteResult);
            } else {
                Map param = new HashMap();
                param.put("productId", product.getContractPrdouctId());
                param.put("contractNo", borrNum);
                try {
                    String result = HttpUtils.sendPost(contractUrl + downloadUrl, HttpUtils.toParam(param));
                    //正常生成合同查看
                    if (Detect.notEmpty(result)) {
                        JSONObject jsonResult = JSONObject.parseObject(result);
                        JSONObject jsonData = jsonResult.getJSONObject("data");
                        if (jsonData.getString("code").equals("10000")) {
                            noteResult.setCode(CodeReturn.SUCCESS_CODE);
                            noteResult.setData(jsonData.getString("data"));
                            noteResult.setInfo("成功");
                            String imageUrl = null;
                            InputStream inputStream = null;
                            try {
                                inputStream = PdfToImage.pdfToImage(jsonData.getString("data"), "png", null, true);
                                imageUrl = DFSUtil.uploadContent(dfsUrl, inputStream, "png");
                            } catch (IOException e) {
                                log.info("合同PDF转为图片失败:{}", e.getMessage());
                                e.printStackTrace();
                            } catch (Exception e) {
                                log.info("合同PDF转为图片失败:{}", e.getMessage());
                                e.printStackTrace();
                            } finally {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                            }
                            saveContract(contract.getBorrId(), Constants.ContractStatus.SUCESS, "", jsonData.getString("data"), imageUrl);
                            return JSONObject.toJSONString(noteResult);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //未生成正常合同
        BorrowList bl = borrowListMapper.selectByBorrNum(borrNum);
        if (bl != null) {
            createElectronicContract(bl.getId());
        }

        return JSONObject.toJSONString(noteResult);
    }

    @Override
    public String callBack(String code, String url, String borrNum) throws UnsupportedEncodingException {
        //查找本地合同是否存在
        Example example = new Example(Contract.class);
        example.createCriteria().andEqualTo("borrNum", borrNum);
        List<Contract> contract = contractMapper.selectByExample(example);
        String imageUrl = null;
        if (contract != null && contract.size() > 0 && Detect.isPositive(contract.get(0).getBorrId())) {
            int status = Constants.ContractStatus.FAIL;
            if (code.equals("10000")) {
                status = Constants.ContractStatus.SUCESS;
                InputStream inputStream = null;
                try {
                    inputStream = PdfToImage.pdfToImage(url, "png", null, true);
                    imageUrl = DFSUtil.uploadContent(dfsUrl, inputStream, "png");
                } catch (IOException e) {
                    log.info("合同PDF转为图片失败:{}", e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    log.info("合同PDF转为图片失败:{}", e.getMessage());
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            log.info("关闭流失败{}", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
            saveContract(contract.get(0).getBorrId(), status, "", url, imageUrl);
        } else {
            log.error("电子合同未找到对应订单号" + borrNum);
        }
        return null;
    }

    @Override
    public void disposeExceptionContract() {
        Example example = new Example(Contract.class);
        Set<Integer> inSet = new HashSet();
        inSet.add(Constants.ContractStatus.SUCESS);
        example.createCriteria().andNotIn("status", inSet);
        //查询失败异常合同
        List<Contract> contracts = contractMapper.selectByExample(example);
        if (Detect.notEmpty(contracts)) {
            //调用下载接口
            for (Contract constant : contracts) {
                downElectronicContract(constant.getBorrNum());
            }
        }
    }

    private Map getContractDate(Integer borrId) {
        Assertion.isPositive(borrId, "合同Id不能为空");
        //查询合同信息
        IdEntity idEntity = borrowListMapper.queryIdentityById(borrId);
        if(null != idEntity){
            //查询用户详细信息
            PrivateVo privateVo = companyAService.queryUserPrivateByPhone(idEntity.getPhone());
            if(null != privateVo){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                long now = DateUtil.dateToStamp(idEntity.getPayDate());
                String borrowDate = sdf.format(new Date(now));
                //16.签订年
                String signYear = borrowDate.substring(0, 4);
                //17.签订月
                String signMonth = borrowDate.substring(5, 7);
                //18.签订日
                String signDay = borrowDate.substring(8, borrowDate.length() - 1);
                Map<String, String> map = new HashMap();

                map.put("#cardId#", idEntity.getCardNum());
                //客户名
                map.put("#name#", idEntity.getName());
                map.put("#borrNum#", idEntity.getBorrNum());
                map.put("#phone#", idEntity.getPhone());
                map.put("#mail#", privateVo.getEmail());
                map.put("#address#", privateVo.getBusiAddress());

                map.put("#day#", signDay);
                map.put("#month#", signMonth);
                map.put("#year#", signYear);
                return map;
            }
        }
        return null;
    }

    private Map getContractDateByLoan(Integer borrId) {
        Assertion.isPositive(borrId, "合同Id不能为空");
        //查询合同信息
        IdEntity idEntity = borrowListMapper.queryIdentityById(borrId);
        if(null != idEntity){
            PrivateVo privateVo = companyAService.queryUserPrivateByPhone(idEntity.getPhone());
            Map<String, String> map = new HashMap();
            if(null != privateVo){
                map.put("#address#", privateVo.getBusiAddress());
                map.put("#email#", privateVo.getEmail());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            long now = DateUtil.dateToStamp(idEntity.getPayDate());
            String borrowDate = sdf.format(new Date(now));
            //.签订年
            String signYear = borrowDate.substring(0, 4);
            //.签订月
            String signMonth = borrowDate.substring(5, 7);
            //.签订日
            String signDay = borrowDate.substring(8, borrowDate.length() - 1);



            //客户名信息
            map.put("#cardId#", idEntity.getCardNum());
            map.put("#name#", idEntity.getName());
            map.put("#borrNum#", idEntity.getBorrNum());
            map.put("#phone#", idEntity.getPhone());
            map.put("#bankNum#", idEntity.getBankNum());

            //借款信息.
            map.put("#principal#", idEntity.getBorrAmount());
            map.put("#term#", idEntity.getTermDay() + "");
            map.put("#interest#", idEntity.getInterestSum());
            map.put("#repayDate#", DateUtil.getDateString(idEntity.getPlanrepayDate()));
            map.put("#repayAmount#", idEntity.getPlanRepay());

            //日期信息
            map.put("#day#", signDay);
            map.put("#month#", signMonth);
            map.put("#year#", signYear);
            map.put("#borrDate#", borrowDate);

            //甲方
            map.put("#loanName#","方伟");
            map.put("#loanCardId#","321322198706184814");
            return map;
        }
        return null;
    }

    /**
     * 创建电子合同
     *
     * @param borrId
     * @param status
     */
    private void creatContract(Integer borrId, Integer status, String msg) {
        if (Detect.isPositive(borrId)) {
            BorrowList bl = borrowListMapper.selectByPrimaryKey(borrId);
            if (bl != null) {
                Contract contract = new Contract();
                contract.setBorrNum(bl.getBorrNum());
                contract.setBorrId(borrId);
                contract.setCreateDate(Calendar.getInstance().getTime());
                contract.setStatus(status);
                contract.setResultJson(msg);
                contractMapper.insertContract(contract);
            }
        }
    }

    /**
     * 保存电子合同
     *
     * @param borrId
     * @param status
     */
    private void saveContract(Integer borrId, Integer status, String msg, String url, String imageUrl) throws UnsupportedEncodingException {
        if (Detect.isPositive(borrId) && Detect.isPositive(status)) {
            Contract contract = new Contract();
            contract.setBorrId(borrId);
            contract = contractMapper.selectOne(contract);

            if (contract != null) {
                contract.setStatus(status);
                if (Detect.notEmpty(msg)) {
                    contract.setResultJson(msg);
                }
                contract.setContractUrl(url);
                if (Detect.notEmpty(imageUrl)) {
                    contract.setImageUrl(baseUrl + URLEncoder.encode(imageUrl, "UTF-8"));
                }
                contractMapper.updateByPrimaryKeySelective(contract);
            }
        }
    }

    /**
     * 获取封装的数据
     * @param product
     * @return
     */
    private Map queryContractDataByProdId(Product product,Integer borrId){
         Map map=null;
        if(ContractDataCodeEnum.GO_TO_BUY.getCode().equals(product.getId().toString())){
            //悠兔白卡
            map = getContractDate(borrId);
        }
        else if(ContractDataCodeEnum.AS_CREDIT.getCode().equals(product.getId().toString())){
            //悠兔白卡
            map = getContractDateByLoan(borrId);
        }
        return map;
    }

    /**
     * 获取产品id
     */
    private Product queryProductByBorrId(Integer borrId){
          BorrowList borrowList = borrowListMapper.selectByPrimaryKey(borrId);
          if(null == borrowList || !Detect.isPositive(borrowList.getProdId())){
              return null;
          }
          Product product = productMapper.selectByPrimaryKey(borrowList.getProdId());
          return product;
    }
    public static void main(String[] arge) throws IOException {
        String imageUrl = null;
        InputStream inputStream = null;
        try {
            inputStream = PdfToImage.pdfToImage("http://10.0.2.164/group1/M01/0C/EE/CgACplqg592AYSTpAArBCsAzHq8240.pdf", "png", null, true);
            imageUrl = DFSUtil.uploadContent("http://192.168.1.87:12015/loan-dfs/fdfs", inputStream, "png");
            System.out.println(imageUrl);
            int index;
            byte[] bytes = new byte[1024];
            FileOutputStream downloadFile = new FileOutputStream("C:\\Users\\wanzezhong\\Desktop\\aa.png");
            while ((index = inputStream.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            downloadFile.close();
        } catch (IOException e) {
            log.info("合同PDF转为图片失败:{}", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.info("合同PDF转为图片失败:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
//        System.out.println("http://cas.ronghezulin.com/loan-manage/proxy/image.action?path=" + URLEncoder.encode("http://10.0.2.166/group1/M01/0C/EE/CgACplqftzSAGSQxAAo2jR4QGPY707.png", "UTF-8"));
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public String getGenerateUrl() {
        return generateUrl;
    }

    public void setGenerateUrl(String generateUrl) {
        this.generateUrl = generateUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
