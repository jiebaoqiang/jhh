package com.jhh.dc.baika.manage.controller;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by chenchao on 2018/3/7.
 */
@Controller
@RequestMapping("/proxy")
@Log4j
public class ProxyController {

    @ResponseBody
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public String getImage(HttpServletResponse response, String path) {
        log.info(String.format("---->代理数据地址【%s】", path));
        response.setContentType("image/png");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (path != null) {
                String imageRealPath = URLDecoder.decode(path, "UTF-8");
                log.info(String.format("---->真实数据地址【%s】", path));
                outputStream = response.getOutputStream();
                URL url = new URL(imageRealPath);
                inputStream = url.openStream();
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();
                return "success";
            }
        } catch (IOException e) {
            log.error("无法获取代理数据, path=" + path, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }
        return "fail";
    }
}
