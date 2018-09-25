package com.jhh.dc.baika.service.notice;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.api.baikelu.BaikeluRemindService;
import com.jhh.dc.baika.api.contract.ElectronicContractService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.notice.UserBehaviorNoticeService;
import com.jhh.dc.baika.common.util.DateUtil;
import com.jhh.dc.baika.common.util.Detect;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.entity.app.Product;
import com.jhh.dc.baika.entity.baikelu.BaikeluRemindExcelVo;
import com.jhh.dc.baika.entity.baikelu.BaikeluRemindVo;
import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.entity.contract.Contract;
import com.jhh.dc.baika.entity.contract.IdEntity;
import com.jhh.dc.baika.common.enums.BaikeluRemindEnum;
import com.jhh.dc.baika.common.enums.ContractDataCodeEnum;
import com.jinhuhang.settlement.service.SettlementAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisCluster;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by wanzezhong on 2018年5月29日 14:41:39
 *
 * @author carl.wan
 */
@Slf4j
@Service
public class UserBehaviorNoticeServiceImpl implements UserBehaviorNoticeService {

    @Autowired
    JedisCluster jedisCluster;
    @Autowired
    BaikeluRemindService baikeluRemindService;

    @Override
    public NoteResult registerNotice() {
        String lock = jedisCluster.get(RedisConst.REGISTER_REMIND_NOTICE_LOCK);
        //判断锁是关闭
        log.info("REGISTER_REMIND_NOTICE_LOCK:" + lock);
        if(lock != null && lock.equals(BaikeluRemindEnum.IS_CLOSE.getCode())){
            Map<String, String> map = jedisCluster.hgetAll(RedisConst.REGISTER_REMIND_NOTICE);
            if(Detect.notEmpty(map)){
                List<String> list = new ArrayList();
               // List<BaikeluRemindExcelVo> param = new ArrayList();
                List<BaikeluRemindVo> param = new ArrayList();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    //超过1分钟发起调用
                    if(secondNow(entry.getValue()) >= 60){
                        list.add(entry.getKey());
//                        BaikeluRemindExcelVo baikeluRemindExcelVo = new BaikeluRemindExcelVo();
//                        baikeluRemindExcelVo.setPhone_num(entry.getKey());
//                        baikeluRemindExcelVo.setJob_ref(UUID.randomUUID().toString());
//                        baikeluRemindExcelVo.setRemind_type(BaikeluRemindEnum.NO_DOWNAPP_REMIND.getStatus());
                        BaikeluRemindVo baikeluRemindVo=new BaikeluRemindVo();
                        baikeluRemindVo.setCsv_phone_num(entry.getKey());
                        baikeluRemindVo.setCsv_arn(UUID.randomUUID().toString());
                        baikeluRemindVo.setCsv_tag(BaikeluRemindEnum.NO_DOWNAPP_REMIND.getStatus());
                        param.add(baikeluRemindVo);
                    }
                }
                //有值拨打电话，清除缓存
                if(Detect.notEmpty(list)){
                    //调用百可录打电话
                    ResponseDo result =  baikeluRemindService.sendBaikeluRemindApi(param, BaikeluRemindEnum.NO_DOWNAPP_REMIND.getCode());

                    //删除缓存记录
                    if(result.getCode() == 200){
                        delRegisterRedis(list.toArray(new String[0]));
                    }
                }

            }

        }
        return null;
    }

    @Override
    public NoteResult loginNotice() {
        String lock = jedisCluster.get(RedisConst.LOGIN_REMIND_NOTICE_LOCK);
        //判断锁是关闭
        log.info("LOGIN_REMIND_NOTICE_LOCK:" + lock);
        if(lock != null && lock.equals(BaikeluRemindEnum.IS_CLOSE.getCode())){
            Map<String, String> map = jedisCluster.hgetAll(RedisConst.LOGIN_REMIND_NOTICE);
            if(Detect.notEmpty(map)){

                List<String> list = new ArrayList();
                //List<BaikeluRemindExcelVo> param = new ArrayList();
                List<BaikeluRemindVo> param = new ArrayList();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    //超过30分钟发起调用
                    //TODO 百可录时间生产更新为30分钟触发
                    if(secondNow(entry.getValue()) >= 60 * 30){
                        list.add(entry.getKey());
//                        BaikeluRemindExcelVo baikeluRemindExcelVo = new BaikeluRemindExcelVo();
//                        baikeluRemindExcelVo.setPhone_num(entry.getKey());
//                        baikeluRemindExcelVo.setJob_ref(UUID.randomUUID().toString());
//                        baikeluRemindExcelVo.setRemind_type(BaikeluRemindEnum.NO_LOGIN_REMIND.getStatus());
//                        param.add(baikeluRemindExcelVo);
                        BaikeluRemindVo baikeluRemindVo=new BaikeluRemindVo();
                        baikeluRemindVo.setCsv_tag(BaikeluRemindEnum.NO_LOGIN_REMIND.getStatus());
                        baikeluRemindVo.setCsv_arn(UUID.randomUUID().toString());
                        baikeluRemindVo.setCsv_phone_num(entry.getKey());
                        param.add(baikeluRemindVo);
                    }
                }

                //有值拨打电话，清除缓存
                if(Detect.notEmpty(list)){
                    //调用百可录打电话
                    ResponseDo result =  baikeluRemindService.sendBaikeluRemindApi(param, BaikeluRemindEnum.NO_LOGIN_REMIND.getCode());

                    //删除缓存记录
                    if(result.getCode() == 200){
                        delLoginRedis(list.toArray(new String[0]));
                    }
                }

            }
        }
        return null;
    }

    @Override
    public void addRegisterRedis(String phone){
        if(Detect.notEmpty(phone)){
            jedisCluster.hset(RedisConst.REGISTER_REMIND_NOTICE, phone, DateUtil.getDateStringToHHmmss(new Date()));
        }
    }

    @Override
    public void addLoginRedis(String phone){
        if(Detect.notEmpty(phone)){
            jedisCluster.hset(RedisConst.LOGIN_REMIND_NOTICE, phone, DateUtil.getDateStringToHHmmss(new Date()));
        }
    }

    @Override
    public void delRegisterRedis(String... phone){
        if(Detect.notEmpty(phone)){
            jedisCluster.hdel(RedisConst.REGISTER_REMIND_NOTICE, phone);
        }
    }
    @Override
    public void delLoginRedis(String... phone){
        if(Detect.notEmpty(phone)){
            jedisCluster.hdel(RedisConst.LOGIN_REMIND_NOTICE, phone);
        }
    }

    public int secondNow(String time){
        Date nowDate = Calendar.getInstance().getTime();
        Date date = DateUtil.getDateHHmmss(time);
        return (int) ((nowDate.getTime() - date.getTime()) / 1000);
    }

    public static void main(String[] arge) {
    }
}
