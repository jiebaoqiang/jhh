package com.jhh.dc.baika.manage.service.robot;


import javax.servlet.http.HttpServletRequest;

import com.jhh.dc.baika.manage.entity.Response;

/**
 * 机器人接口
 * @author carl.wan
 *2017年9月11日 15:25:25
 */
public interface RobotService {

    /**
     * 发送风控订单
     * @param borrId 合同Id
     * @return
     */
    Response sendRcOrder(Integer borrId) throws Exception;
    /**
     * 风控订单回调结果
     * @param
     * @return
     */
    Response callBackRc(HttpServletRequest request) throws Exception;

    /**
     * 查询百可录订单
     * @param borrNum
     * @return
     */
    Response robotOrderByBorrNum(String borrNum, HttpServletRequest request);
}
