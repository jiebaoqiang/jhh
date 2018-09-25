package com.jhh.dc.baika.manage.mapper;



import java.util.List;

import com.jhh.dc.baika.entity.manager.Feedback;
import com.jhh.dc.baika.entity.manager_vo.FeedbackVo;

public interface FeedbackMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Feedback record);

    int insertSelective(Feedback record);

    Feedback selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Feedback record);

    int updateByPrimaryKey(Feedback record);
    
    List<FeedbackVo> getFeedbackList();
}