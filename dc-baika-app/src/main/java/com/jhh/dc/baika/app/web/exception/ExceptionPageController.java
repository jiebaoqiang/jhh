package com.jhh.dc.baika.app.web.exception;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.jhh.dc.baika.app.common.exception.CommonException;
import com.jhh.dc.baika.app.cookie.CookieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *  异常捕获公用类
 */
@Controller
@Slf4j
public class ExceptionPageController {

    @Autowired
    private CookieService cookieService;

    @ExceptionHandler
    public String exception(HttpServletRequest request, Exception ex,HttpServletResponse response) {
        String prodType = (String) request.getAttribute("prodType");
        if (StringUtils.isEmpty(prodType)){
            prodType = cookieService.getCookie(request,"prodType");
        }
        request.setAttribute("prodType",prodType);
        Map<String,String> map = new HashMap<>();
        map.put("prodType",prodType);
        cookieService.writeCookie(response,map);
        if (ex instanceof CommonException) {
            log.info("自定义异常抛出   CommonException=",ex);
            CommonException commonException = (CommonException) ex;
            request.setAttribute("errorMsg",commonException.getMessage());
            request.setAttribute("errorCode",commonException.getResultCode());
            return "status/error";
        }
        // 打印
        log.info(ex.getMessage(), ex);
        request.setAttribute("errorMsg","服务器开小差了，请稍候");
        return "status/error";
    }


}
