package com.jhh.dc.baika.manage.mapper;

import com.jhh.dc.baika.manage.entity.RobotQuestion;

import tk.mybatis.mapper.common.Mapper;

public interface RobotQuestionMapper extends Mapper<RobotQuestion> {

    int insertRobotQuestion(RobotQuestion robotQuestion);

}
