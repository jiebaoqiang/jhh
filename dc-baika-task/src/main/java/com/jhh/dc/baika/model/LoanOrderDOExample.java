package com.jhh.dc.baika.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanOrderDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LoanOrderDOExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andGuidIsNull() {
            addCriterion("guid is null");
            return (Criteria) this;
        }

        public Criteria andGuidIsNotNull() {
            addCriterion("guid is not null");
            return (Criteria) this;
        }

        public Criteria andGuidEqualTo(String value) {
            addCriterion("guid =", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotEqualTo(String value) {
            addCriterion("guid <>", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidGreaterThan(String value) {
            addCriterion("guid >", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidGreaterThanOrEqualTo(String value) {
            addCriterion("guid >=", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLessThan(String value) {
            addCriterion("guid <", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLessThanOrEqualTo(String value) {
            addCriterion("guid <=", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLike(String value) {
            addCriterion("guid like", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotLike(String value) {
            addCriterion("guid not like", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidIn(List<String> values) {
            addCriterion("guid in", values, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotIn(List<String> values) {
            addCriterion("guid not in", values, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidBetween(String value1, String value2) {
            addCriterion("guid between", value1, value2, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotBetween(String value1, String value2) {
            addCriterion("guid not between", value1, value2, "guid");
            return (Criteria) this;
        }

        public Criteria andPIdIsNull() {
            addCriterion("p_id is null");
            return (Criteria) this;
        }

        public Criteria andPIdIsNotNull() {
            addCriterion("p_id is not null");
            return (Criteria) this;
        }

        public Criteria andPIdEqualTo(Integer value) {
            addCriterion("p_id =", value, "pId");
            return (Criteria) this;
        }

        public Criteria andPIdNotEqualTo(Integer value) {
            addCriterion("p_id <>", value, "pId");
            return (Criteria) this;
        }

        public Criteria andPIdGreaterThan(Integer value) {
            addCriterion("p_id >", value, "pId");
            return (Criteria) this;
        }

        public Criteria andPIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("p_id >=", value, "pId");
            return (Criteria) this;
        }

        public Criteria andPIdLessThan(Integer value) {
            addCriterion("p_id <", value, "pId");
            return (Criteria) this;
        }

        public Criteria andPIdLessThanOrEqualTo(Integer value) {
            addCriterion("p_id <=", value, "pId");
            return (Criteria) this;
        }

        public Criteria andPIdIn(List<Integer> values) {
            addCriterion("p_id in", values, "pId");
            return (Criteria) this;
        }

        public Criteria andPIdNotIn(List<Integer> values) {
            addCriterion("p_id not in", values, "pId");
            return (Criteria) this;
        }

        public Criteria andPIdBetween(Integer value1, Integer value2) {
            addCriterion("p_id between", value1, value2, "pId");
            return (Criteria) this;
        }

        public Criteria andPIdNotBetween(Integer value1, Integer value2) {
            addCriterion("p_id not between", value1, value2, "pId");
            return (Criteria) this;
        }

        public Criteria andBorrNumIsNull() {
            addCriterion("borr_num is null");
            return (Criteria) this;
        }

        public Criteria andBorrNumIsNotNull() {
            addCriterion("borr_num is not null");
            return (Criteria) this;
        }

        public Criteria andBorrNumEqualTo(String value) {
            addCriterion("borr_num =", value, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumNotEqualTo(String value) {
            addCriterion("borr_num <>", value, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumGreaterThan(String value) {
            addCriterion("borr_num >", value, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumGreaterThanOrEqualTo(String value) {
            addCriterion("borr_num >=", value, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumLessThan(String value) {
            addCriterion("borr_num <", value, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumLessThanOrEqualTo(String value) {
            addCriterion("borr_num <=", value, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumLike(String value) {
            addCriterion("borr_num like", value, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumNotLike(String value) {
            addCriterion("borr_num not like", value, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumIn(List<String> values) {
            addCriterion("borr_num in", values, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumNotIn(List<String> values) {
            addCriterion("borr_num not in", values, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumBetween(String value1, String value2) {
            addCriterion("borr_num between", value1, value2, "borrNum");
            return (Criteria) this;
        }

        public Criteria andBorrNumNotBetween(String value1, String value2) {
            addCriterion("borr_num not between", value1, value2, "borrNum");
            return (Criteria) this;
        }

        public Criteria andSerialNoIsNull() {
            addCriterion("serial_no is null");
            return (Criteria) this;
        }

        public Criteria andSerialNoIsNotNull() {
            addCriterion("serial_no is not null");
            return (Criteria) this;
        }

        public Criteria andSerialNoEqualTo(String value) {
            addCriterion("serial_no =", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoNotEqualTo(String value) {
            addCriterion("serial_no <>", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoGreaterThan(String value) {
            addCriterion("serial_no >", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoGreaterThanOrEqualTo(String value) {
            addCriterion("serial_no >=", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoLessThan(String value) {
            addCriterion("serial_no <", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoLessThanOrEqualTo(String value) {
            addCriterion("serial_no <=", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoLike(String value) {
            addCriterion("serial_no like", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoNotLike(String value) {
            addCriterion("serial_no not like", value, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoIn(List<String> values) {
            addCriterion("serial_no in", values, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoNotIn(List<String> values) {
            addCriterion("serial_no not in", values, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoBetween(String value1, String value2) {
            addCriterion("serial_no between", value1, value2, "serialNo");
            return (Criteria) this;
        }

        public Criteria andSerialNoNotBetween(String value1, String value2) {
            addCriterion("serial_no not between", value1, value2, "serialNo");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIsNull() {
            addCriterion("company_id is null");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIsNotNull() {
            addCriterion("company_id is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyIdEqualTo(Integer value) {
            addCriterion("company_id =", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotEqualTo(Integer value) {
            addCriterion("company_id <>", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdGreaterThan(Integer value) {
            addCriterion("company_id >", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("company_id >=", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLessThan(Integer value) {
            addCriterion("company_id <", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLessThanOrEqualTo(Integer value) {
            addCriterion("company_id <=", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIn(List<Integer> values) {
            addCriterion("company_id in", values, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotIn(List<Integer> values) {
            addCriterion("company_id not in", values, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdBetween(Integer value1, Integer value2) {
            addCriterion("company_id between", value1, value2, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotBetween(Integer value1, Integer value2) {
            addCriterion("company_id not between", value1, value2, "companyId");
            return (Criteria) this;
        }

        public Criteria andPerIdIsNull() {
            addCriterion("per_id is null");
            return (Criteria) this;
        }

        public Criteria andPerIdIsNotNull() {
            addCriterion("per_id is not null");
            return (Criteria) this;
        }

        public Criteria andPerIdEqualTo(Integer value) {
            addCriterion("per_id =", value, "perId");
            return (Criteria) this;
        }

        public Criteria andPerIdNotEqualTo(Integer value) {
            addCriterion("per_id <>", value, "perId");
            return (Criteria) this;
        }

        public Criteria andPerIdGreaterThan(Integer value) {
            addCriterion("per_id >", value, "perId");
            return (Criteria) this;
        }

        public Criteria andPerIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("per_id >=", value, "perId");
            return (Criteria) this;
        }

        public Criteria andPerIdLessThan(Integer value) {
            addCriterion("per_id <", value, "perId");
            return (Criteria) this;
        }

        public Criteria andPerIdLessThanOrEqualTo(Integer value) {
            addCriterion("per_id <=", value, "perId");
            return (Criteria) this;
        }

        public Criteria andPerIdIn(List<Integer> values) {
            addCriterion("per_id in", values, "perId");
            return (Criteria) this;
        }

        public Criteria andPerIdNotIn(List<Integer> values) {
            addCriterion("per_id not in", values, "perId");
            return (Criteria) this;
        }

        public Criteria andPerIdBetween(Integer value1, Integer value2) {
            addCriterion("per_id between", value1, value2, "perId");
            return (Criteria) this;
        }

        public Criteria andPerIdNotBetween(Integer value1, Integer value2) {
            addCriterion("per_id not between", value1, value2, "perId");
            return (Criteria) this;
        }

        public Criteria andBankIdIsNull() {
            addCriterion("bank_id is null");
            return (Criteria) this;
        }

        public Criteria andBankIdIsNotNull() {
            addCriterion("bank_id is not null");
            return (Criteria) this;
        }

        public Criteria andBankIdEqualTo(Integer value) {
            addCriterion("bank_id =", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotEqualTo(Integer value) {
            addCriterion("bank_id <>", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdGreaterThan(Integer value) {
            addCriterion("bank_id >", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("bank_id >=", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdLessThan(Integer value) {
            addCriterion("bank_id <", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdLessThanOrEqualTo(Integer value) {
            addCriterion("bank_id <=", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdIn(List<Integer> values) {
            addCriterion("bank_id in", values, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotIn(List<Integer> values) {
            addCriterion("bank_id not in", values, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdBetween(Integer value1, Integer value2) {
            addCriterion("bank_id between", value1, value2, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotBetween(Integer value1, Integer value2) {
            addCriterion("bank_id not between", value1, value2, "bankId");
            return (Criteria) this;
        }

        public Criteria andContractIdIsNull() {
            addCriterion("contract_id is null");
            return (Criteria) this;
        }

        public Criteria andContractIdIsNotNull() {
            addCriterion("contract_id is not null");
            return (Criteria) this;
        }

        public Criteria andContractIdEqualTo(Integer value) {
            addCriterion("contract_id =", value, "contractId");
            return (Criteria) this;
        }

        public Criteria andContractIdNotEqualTo(Integer value) {
            addCriterion("contract_id <>", value, "contractId");
            return (Criteria) this;
        }

        public Criteria andContractIdGreaterThan(Integer value) {
            addCriterion("contract_id >", value, "contractId");
            return (Criteria) this;
        }

        public Criteria andContractIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("contract_id >=", value, "contractId");
            return (Criteria) this;
        }

        public Criteria andContractIdLessThan(Integer value) {
            addCriterion("contract_id <", value, "contractId");
            return (Criteria) this;
        }

        public Criteria andContractIdLessThanOrEqualTo(Integer value) {
            addCriterion("contract_id <=", value, "contractId");
            return (Criteria) this;
        }

        public Criteria andContractIdIn(List<Integer> values) {
            addCriterion("contract_id in", values, "contractId");
            return (Criteria) this;
        }

        public Criteria andContractIdNotIn(List<Integer> values) {
            addCriterion("contract_id not in", values, "contractId");
            return (Criteria) this;
        }

        public Criteria andContractIdBetween(Integer value1, Integer value2) {
            addCriterion("contract_id between", value1, value2, "contractId");
            return (Criteria) this;
        }

        public Criteria andContractIdNotBetween(Integer value1, Integer value2) {
            addCriterion("contract_id not between", value1, value2, "contractId");
            return (Criteria) this;
        }

        public Criteria andOptAmountIsNull() {
            addCriterion("opt_amount is null");
            return (Criteria) this;
        }

        public Criteria andOptAmountIsNotNull() {
            addCriterion("opt_amount is not null");
            return (Criteria) this;
        }

        public Criteria andOptAmountEqualTo(BigDecimal value) {
            addCriterion("opt_amount =", value, "optAmount");
            return (Criteria) this;
        }

        public Criteria andOptAmountNotEqualTo(BigDecimal value) {
            addCriterion("opt_amount <>", value, "optAmount");
            return (Criteria) this;
        }

        public Criteria andOptAmountGreaterThan(BigDecimal value) {
            addCriterion("opt_amount >", value, "optAmount");
            return (Criteria) this;
        }

        public Criteria andOptAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("opt_amount >=", value, "optAmount");
            return (Criteria) this;
        }

        public Criteria andOptAmountLessThan(BigDecimal value) {
            addCriterion("opt_amount <", value, "optAmount");
            return (Criteria) this;
        }

        public Criteria andOptAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("opt_amount <=", value, "optAmount");
            return (Criteria) this;
        }

        public Criteria andOptAmountIn(List<BigDecimal> values) {
            addCriterion("opt_amount in", values, "optAmount");
            return (Criteria) this;
        }

        public Criteria andOptAmountNotIn(List<BigDecimal> values) {
            addCriterion("opt_amount not in", values, "optAmount");
            return (Criteria) this;
        }

        public Criteria andOptAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("opt_amount between", value1, value2, "optAmount");
            return (Criteria) this;
        }

        public Criteria andOptAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("opt_amount not between", value1, value2, "optAmount");
            return (Criteria) this;
        }

        public Criteria andActAmountIsNull() {
            addCriterion("act_amount is null");
            return (Criteria) this;
        }

        public Criteria andActAmountIsNotNull() {
            addCriterion("act_amount is not null");
            return (Criteria) this;
        }

        public Criteria andActAmountEqualTo(BigDecimal value) {
            addCriterion("act_amount =", value, "actAmount");
            return (Criteria) this;
        }

        public Criteria andActAmountNotEqualTo(BigDecimal value) {
            addCriterion("act_amount <>", value, "actAmount");
            return (Criteria) this;
        }

        public Criteria andActAmountGreaterThan(BigDecimal value) {
            addCriterion("act_amount >", value, "actAmount");
            return (Criteria) this;
        }

        public Criteria andActAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("act_amount >=", value, "actAmount");
            return (Criteria) this;
        }

        public Criteria andActAmountLessThan(BigDecimal value) {
            addCriterion("act_amount <", value, "actAmount");
            return (Criteria) this;
        }

        public Criteria andActAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("act_amount <=", value, "actAmount");
            return (Criteria) this;
        }

        public Criteria andActAmountIn(List<BigDecimal> values) {
            addCriterion("act_amount in", values, "actAmount");
            return (Criteria) this;
        }

        public Criteria andActAmountNotIn(List<BigDecimal> values) {
            addCriterion("act_amount not in", values, "actAmount");
            return (Criteria) this;
        }

        public Criteria andActAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("act_amount between", value1, value2, "actAmount");
            return (Criteria) this;
        }

        public Criteria andActAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("act_amount not between", value1, value2, "actAmount");
            return (Criteria) this;
        }

        public Criteria andRlDateIsNull() {
            addCriterion("rl_date is null");
            return (Criteria) this;
        }

        public Criteria andRlDateIsNotNull() {
            addCriterion("rl_date is not null");
            return (Criteria) this;
        }

        public Criteria andRlDateEqualTo(Date value) {
            addCriterion("rl_date =", value, "rlDate");
            return (Criteria) this;
        }

        public Criteria andRlDateNotEqualTo(Date value) {
            addCriterion("rl_date <>", value, "rlDate");
            return (Criteria) this;
        }

        public Criteria andRlDateGreaterThan(Date value) {
            addCriterion("rl_date >", value, "rlDate");
            return (Criteria) this;
        }

        public Criteria andRlDateGreaterThanOrEqualTo(Date value) {
            addCriterion("rl_date >=", value, "rlDate");
            return (Criteria) this;
        }

        public Criteria andRlDateLessThan(Date value) {
            addCriterion("rl_date <", value, "rlDate");
            return (Criteria) this;
        }

        public Criteria andRlDateLessThanOrEqualTo(Date value) {
            addCriterion("rl_date <=", value, "rlDate");
            return (Criteria) this;
        }

        public Criteria andRlDateIn(List<Date> values) {
            addCriterion("rl_date in", values, "rlDate");
            return (Criteria) this;
        }

        public Criteria andRlDateNotIn(List<Date> values) {
            addCriterion("rl_date not in", values, "rlDate");
            return (Criteria) this;
        }

        public Criteria andRlDateBetween(Date value1, Date value2) {
            addCriterion("rl_date between", value1, value2, "rlDate");
            return (Criteria) this;
        }

        public Criteria andRlDateNotBetween(Date value1, Date value2) {
            addCriterion("rl_date not between", value1, value2, "rlDate");
            return (Criteria) this;
        }

        public Criteria andRlRemarkIsNull() {
            addCriterion("rl_remark is null");
            return (Criteria) this;
        }

        public Criteria andRlRemarkIsNotNull() {
            addCriterion("rl_remark is not null");
            return (Criteria) this;
        }

        public Criteria andRlRemarkEqualTo(String value) {
            addCriterion("rl_remark =", value, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkNotEqualTo(String value) {
            addCriterion("rl_remark <>", value, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkGreaterThan(String value) {
            addCriterion("rl_remark >", value, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("rl_remark >=", value, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkLessThan(String value) {
            addCriterion("rl_remark <", value, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkLessThanOrEqualTo(String value) {
            addCriterion("rl_remark <=", value, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkLike(String value) {
            addCriterion("rl_remark like", value, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkNotLike(String value) {
            addCriterion("rl_remark not like", value, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkIn(List<String> values) {
            addCriterion("rl_remark in", values, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkNotIn(List<String> values) {
            addCriterion("rl_remark not in", values, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkBetween(String value1, String value2) {
            addCriterion("rl_remark between", value1, value2, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlRemarkNotBetween(String value1, String value2) {
            addCriterion("rl_remark not between", value1, value2, "rlRemark");
            return (Criteria) this;
        }

        public Criteria andRlStateIsNull() {
            addCriterion("rl_state is null");
            return (Criteria) this;
        }

        public Criteria andRlStateIsNotNull() {
            addCriterion("rl_state is not null");
            return (Criteria) this;
        }

        public Criteria andRlStateEqualTo(String value) {
            addCriterion("rl_state =", value, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateNotEqualTo(String value) {
            addCriterion("rl_state <>", value, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateGreaterThan(String value) {
            addCriterion("rl_state >", value, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateGreaterThanOrEqualTo(String value) {
            addCriterion("rl_state >=", value, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateLessThan(String value) {
            addCriterion("rl_state <", value, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateLessThanOrEqualTo(String value) {
            addCriterion("rl_state <=", value, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateLike(String value) {
            addCriterion("rl_state like", value, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateNotLike(String value) {
            addCriterion("rl_state not like", value, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateIn(List<String> values) {
            addCriterion("rl_state in", values, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateNotIn(List<String> values) {
            addCriterion("rl_state not in", values, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateBetween(String value1, String value2) {
            addCriterion("rl_state between", value1, value2, "rlState");
            return (Criteria) this;
        }

        public Criteria andRlStateNotBetween(String value1, String value2) {
            addCriterion("rl_state not between", value1, value2, "rlState");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andReasonIsNull() {
            addCriterion("reason is null");
            return (Criteria) this;
        }

        public Criteria andReasonIsNotNull() {
            addCriterion("reason is not null");
            return (Criteria) this;
        }

        public Criteria andReasonEqualTo(String value) {
            addCriterion("reason =", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotEqualTo(String value) {
            addCriterion("reason <>", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonGreaterThan(String value) {
            addCriterion("reason >", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonGreaterThanOrEqualTo(String value) {
            addCriterion("reason >=", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLessThan(String value) {
            addCriterion("reason <", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLessThanOrEqualTo(String value) {
            addCriterion("reason <=", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLike(String value) {
            addCriterion("reason like", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotLike(String value) {
            addCriterion("reason not like", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonIn(List<String> values) {
            addCriterion("reason in", values, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotIn(List<String> values) {
            addCriterion("reason not in", values, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonBetween(String value1, String value2) {
            addCriterion("reason between", value1, value2, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotBetween(String value1, String value2) {
            addCriterion("reason not between", value1, value2, "reason");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreationDateIsNull() {
            addCriterion("creation_date is null");
            return (Criteria) this;
        }

        public Criteria andCreationDateIsNotNull() {
            addCriterion("creation_date is not null");
            return (Criteria) this;
        }

        public Criteria andCreationDateEqualTo(Date value) {
            addCriterion("creation_date =", value, "creationDate");
            return (Criteria) this;
        }

        public Criteria andCreationDateNotEqualTo(Date value) {
            addCriterion("creation_date <>", value, "creationDate");
            return (Criteria) this;
        }

        public Criteria andCreationDateGreaterThan(Date value) {
            addCriterion("creation_date >", value, "creationDate");
            return (Criteria) this;
        }

        public Criteria andCreationDateGreaterThanOrEqualTo(Date value) {
            addCriterion("creation_date >=", value, "creationDate");
            return (Criteria) this;
        }

        public Criteria andCreationDateLessThan(Date value) {
            addCriterion("creation_date <", value, "creationDate");
            return (Criteria) this;
        }

        public Criteria andCreationDateLessThanOrEqualTo(Date value) {
            addCriterion("creation_date <=", value, "creationDate");
            return (Criteria) this;
        }

        public Criteria andCreationDateIn(List<Date> values) {
            addCriterion("creation_date in", values, "creationDate");
            return (Criteria) this;
        }

        public Criteria andCreationDateNotIn(List<Date> values) {
            addCriterion("creation_date not in", values, "creationDate");
            return (Criteria) this;
        }

        public Criteria andCreationDateBetween(Date value1, Date value2) {
            addCriterion("creation_date between", value1, value2, "creationDate");
            return (Criteria) this;
        }

        public Criteria andCreationDateNotBetween(Date value1, Date value2) {
            addCriterion("creation_date not between", value1, value2, "creationDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIsNull() {
            addCriterion("update_date is null");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIsNotNull() {
            addCriterion("update_date is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateDateEqualTo(Date value) {
            addCriterion("update_date =", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotEqualTo(Date value) {
            addCriterion("update_date <>", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateGreaterThan(Date value) {
            addCriterion("update_date >", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("update_date >=", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateLessThan(Date value) {
            addCriterion("update_date <", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateLessThanOrEqualTo(Date value) {
            addCriterion("update_date <=", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIn(List<Date> values) {
            addCriterion("update_date in", values, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotIn(List<Date> values) {
            addCriterion("update_date not in", values, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateBetween(Date value1, Date value2) {
            addCriterion("update_date between", value1, value2, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotBetween(Date value1, Date value2) {
            addCriterion("update_date not between", value1, value2, "updateDate");
            return (Criteria) this;
        }

        public Criteria andSyncIsNull() {
            addCriterion("sync is null");
            return (Criteria) this;
        }

        public Criteria andSyncIsNotNull() {
            addCriterion("sync is not null");
            return (Criteria) this;
        }

        public Criteria andSyncEqualTo(String value) {
            addCriterion("sync =", value, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncNotEqualTo(String value) {
            addCriterion("sync <>", value, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncGreaterThan(String value) {
            addCriterion("sync >", value, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncGreaterThanOrEqualTo(String value) {
            addCriterion("sync >=", value, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncLessThan(String value) {
            addCriterion("sync <", value, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncLessThanOrEqualTo(String value) {
            addCriterion("sync <=", value, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncLike(String value) {
            addCriterion("sync like", value, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncNotLike(String value) {
            addCriterion("sync not like", value, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncIn(List<String> values) {
            addCriterion("sync in", values, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncNotIn(List<String> values) {
            addCriterion("sync not in", values, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncBetween(String value1, String value2) {
            addCriterion("sync between", value1, value2, "sync");
            return (Criteria) this;
        }

        public Criteria andSyncNotBetween(String value1, String value2) {
            addCriterion("sync not between", value1, value2, "sync");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(String value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(String value) {
            addCriterion("create_user <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(String value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(String value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(String value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(String value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLike(String value) {
            addCriterion("create_user like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotLike(String value) {
            addCriterion("create_user not like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<String> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<String> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(String value1, String value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(String value1, String value2) {
            addCriterion("create_user not between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserIsNull() {
            addCriterion("collection_user is null");
            return (Criteria) this;
        }

        public Criteria andCollectionUserIsNotNull() {
            addCriterion("collection_user is not null");
            return (Criteria) this;
        }

        public Criteria andCollectionUserEqualTo(String value) {
            addCriterion("collection_user =", value, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserNotEqualTo(String value) {
            addCriterion("collection_user <>", value, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserGreaterThan(String value) {
            addCriterion("collection_user >", value, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserGreaterThanOrEqualTo(String value) {
            addCriterion("collection_user >=", value, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserLessThan(String value) {
            addCriterion("collection_user <", value, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserLessThanOrEqualTo(String value) {
            addCriterion("collection_user <=", value, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserLike(String value) {
            addCriterion("collection_user like", value, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserNotLike(String value) {
            addCriterion("collection_user not like", value, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserIn(List<String> values) {
            addCriterion("collection_user in", values, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserNotIn(List<String> values) {
            addCriterion("collection_user not in", values, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserBetween(String value1, String value2) {
            addCriterion("collection_user between", value1, value2, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andCollectionUserNotBetween(String value1, String value2) {
            addCriterion("collection_user not between", value1, value2, "collectionUser");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeIsNull() {
            addCriterion("deductions_type is null");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeIsNotNull() {
            addCriterion("deductions_type is not null");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeEqualTo(Integer value) {
            addCriterion("deductions_type =", value, "deductionsType");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeNotEqualTo(Integer value) {
            addCriterion("deductions_type <>", value, "deductionsType");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeGreaterThan(Integer value) {
            addCriterion("deductions_type >", value, "deductionsType");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("deductions_type >=", value, "deductionsType");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeLessThan(Integer value) {
            addCriterion("deductions_type <", value, "deductionsType");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeLessThanOrEqualTo(Integer value) {
            addCriterion("deductions_type <=", value, "deductionsType");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeIn(List<Integer> values) {
            addCriterion("deductions_type in", values, "deductionsType");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeNotIn(List<Integer> values) {
            addCriterion("deductions_type not in", values, "deductionsType");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeBetween(Integer value1, Integer value2) {
            addCriterion("deductions_type between", value1, value2, "deductionsType");
            return (Criteria) this;
        }

        public Criteria andDeductionsTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("deductions_type not between", value1, value2, "deductionsType");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Integer value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Integer value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Integer value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Integer value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Integer value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Integer> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Integer> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Integer value1, Integer value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andSidIsNull() {
            addCriterion("sid is null");
            return (Criteria) this;
        }

        public Criteria andSidIsNotNull() {
            addCriterion("sid is not null");
            return (Criteria) this;
        }

        public Criteria andSidEqualTo(String value) {
            addCriterion("sid =", value, "sid");
            return (Criteria) this;
        }

        public Criteria andSidNotEqualTo(String value) {
            addCriterion("sid <>", value, "sid");
            return (Criteria) this;
        }

        public Criteria andSidGreaterThan(String value) {
            addCriterion("sid >", value, "sid");
            return (Criteria) this;
        }

        public Criteria andSidGreaterThanOrEqualTo(String value) {
            addCriterion("sid >=", value, "sid");
            return (Criteria) this;
        }

        public Criteria andSidLessThan(String value) {
            addCriterion("sid <", value, "sid");
            return (Criteria) this;
        }

        public Criteria andSidLessThanOrEqualTo(String value) {
            addCriterion("sid <=", value, "sid");
            return (Criteria) this;
        }

        public Criteria andSidLike(String value) {
            addCriterion("sid like", value, "sid");
            return (Criteria) this;
        }

        public Criteria andSidNotLike(String value) {
            addCriterion("sid not like", value, "sid");
            return (Criteria) this;
        }

        public Criteria andSidIn(List<String> values) {
            addCriterion("sid in", values, "sid");
            return (Criteria) this;
        }

        public Criteria andSidNotIn(List<String> values) {
            addCriterion("sid not in", values, "sid");
            return (Criteria) this;
        }

        public Criteria andSidBetween(String value1, String value2) {
            addCriterion("sid between", value1, value2, "sid");
            return (Criteria) this;
        }

        public Criteria andSidNotBetween(String value1, String value2) {
            addCriterion("sid not between", value1, value2, "sid");
            return (Criteria) this;
        }

        public Criteria andPayChannelIsNull() {
            addCriterion("pay_channel is null");
            return (Criteria) this;
        }

        public Criteria andPayChannelIsNotNull() {
            addCriterion("pay_channel is not null");
            return (Criteria) this;
        }

        public Criteria andPayChannelEqualTo(Integer value) {
            addCriterion("pay_channel =", value, "payChannel");
            return (Criteria) this;
        }

        public Criteria andPayChannelNotEqualTo(Integer value) {
            addCriterion("pay_channel <>", value, "payChannel");
            return (Criteria) this;
        }

        public Criteria andPayChannelGreaterThan(Integer value) {
            addCriterion("pay_channel >", value, "payChannel");
            return (Criteria) this;
        }

        public Criteria andPayChannelGreaterThanOrEqualTo(Integer value) {
            addCriterion("pay_channel >=", value, "payChannel");
            return (Criteria) this;
        }

        public Criteria andPayChannelLessThan(Integer value) {
            addCriterion("pay_channel <", value, "payChannel");
            return (Criteria) this;
        }

        public Criteria andPayChannelLessThanOrEqualTo(Integer value) {
            addCriterion("pay_channel <=", value, "payChannel");
            return (Criteria) this;
        }

        public Criteria andPayChannelIn(List<Integer> values) {
            addCriterion("pay_channel in", values, "payChannel");
            return (Criteria) this;
        }

        public Criteria andPayChannelNotIn(List<Integer> values) {
            addCriterion("pay_channel not in", values, "payChannel");
            return (Criteria) this;
        }

        public Criteria andPayChannelBetween(Integer value1, Integer value2) {
            addCriterion("pay_channel between", value1, value2, "payChannel");
            return (Criteria) this;
        }

        public Criteria andPayChannelNotBetween(Integer value1, Integer value2) {
            addCriterion("pay_channel not between", value1, value2, "payChannel");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleIsNull() {
            addCriterion("trigger_style is null");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleIsNotNull() {
            addCriterion("trigger_style is not null");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleEqualTo(Integer value) {
            addCriterion("trigger_style =", value, "triggerStyle");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleNotEqualTo(Integer value) {
            addCriterion("trigger_style <>", value, "triggerStyle");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleGreaterThan(Integer value) {
            addCriterion("trigger_style >", value, "triggerStyle");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleGreaterThanOrEqualTo(Integer value) {
            addCriterion("trigger_style >=", value, "triggerStyle");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleLessThan(Integer value) {
            addCriterion("trigger_style <", value, "triggerStyle");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleLessThanOrEqualTo(Integer value) {
            addCriterion("trigger_style <=", value, "triggerStyle");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleIn(List<Integer> values) {
            addCriterion("trigger_style in", values, "triggerStyle");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleNotIn(List<Integer> values) {
            addCriterion("trigger_style not in", values, "triggerStyle");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleBetween(Integer value1, Integer value2) {
            addCriterion("trigger_style between", value1, value2, "triggerStyle");
            return (Criteria) this;
        }

        public Criteria andTriggerStyleNotBetween(Integer value1, Integer value2) {
            addCriterion("trigger_style not between", value1, value2, "triggerStyle");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}