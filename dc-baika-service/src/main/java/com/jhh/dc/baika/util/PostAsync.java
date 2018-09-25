package com.jhh.dc.baika.util;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.HttpUrlPost;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.common.util.thread.runner.AbstractSimpleRunner;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 2018/7/16.
 */
@Slf4j
public class PostAsync<T> extends AbstractSimpleRunner {

    private T obj;

    /**
     * 数据发送A公司请求地址
     */
    private String companyUrl;

    private BorrowListMapper borrowListMapper;

    public PostAsync(T obj,String companyUrl,BorrowListMapper borrowListMapper) {
        this.obj = obj;
        this.companyUrl = companyUrl;
        this.borrowListMapper = borrowListMapper;
    }

    @Override
    public boolean doExecute() {
        log.info("同步A公司请求参数 T= "+obj+"发送地址 companyUrl"+companyUrl);
        Map<String,String> map = MapUtils.setConditionMap(obj);
        String result = HttpUrlPost.sendPost(companyUrl, map);
        log.info("同步A公司请求数响应-----------------\n" + result);
        JSONObject json = JSONObject.parseObject(result);
        if ("200".equals(json.getString("code"))) {
            //当同步状态为最终态时，加入同步完成标识
          if (obj instanceof BorrowList){
              BorrowList borr = (BorrowList) obj;
              borr.setSync(getSync(borr.getBorrStatus()));
              borrowListMapper.updateByPrimaryKeySelective(borr);
          }
        } else {
            AsyncExecutor.delayExecute(new PostAsync<>(obj,companyUrl,borrowListMapper), 5, TimeUnit.MINUTES);
        }
        return true;
    }


    /**
     *  同步成功获取位移值
     * @param borrStatus
     * @return
     */
    private int getSync(String borrStatus){
        if (StringUtils.isNotEmpty(borrStatus)) {
            if (CodeReturn.STATUS_CANCEL.equals(borrStatus)){
                return CodeReturn.BORRSTATUS_CANCEL_SYNC;
            }else if (CodeReturn.STATUS_PAY_BACK.equals(borrStatus) || CodeReturn.STATUS_DELAY_PAYBACK.equals(borrStatus)
                    || CodeReturn.STATUS_EARLY_PAYBACK.equals(borrStatus)){
                return CodeReturn.BORRSTATUS_FINAL_SYNC;
            }else if (CodeReturn.STATUS_LATE_REPAY.equals(borrStatus)){
                return CodeReturn.BORRSTATUS_OVERDUE_SYNC;
            }else if (CodeReturn.STATUS_REVIEW_FAIL.equals(borrStatus) || CodeReturn.STATUS_PHONE_REVIEW_FAIL.equals(borrStatus)){
                return CodeReturn.BORRSTATUS_CHECK_SYNC;
            }
            return 0;
        }else {
            return 0;
        }
    }
}
