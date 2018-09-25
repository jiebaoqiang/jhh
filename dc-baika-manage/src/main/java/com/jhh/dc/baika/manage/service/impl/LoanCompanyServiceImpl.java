package com.jhh.dc.baika.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.manage.mapper.LoanCompanyMapper;
import com.jhh.dc.baika.entity.manager.LoanCompany;
import com.jhh.dc.baika.manage.service.loan.LoanCompanyService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * 公司相关
 */
@Service
public class LoanCompanyServiceImpl implements LoanCompanyService {

    private static Log LOG = LogFactory.getLog(LoanCompanyServiceImpl.class);

    @Autowired
    private LoanCompanyMapper loanCompanyMapper;

    @Override
    public List<LoanCompany> selectAllLoanCompanys() {
        return loanCompanyMapper.selectAll();
    }

    @Override
    public List<LoanCompany> selectLoanCompaniesByExample(Map<String, Object> conditionMap) {
        Example queryExample = new Example(LoanCompany.class);
        Example.Criteria criteria = queryExample.createCriteria();
        handleCondition(conditionMap, criteria);
        List<LoanCompany> loanCompanies = loanCompanyMapper.selectByExample(queryExample);
        return loanCompanies;
    }

    @Override
    public PageInfo<LoanCompany> selectLoanCompaniesByPage(Map<String, Object> queryMap, int offset, int size) {

        Example queryExample = new Example(LoanCompany.class);
        Example.Criteria criteria = queryExample.createCriteria();

        handleCondition(queryMap, criteria);

        if (queryMap.containsKey("selector") && queryMap.containsKey("desc")) {
            Example.OrderBy orderBy = queryExample.orderBy(queryMap.get("selector").toString());
            if ("desc".equals(queryMap.get("desc"))) {
                orderBy.desc();
            }
            orderBy.orderBy("status").orderBy("createDate").desc();
        } else {
            queryExample.orderBy("status").orderBy("createDate").desc();
        }

        PageHelper.offsetPage(offset, size);
        List<LoanCompany> loanCompanies = loanCompanyMapper.selectByExample(queryExample);
        PageInfo<LoanCompany> pageInfo = new PageInfo<>(loanCompanies);

        return pageInfo;
    }

    private static void handleCondition(Map<String, Object> conditionMap, Example.Criteria criteria) {
        for (String entry : conditionMap.keySet()) {
            try {
                criteria.andEqualTo(entry, conditionMap.get(entry));
            } catch (MapperException e) {
                if(LOG.isWarnEnabled()) {
                    LOG.warn(e);
                }
            }
        }
    }
}
