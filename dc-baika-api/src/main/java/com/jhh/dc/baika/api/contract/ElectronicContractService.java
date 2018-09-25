
package com.jhh.dc.baika.api.contract;

import java.io.UnsupportedEncodingException;

/**
 *描述：电子合同api
 *@author: carl.wan
 *@date： 日期：2017年11月23日 10:33:00
 */
public interface ElectronicContractService {

    /**
     * 生成合同
     * @param borrId
     * @return
     */
    String createElectronicContract(Integer borrId);
    /**
     * 查询预览合同
     * @param borrId
     * @return
     */
    String queryElectronicContract(Integer borrId);

    /**
     * 查询正式合同
     * @param borrNum
     * @return
     */
    String downElectronicContract(String borrNum);

    /**
     * 回调
     * @param borrNum
     * @return
     */
    String callBack(String code, String url, String borrNum) throws UnsupportedEncodingException;

    /**
     * 处理异常电子合同数据
     * @return
     */
    void disposeExceptionContract();

}
