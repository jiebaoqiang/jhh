package com.jhh.dc.baika.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.service.report.ReportService;
import com.jhh.dc.baika.manage.utils.Assertion;
import com.jhh.dc.baika.manage.utils.Detect;
import com.jhh.dc.baika.api.loan.CompanyAService;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.common.ResponseCode;
import com.jhh.dc.baika.entity.manager_vo.PhoneBookVo;
import com.jhh.dc.baika.entity.manager_vo.PrivateVo;
import com.jhh.dc.baika.manage.mapper.PersonMapper;
import com.jhh.dc.baika.common.util.HttpUtils;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Setter
@Getter
public class ReportServiceImpl implements ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Value("${riskReportApiUrl}")
    private String riskReportApiUrl;
    @Value("${polyXinliUrl}")
    private String polyXinliUrl;

    @Autowired
    PersonMapper personMapper;

    @Autowired
    CompanyAService companyAService;

    @Override
    public Response getPolyXinliReport() {
        Assertion.notEmpty(riskReportApiUrl + polyXinliUrl, "聚信立报告地址错误");
        return new Response().code(ResponseCode.SUCCESS).data(riskReportApiUrl + polyXinliUrl);
    }

    @Override
    public Response getContact(Integer perId, String phones, Integer offset, Integer size) throws IOException {
        logger.info("getContact:-----------phones:" + phones);
        Response response = new Response().code(ResponseCode.SUCCESS);
        if (Detect.isPositive(perId)) {

            JSONArray resultArray = getContact(perId);
            if (resultArray != null) {
                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < size; i++) {
                    if (i + offset < resultArray.size()) {
                        jsonArray.add(resultArray.get(i + offset));
                    }
                }
                response.data(jsonArray);
            } else {
                response.data(new JSONArray());
            }
        }
        return response;
    }

    public JSONArray getContact(Integer perId) {
        if (Detect.isPositive(perId)) {
            Person person = personMapper.selectByPrimaryKey(perId);
            if (person != null) {
                PrivateVo privateVo=companyAService.queryUserPrivateByPhone(person.getPhone());
                 if(privateVo!=null&& Detect.notEmpty(privateVo.getContactUrl())){
                     String bf = HttpUtils.sendGet(privateVo.getContactUrl(), "");
                     return JSONObject.parseArray(bf);
                }

            }
        }
        return null;
    }

//	public Page getPageObject(List list, int offset, int size, int totalSize){
//		Page page = new Page();
//		page.add(list);
//		page.setCount(true);
//		page.setPageNum(offset/size + 1);
//		page.setPageSize(size);
//		page.setStartRow(offset);
//		page.setEndRow(offset + size);
//		page.setTotal(totalSize);
//		page.setPages(totalSize/size);
//		page.setReasonable(false);
//		page.setPageSizeZero(false);
//		return page;
//	}

    @Override
    public Response getContactForExport(Integer perId) {
        Response response = new Response().code(ResponseCode.SUCCESS);
        if (Detect.isPositive(perId)) {
            JSONArray array = getContact(perId);
            if (array != null) {
                String result = array.toJSONString();
                if (StringUtils.isNotBlank(result)) {
                    response.data(JSONObject.parseArray(result, PhoneBookVo.class));
                }
            }
        }
        return response;
    }
}
