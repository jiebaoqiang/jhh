package com.jhh.dc.baika.api.baikelu;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.baikelu.BaikeluRemindExcelVo;
import com.jhh.dc.baika.entity.baikelu.BaikeluRemindVo;

import java.util.List;

/**
 * 百可录提醒电话服务接口
 */
public interface BaikeluRemindService {

    /**
     * 电话提醒API形式
     * @param remindVoList 接收对象
     * @param type    内容类型
     * @return
     */
    ResponseDo sendBaikeluRemindApi(List<BaikeluRemindVo> remindVoList, String type);

    /**
     * 电话提醒excel形式
     * @param remindVoList 接收对象
     * @param type    内容类型
     * @return
     */
    ResponseDo sendBaikeluRemindExcel(List<BaikeluRemindExcelVo> remindVoList, String type);

    /**
     * 百可录开关控制
     * @param redisKey
     * @param flag true开  false
     * @return
     */
    ResponseDo baikeluOnOffControl(String redisKey,String flag);

    /**
     * 百可录回调函数Excel形式
     * @param
     * @return
     */
    ResponseDo baikeluCallbackRemind(String jobData);

    /**
     * 百可录回调函数Api形式
     * @param
     * @return
     */
    String baikeluCallbackRemindApi(String jobRef,String workResult);

    /**
     * 百可录异常报文返回Api形式
     */
    String baikeluCallBackFailApi(String code,String result);

    /**
     * 百可录正常报文返回Api形式
     */

    String baikeluCallBackSuccApi();

}
