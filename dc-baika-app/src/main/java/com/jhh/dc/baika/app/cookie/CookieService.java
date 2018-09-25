package com.jhh.dc.baika.app.cookie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 2018/7/27.
 */
@Component
@Slf4j
public class CookieService {


    @Value("${token.expire.time}")
    private Integer time;

    public String getCookie(HttpServletRequest request, String cookieName) {

        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public void writeCookie(HttpServletResponse response, Map<String,String> map){
        log.info("cookie中存入数据为----------------------------"+map);
        if (map != null && map.size()>0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Cookie cookie = new Cookie(entry.getKey(), entry.getValue());
                //产品类型不需要过期时间
                    cookie.setMaxAge(time * 60);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

        }
    }
}
