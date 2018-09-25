package com.jhh.dc.baika.mapper.loan;

import org.apache.ibatis.annotations.Param;

/**
 * 2018/6/20.
 */
public interface PayChannelAdapterMapper {

    String getChannelBypayCenterAndType(@Param("payCenterChannel") String payCenterChannel, @Param("type") String type);
}
