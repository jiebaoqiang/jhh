package com.jhh.dc.baika.app.web.exception;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.app.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2018/7/25.
 */
@RestController
@Slf4j
public class ExceptionJsonController {

    @ExceptionHandler
    public ResponseDo<?> exception(HttpServletRequest request, Exception ex) {
        if (ex instanceof CommonException) {
            log.info("自定义异常抛出   CommonException=",ex);
            CommonException commonException = (CommonException) ex;
            return new ResponseDo<>(commonException.getResultCode(),commonException.getMessage());
        }
        // 打印
        log.info(ex.getMessage(), ex);
        return ResponseDo.newFailedDo("服务器开小差了，请稍候");
    }

    protected Map<String, String> getResult(List<BindingResult> bindingResults) {
        Map<String, String> map = new HashMap<>();
        for (BindingResult bindingResult : bindingResults) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        return map;
    }
}
