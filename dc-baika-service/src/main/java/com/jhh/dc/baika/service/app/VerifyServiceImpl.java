package com.jhh.dc.baika.service.app;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.mapper.app.PersonMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

/**
 * 2018/1/2.
 */
@Service
public class VerifyServiceImpl {

    private static final Logger logger = LoggerFactory
            .getLogger(VerifyServiceImpl.class);

    @Autowired
    private PersonMapper personMapper;

    /**
     * 验证用户是否存在
     *
     * @param perId
     * @return
     */
    public boolean verifyPerson(String perId) {

        Person p = personMapper.selectByPrimaryKey(Integer.valueOf(perId));
        return p != null;
    }

    public static void main(String[] args) throws ParseException {

    }
}