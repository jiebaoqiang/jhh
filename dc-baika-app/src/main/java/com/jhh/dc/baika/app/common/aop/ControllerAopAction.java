package com.jhh.dc.baika.app.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jhh.dc.baika.common.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * controller aop
 * 切入controller, 记录执行过程
 * @author xingmin
 */
@Aspect
public class ControllerAopAction {

    private long BEGIN_TIME ;

//    @Pointcut("execution(* com.jhh.dc.baika.app.app..*.*(..))")
//    private void controllerAspect(){}
//
//    /**
//     * 方法开始执行
//     */
//    @Before("controllerAspect()")
//    public void doBefore(){
//        BEGIN_TIME = System.currentTimeMillis();
//    }
//
//    /**
//     * 方法结束执行
//     */
//    @After("controllerAspect()")
//    public void after(){
//        System.out.println(String.format("----【%s】----> 执行完成, takes[%s] millisecond.", Thread.currentThread().getName(), System.currentTimeMillis() - BEGIN_TIME));
//    }

//    /**
//     * 方法结束执行后的操作
//     */
//    @AfterReturning("controllerAspect()")
//    public void doAfter(){}
//
//    /**
//     * 方法有异常时的操作
//     */
//    @AfterThrowing("controllerAspect()")
//    public void doAfterThrow(){
//        System.out.println(String.format("----【%s】----> exception", Thread.currentThread().getName()));
//    }

    /**
     * 方法执行
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.jhh.dc.baika.app.web..*.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        BEGIN_TIME = System.currentTimeMillis();
        System.out.println(String.format("--------> 正在执行时间【%s】",DateUtil.getDateStringToHHmmss(new Date())));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 拦截的实体类，就是当前正在执行的controller
        Object target = proceedingJoinPoint.getTarget();
        // 拦截的方法名称。当前正在执行的方法
        String methodName = proceedingJoinPoint.getSignature().getName();
        // 拦截的放参数类型
        Signature sig = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException(String.format("----【%s】----> 该注解只能用于方法", Thread.currentThread().getName()));
        }
        methodSignature = (MethodSignature) sig;
        Class[] parameterTypes = methodSignature.getMethod().getParameterTypes();

        // 拦截的方法参数名
        String[] parameterNames = methodSignature.getParameterNames();
        // 拦截的方法参数值
        Object[] args = proceedingJoinPoint.getArgs();
        Map<String, Object> parameterMap = new HashMap<>(16);

        for (int i = 0; i < parameterNames.length; i++) {
            if (args[i] instanceof HttpServletRequest) {
                buildParameterMap(parameterMap, (HttpServletRequest) args[i]);
                continue;
            }

            if (args[i] instanceof String) {
                String param = (String) args[i];
                parameterMap.put(parameterNames[i], param.length() > 50 ? String.format("%s ...", param.substring(0, 50)) : param);
                continue;
            }
            parameterMap.put(parameterNames[i], args[i]);
        }

        Method method = null;
        try {
            method = target.getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }

        if (null != method) {
            System.out.println(String.format("----【%s】----> 正在执行【%s】中的【%s】, 参数【%s】, url【%s】, ip【%s】", Thread.currentThread().getName(), target.getClass().getName(), methodName, parameterMap, request.getRequestURI(), getIp(request)));
        } else {
            System.out.println(String.format("----【%s】----> 未查询到【%s】中的【%s】, 参数【%s】, url【%s】, ip【%s】", Thread.currentThread().getName(), target.getClass().getName(), methodName, parameterMap, request.getRequestURI(), getIp(request)));
        }
        Object obj = proceedingJoinPoint.proceed();
        System.out.println(String.format("----【%s】----> 执行完成, takes[%s] millisecond.", Thread.currentThread().getName(), System.currentTimeMillis() - BEGIN_TIME));
        System.out.println(String.format("--------> 执行返回的结果【%s】",obj));
        return obj;
    }

    /**
     * 获取ip地址
     * @param request
     * @return
     */
    private String getIp(HttpServletRequest request){
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }

    private void buildParameterMap(Map<String, Object> map, HttpServletRequest request) {
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            map.put(name, request.getParameter(name).length() > 50 ? String.format("%s ...", request.getParameter(name).substring(0, 50)) : request.getParameter(name));
        }
    }

}
