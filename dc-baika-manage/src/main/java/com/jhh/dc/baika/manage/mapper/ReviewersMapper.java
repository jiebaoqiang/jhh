package com.jhh.dc.baika.manage.mapper;


import com.jhh.dc.baika.entity.app.Reviewers;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ReviewersMapper extends Mapper<Reviewers> {

    int updateReviewersIsDelete(String employNum);

    List selectReviewsByEmployNum(String employNum);

    List selectReviewsEmployeeNum();

}
