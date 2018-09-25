package com.jhh.dc.baika.app.interceptor;

import com.jhh.dc.baika.app.common.exception.CommonException;
import com.jhh.dc.baika.app.cookie.CookieService;
import com.jhh.dc.baika.app.web.exception.ExceptionJsonController;
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

/**
 * ajax 拦截器 失败返回json
 */
@Component
public class LogicInterceptor extends ExceptionJsonController implements HandlerInterceptor {

    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private CookieService cookieService;

    @Value("${token.expire.time}")
    private Integer time;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //获取cookie中用户手机号
        String phone = cookieService.getCookie(httpServletRequest, "phone");
        //获取用户token
        String token = cookieService.getCookie(httpServletRequest, "token");
        //获取本地存入token
        String userToken = jedisCluster.get(RedisConst.APP_USER_TOKEN + phone);
        if (StringUtils.isEmpty(userToken) || !userToken.equals(token)) {
            throw new CommonException(300, "当前会话已失效，请重新登陆");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //获取cookie中用户手机号
        String phone = cookieService.getCookie(httpServletRequest, "phone");
        //获取用户token
        String token = cookieService.getCookie(httpServletRequest, "token");
        //获取本地token
        String userToken = jedisCluster.get(RedisConst.APP_USER_TOKEN + phone);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(userToken)) {
            throw new CommonException(300, "当前会话已失效,请重新登陆");
        }
        //延长token时间
        jedisCluster.expire(RedisConst.APP_USER_TOKEN + phone, time * 60);
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("token", token);
        cookieService.writeCookie(httpServletResponse, map);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
