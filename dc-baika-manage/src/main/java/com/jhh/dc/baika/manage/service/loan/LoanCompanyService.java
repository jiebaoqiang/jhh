package com.jhh.dc.baika.manage.service.loan;

import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.entity.manager.LoanCompany;

import java.util.List;
import java.util.Map;

public interface LoanCompanyService {

    /**
     * 查询所有公司
     *
     * @return
     */
    List<LoanCompany> selectAllLoanCompanys();

    /**
     * 按条件查询公司列表
     * @param conditionMap 查询条件Map
     * @return List<LoanCompany> 公司列表
     */
    List<LoanCompany> selectLoanCompaniesByExample(Map<String, Object> conditionMap);

    /**
     * 分页查询公司
     *
     * @param queryMap
     * @param offset
     * @param size
     * @return
     */
    PageInfo<LoanCompany> selectLoanCompaniesByPage(Map<String, Object> queryMap, int offset, int size);
}
