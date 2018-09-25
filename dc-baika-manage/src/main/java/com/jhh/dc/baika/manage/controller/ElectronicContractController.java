package com.jhh.dc.baika.manage.controller;


import com.jhh.dc.baika.api.contract.ElectronicContractService;
import com.jhh.dc.baika.common.util.HttpUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Created by wanzezhong on 2017/11/23.
 */
@Controller
public class ElectronicContractController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(ElectronicContractController.class);

    @Autowired
    ElectronicContractService electronicContractService;

    @ResponseBody
    @RequestMapping(value = "/contract/contract/{borrId}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getContract(@PathVariable Integer borrId){
        return electronicContractService.createElectronicContract(borrId);
    }

    @ResponseBody
    @RequestMapping(value = "/contract/preview/{borrId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getContractPreview(@PathVariable Integer borrId){
        return electronicContractService.queryElectronicContract(borrId);
    }

    @ResponseBody
    @RequestMapping(value = "/contract/down/{borrNum}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String downloadContract(@PathVariable String borrNum){
        return electronicContractService.downElectronicContract(borrNum);
    }

    @ResponseBody
    @RequestMapping(value = "/contract/callBack",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String callBack(HttpServletRequest request,String code,String contractNo,String downloadUrl, String isPing) throws UnsupportedEncodingException {
        //正式数据走业务逻辑
        if(isPing.equals("false")) {
            electronicContractService.callBack(code, downloadUrl, contractNo);
        }
        return request.getParameter("code");
    }

    @ResponseBody
    @RequestMapping(value = "/contract/disposeExceptionContract",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String disposeExceptionContract(HttpServletRequest request) {
        //正式数据走业务逻辑
        electronicContractService.disposeExceptionContract();
        return "sucess";
    }


    public static void main(String[] arge){
        try {
//            String url = "http://localhost:8093/loan-web/contract/contract/6435362.action";
//            String result = HttpUtils.sendPost(url,"");

//            String url = "http://localhost:8093/loan-web/contract/preview/6442362.action";
//            String result = HttpUtils.sendGet(url,"");


            String url = "http://localhost:8093/loan-web/contract/down/14975791155145915.action";
            String result = HttpUtils.sendGet(url,"");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
