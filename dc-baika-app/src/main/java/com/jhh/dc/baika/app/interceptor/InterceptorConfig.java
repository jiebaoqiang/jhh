package com.jhh.dc.baika.app.interceptor;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 表单提交类总拦截器 失败跳转页面
 */
@Configuration
@AllArgsConstructor
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    private FormInterceptor userInterceptor;

    private LogicInterceptor logicInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //入口处将token信息放入cookie中
        registry.addInterceptor(userInterceptor).addPathPatterns("/form/**");
        //拦截所有ajax请求验证token使用 图形码 ，回调 不需要拦截
        registry.addInterceptor(logicInterceptor).addPathPatterns("/**").excludePathPatterns("/form/**","/user/validateCode","/loan/updateBorrowStatusByBorrowNum/**"
        ,"/callback/**","/account/**","");
    }
}
