package com.jhh.dc.baika.mapper.manager;


import com.jhh.dc.baika.entity.loan.CollectorsList;

import tk.mybatis.mapper.common.Mapper;

public interface CollectorsListMapper extends Mapper<CollectorsList> {


    int updateCollectorsList(Integer borrId);

    String selectCollectUserByBorrId(Integer borrId);
    //把collections_list表的字段更新为1
    void deleteCollection(int borrId);
}
