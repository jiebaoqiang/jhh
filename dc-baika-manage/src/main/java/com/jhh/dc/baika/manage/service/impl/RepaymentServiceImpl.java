package com.jhh.dc.baika.manage.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.entity.loan.SystemUser;
import com.jhh.dc.baika.entity.manager_vo.LoanManagementVo;
import com.jhh.dc.baika.manage.forkjoin.ForkJoinTask;
import com.jhh.dc.baika.manage.mapper.SystemUserMapper;
import com.jhh.dc.baika.manage.mapper.RepaymentMapper;
import com.jhh.dc.baika.manage.service.loan.RepaymentService;
import com.jhh.dc.baika.manage.utils.Detect;
import com.jhh.dc.baika.common.util.DateUtil;
import com.jinhuhang.risk.dto.blacklist.jsonbean.BlackListDto;
import com.jinhuhang.risk.service.impl.blacklist.BlacklistAPIClient;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@Service
@Log4j
public class RepaymentServiceImpl implements RepaymentService {

    @Autowired
    RepaymentMapper repaymentMapper;

    @Autowired
    private SystemUserMapper collectorsMapper;

    @Autowired
    private BlacklistAPIClient riskClient;
    private static final String RISK_SUCCESS = "1";

    @Override
    public List getRepaymentPlan(Map<String, String[]> parameterMap) {
        Map<String, Object> param = QueryParamUtils.getParams(parameterMap);
        List<Map> ramaymentPlanList = repaymentMapper.getRepaymentPlan(param);
        //匹配催收人
        List<SystemUser> collectorList = collectorsMapper.selectAll();
        Map<String, String> collectorMap = new HashMap<>();
        for (SystemUser collector : collectorList) {
            collectorMap.put(collector.getUserSysno(), collector.getUserName());
        }

        for (Map map  : ramaymentPlanList) {
            map.put("collectionName",collectorMap.get(map.get("collectionUser")));
        }

        ramaymentPlanList.stream().forEach(map -> {
            if("1".equals(map.get("productTypeCode").toString()) && "BS004".equals(map.get("borrStatus").toString())){
                map.put("borrStatus","BS004K");
            }
            if("2".equals(map.get("productTypeCode").toString()) && "BS004".equals(map.get("borrStatus").toString() )){
                map.put("borrStatus","BS004Q");
            }
            if("1".equals(map.get("productTypeCode").toString()) && "BS005".equals(map.get("borrStatus").toString() )){
                map.put("borrStatus","BS005K");
            }
            if("2".equals(map.get("productTypeCode").toString()) && "BS005".equals(map.get("borrStatus").toString() )){
                map.put("borrStatus","BS005K");
            }
        });

        return handleBlackList(ramaymentPlanList);
    }

    @Override
    public PageInfo getRepaymentOrder(HttpServletRequest request) {
        return getRepaymentOrder(request, 0);
    }

    @Override
    public PageInfo getRepaymentOrder(HttpServletRequest request, int size) {
        Map<String, Object> param = QueryParamUtils.getParams(request.getParameterMap());

        String userNo = StringUtils.isEmpty(request.getParameter("userNo")) ? "SYSTEM" : request.getParameter("userNo");
        if (StringUtils.isNotEmpty(userNo)) {
            SystemUser c = new SystemUser();
            c.setUserSysno(userNo);
            SystemUser collectors = collectorsMapper.selectOne(c);
            param.put("levelType", collectors == null ? "" : collectors.getLevelType());
            param.put("companyId", collectors == null ? "" : collectors.getUserGroupId());
        }

        String typeWithChannel = (String) param.get("typeWithChannelEq");
        if (Detect.notEmpty(typeWithChannel)) {
            String[] splits = typeWithChannel.split("/", -1);
            param.put("type", splits[0]);
            if (splits.length > 1 && splits[1] != null) {
                param.put("channel", splits[1].trim());
            } else if (Arrays.asList("15", "16", "17", "18").contains(splits[0])) {
                param.put("filterNull", true);
            }
        }
        //不需要分页查询
        QueryParamUtils.buildPage(request, size, false);
        List list = repaymentMapper.getRepaymentOrder(param);
        //黑名单访问
        handleBlackList(list);
        //分页设置总条数
        PageInfo vos = new PageInfo(list);
        long totalCount = repaymentMapper.getRepaymentOrderCount(param);
        vos.setTotal(totalCount);

        return vos;

    }

    @Override
    public List selectCollectorsList(Map<String, String[]> parameterMap, String userId) {
        Map<String, Object> param = QueryParamUtils.getParams(parameterMap);
        if (Detect.notEmpty(param.get("repayDate") + "")) {
            param.put("repayDate_start", DateUtil.stampToDate((String) param.get("repayDate_start")));
            param.put("repayDate_end", DateUtil.stampToDate((String) param.get("repayDate_end")));
        }
        param.put("userId", userId);
        List list = repaymentMapper.selectCollectorsList(param);
        return handleBlackList(list);
    }

    /**
     * 批量查询黑名单列表数据
     *
     * @param list
     */
    private List handleBlackList(List<Map> list) {
        // 2018-4-13 风控黑名单新接口，批量处理
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }

        List<BlackListDto> blackListDtos = Lists.newArrayList();

        list.forEach(map -> {
            BlackListDto dto = new BlackListDto();
            // 此处不能合并，防止ClassCastException
            if (!ObjectUtils.isEmpty(map.get("idCard"))) {
                dto.setIdcard(map.get("idCard").toString());
            }

            if (!ObjectUtils.isEmpty(map.get("customerMobile"))) {
                dto.setPhone(map.get("customerMobile").toString());
            }
            if (StringUtils.isNotBlank(dto.getPhone()) || StringUtils.isNotBlank(dto.getIdcard())) {
                blackListDtos.add(dto);
            }
        });

        if (CollectionUtils.isEmpty(blackListDtos)) {
            return list;
        }

        // 用ForkJoinTask 批量查询黑名单，每个任务大小为20000
        List blackList = new ForkJoinPool(2).invoke(new ForkJoinTask(blackListDtos));

        return getList(list, blackList);
    }

    private static List<Map> getList(List<Map> list, List blackList) {
        list.forEach(map -> {
            if (blackList.contains(map.get("idCard")) || blackList.contains(map.get("customerMobile"))) {
                map.put("blackList", "Y");
            } else {
                map.put("blackList", "N");
            }
        });
        return list;
    }

    @Override
    public String getSurplusTotalAmountNum(String contractId) {
        return repaymentMapper.getSurplusTotalAmountNum(contractId);
    }

}
