package com.jhh.dc.baika.app.interceptor;

import com.jhh.dc.baika.app.common.exception.CommonException;
import com.jhh.dc.baika.app.cookie.CookieService;
import com.jhh.dc.baika.app.web.exception.ExceptionPageController;
import com.jhh.dc.baika.common.util.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 2018/7/27.
 */
@Component
public class FormInterceptor extends ExceptionPageController implements HandlerInterceptor {

    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private CookieService cookieService;

    @Value("${token.expire.time}")
    private Integer time;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
        String phone = (String) httpServletRequest.getAttribute("phone");
        if (StringUtils.isEmpty(phone)) {
            phone = cookieService.getCookie(httpServletRequest, "phone");
        }
        String token = jedisCluster.get(RedisConst.APP_USER_TOKEN + phone);
        if (httpServletRequest.getRequestURI().contains("/form/applyBorrow")
                || httpServletRequest.getRequestURI().contains("/form/getAccountCenter")
                || httpServletRequest.getRequestURI().contains("/form/jumpDetails")) {
            if (StringUtils.isEmpty(token)) {
                token = UUID.randomUUID().toString();
            }
        }
        if (StringUtils.isEmpty(token)) {
            throw new CommonException(300, "当前会话已失效,请重新登陆");
        }
        if (!"OK".equals(jedisCluster.set(RedisConst.APP_USER_TOKEN + phone, token, "NX", "EX", time * 60))) {
            jedisCluster.expire(RedisConst.APP_USER_TOKEN + phone, time * 60);
        }
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("token", token);
        cookieService.writeCookie(httpServletResponse, map);

    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }


}
