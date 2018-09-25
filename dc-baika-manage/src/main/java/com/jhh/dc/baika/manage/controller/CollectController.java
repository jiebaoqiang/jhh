package com.jhh.dc.baika.manage.controller;

import com.jhh.dc.baika.manage.entity.Result;
import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.common.util.RedisConst;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;


/**
 * 审核管理
 * Create by Jxl on 2017/9/11
 */
@Controller
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 切换代扣到海尔金融
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/switchDeductToHaier")
    public Result switchDeductToHaier(HttpServletRequest request){
        jedisCluster.hget("13636569813",RedisConst.LOGIN_REMIND_NOTICE);
        return null;
//        return switchDeductType(request, Constants.HAIER_PLATFORM);
    }

    /**
     * 切换代扣到银生宝
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/switchDeductToYsb")
    public Result switchDuductToYsb(HttpServletRequest request){
        return switchDeductType(request, Constants.YSB_PLATFORM);
    }

    @ResponseBody
    @RequestMapping("/switchDeductType")
    public Result switchDeductType(HttpServletRequest request, String type) {
        Result result = new Result();
        try {
            String status = jedisCluster.set(RedisConst.DEDUCT_TYPE_KEY, type);
            result.setCode(Result.SUCCESS);
            result.setMessage(status);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Result.FAIL);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    @ResponseBody
    @RequestMapping("/switchPayToHaier")
    public Result switchPayToHaier(HttpServletRequest request){
        return switchPayType(request, Constants.HAIER_PLATFORM);
    }

    @ResponseBody
    @RequestMapping("/switchPayType")
    private Result switchPayType(HttpServletRequest request, String type) {
        Result result = new Result();
        try {
            String status = jedisCluster.set(RedisConst.PAY_TYPE_KEY, type);
            result.setCode(Result.SUCCESS);
            result.setMessage(status);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Result.FAIL);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    /**
     * 切换代扣到银生宝
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/switchPayToYsb")
    public Result switchPayToYsb(HttpServletRequest request){
        return switchPayType(request, Constants.YSB_PLATFORM);
    }

    /**
     * 退款切换银生宝
     */
    @RequestMapping("/switchRefundToYsb")
    @ResponseBody
    public Result switchRefundToYsb(){
        return this.switchRefund(Constants.YSB_PLATFORM);
    }

    /**
     * 退款切换到海尔
     */
    @ResponseBody
    @RequestMapping("/switchRefundToHaier")
    public Result switchRefundToHaier(){
        return switchRefund(Constants.HAIER_PLATFORM);
    }

    /**
     * 切换Redis缓存支付渠道
     * @param type 支付渠道对应的key
     */
    public Result switchRefund(String type) {
        Result result = new Result();
        try {
            String status = jedisCluster.set(RedisConst.REFUND_TYPE_KEY, type);
            result.setCode(Result.SUCCESS);
            result.setMessage(status);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Result.FAIL);
            result.setMessage(e.getMessage());
            return result;
        }
    }

}
