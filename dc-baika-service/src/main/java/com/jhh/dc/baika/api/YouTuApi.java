package com.jhh.dc.baika.api;

import com.jhh.dc.baika.api.annotation.ApiAnnotation;
import com.jhh.dc.baika.api.annotation.Parse;
import com.jhh.dc.baika.entity.bankdeposit.YoutuQuery;
import com.jhh.dc.baika.entity.bankdeposit.YoutuResult;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.Map;

/**
 * Created by zhangqi on 2018/8/23.
 */
@ApiAnnotation(baseUrl = "youtubaika_request_url",fileName = "third",
        timeOut = {100,100,100},parseType = Parse.JSON,note = "悠兔")
public interface YouTuApi {

    /**
     * 开户注册
     * @return
     */
    @FormUrlEncoded
    @POST("interface/query")
    Call<YoutuResult<YoutuQuery>> query(@FieldMap Map<String, String> map);

    /**
     * 用户还款
     * @return
     */
    @FormUrlEncoded
    @POST("interface/repayment")
    Call<YoutuResult<YoutuQuery>> repayment(@FieldMap Map<String, String> map);
}
