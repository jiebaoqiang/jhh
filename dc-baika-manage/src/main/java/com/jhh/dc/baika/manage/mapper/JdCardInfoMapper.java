package com.jhh.dc.baika.manage.mapper;

import com.jhh.dc.baika.manage.service.excel.ImportJDCardExcelEntity;
import com.jhh.dc.baika.entity.app.JdCardInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * carl.wan
 * 2018年7月24日 15:42:18
 */
public interface JdCardInfoMapper extends Mapper<JdCardInfo> {

    /**
     * 批量保存excel数据
     * @param cardExcels
     * @return
     */
    int saveCardExcel(List<ImportJDCardExcelEntity> cardExcels);
}
