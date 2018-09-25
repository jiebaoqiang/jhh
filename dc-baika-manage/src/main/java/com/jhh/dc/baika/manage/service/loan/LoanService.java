package com.jhh.dc.baika.manage.service.loan;

import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.entity.loan.ExportWorkReport;

import java.util.List;
import java.util.Map;

/**
 * Created by chenchao on 2018/1/11.
 */
public interface LoanService {
    /**
     * 聚信立信用
     *
     * @return
     */
    List getPolyXinliCredit(Integer perId, int offset, int size);

    /**
     * 通讯录信息
     */
    List getContact(Integer perId, int offset, int size);

    /**
     * 备注信息
     *
     * @return
     */
    List getMemos(Map<String, String[]> param, Integer borrId);

    /**
     * 获取工作报表
     *
     * @param map
     * @return
     */
    List<ExportWorkReport> workReport(Map<String, Object> map);

    /**
     * 流水列表
     * @param borrId
     * @return
     * @throws Exception
     */
    List getOrderList(Map<String, String[]> param, Integer borrId);

    /**
     * 单独查询订单状态接口
     * @param serialNo 订单号
     * @return
     */
    Response getOrderState(String serialNo) throws Exception;
}
