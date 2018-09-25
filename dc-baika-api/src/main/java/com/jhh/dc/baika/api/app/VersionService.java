package com.jhh.dc.baika.api.app;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.app.Version;


/**版本更新接口
 * @author xuepengfei
 */
public interface VersionService {

    /**根据版本号及设备 查询最新版本
     * @param clientName
     * @param versionName
     * @return
     */
    ResponseDo<Version> getVersionByVersionName(String clientName, String versionName);

}
