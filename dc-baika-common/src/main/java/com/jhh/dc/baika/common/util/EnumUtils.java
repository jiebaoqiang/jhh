package com.jhh.dc.baika.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jhh.dc.baika.common.constant.Constants;

import java.util.List;
import java.util.Map;

/**
 * 枚举处理工具类
 * 更多工具处理请看
 *
 * @see
 */
public class EnumUtils {

    private static final Map<Integer, Constants.RiskBpmNodeStatus> riskBpmNodeStatusMap = Maps.newConcurrentMap();

    private static final Map<Integer, Constants.RiskBpmNode> bpmModeMap = Maps.newConcurrentMap();

    private static final List<Integer> riskBpmNodeIds = Lists.newArrayList();

    static {
        initRiskBpmNodeStatusMap();
        initBpmNodeStatusMap();

    }

    /**
     * 初始化风控枚举类型
     */
    private static void initRiskBpmNodeStatusMap() {

        for (Constants.RiskBpmNodeStatus riskBpmNodeStatus : Constants.RiskBpmNodeStatus.values()) {
            riskBpmNodeStatusMap.put(riskBpmNodeStatus.riskCode, riskBpmNodeStatus);
        }

    }

    /**
     * 初始化认证节点
     */
    private static void initBpmNodeStatusMap() {

        for (Constants.RiskBpmNode bpmNode : Constants.RiskBpmNode.values()) {
            bpmModeMap.put(bpmNode.id, bpmNode);
            riskBpmNodeIds.add(bpmNode.id);
        }
    }


    public static Constants.RiskBpmNodeStatus getRiskBpmNodeStatus(Integer key) {
        return riskBpmNodeStatusMap.get(key);
    }

    public static Constants.RiskBpmNode getBpmNode(Integer key) {
        return bpmModeMap.get(key);
    }

    /**
     * risk bpm node ids
     */
    public static List<Integer> getRiskBpmNodeIds(){
        return riskBpmNodeIds;
    }
}
