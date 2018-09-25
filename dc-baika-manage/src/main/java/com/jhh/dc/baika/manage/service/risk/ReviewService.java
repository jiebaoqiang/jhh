package com.jhh.dc.baika.manage.service.risk;

import com.jhh.dc.baika.manage.entity.Response;

public interface ReviewService {
    /**
     * 人工审核操作
     * @param borroId
     * @param operationType
     * @return
     */
    Response saveManuallyReview(Integer borroId, String reason, String userNum, Integer operationType) throws Exception;

    /**
     * 更新风控审核记录
     * @param borroId
     * @param reason
     * @param employNum
     */
    Response saveReview(Integer borroId, String reason, String employNum);

    /**
     * 风控转件
     * @param brroIds
     * @param userNum
     * @return
     */
    Response transfer(String brroIds, String userNum);

    /**
     * 风控取消
     * @param brroIds
     * @param userNum
     * @return
     */
    Response cancel(String brroIds, String reason, String userNum);


    /**
     * 风控放款
     * @param brroId
     * @param userNum
     * @return
     */
    Response pay(Integer brroId, String userNum, String payChannel);



}