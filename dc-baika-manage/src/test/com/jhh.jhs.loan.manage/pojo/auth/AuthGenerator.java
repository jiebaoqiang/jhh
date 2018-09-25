package com.jhh.dc.baika.manage.pojo.auth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.EnumSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.junit.Test;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by chenchao on 2017/10/13.
 */
public class AuthGenerator {

    public static void main(String[] args) {
        new AuthGenerator();
    }

    public AuthGenerator() {
        List<Directory> dirs = new ArrayList<Directory>();
        Directory checkDir = createCheckDir();
        Directory personDir = createPersonDir();
        Directory youmiDir = createYoumiDir();
        Directory operationDir = createOperationDir();
        Directory loanDir = createLoanDir();
        Directory settingDir = createSettingDir();
        Directory channelDir = createChannelDir();
        Directory refundDir = createRefundDir();

        dirs.add(checkDir);
        dirs.add(personDir);
        dirs.add(youmiDir);
        dirs.add(operationDir);
        dirs.add(loanDir);
        dirs.add(settingDir);
        dirs.add(channelDir);
        dirs.add(refundDir);

        createRoleButtonACL(dirs);

        SerializeWriter out = new SerializeWriter();
        SerializeConfig sc = new SerializeConfig();
        sc.put(Role.class, new EnumSerializer() {

            public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
                serializer.write(object.toString());
            }
        });

        JSONSerializer serializer = new JSONSerializer(out, sc);
        serializer.write(dirs);
        System.out.println(out.toString());
    }

    private void createRoleButtonACL(List<Directory> dirs) {
        setRoleButtonACLWithExclude(dirs, Role.OUTSOURCE_MANAGER, "贷后管理", "还款流水", "导出");
        setRoleButtonACL(dirs, Role.OUTSOURCE_MANAGER, "贷后管理", "贷后管理", "查看", "提交扣款", "提交扣款(合利宝)", "转件", "导出催收备注", "刷新");

        setRoleButtonACLWithExclude(dirs, Role.OUTSOURCE_NORMAL, "贷后管理", "还款流水", "导出");
        setRoleButtonACL(dirs, Role.OUTSOURCE_NORMAL, "贷后管理", "催收队列");

        setRoleButtonACL(dirs, Role.POST_LOAN_MANAGER, "贷后管理");
        setRoleButtonACL(dirs, Role.POST_LOAN_MANAGER, "系统设置", "系统用户");
        setRoleButtonACL(dirs, Role.POST_LOAN_MANAGER, "系统设置", "产品配置");
        setRoleButtonACL(dirs, Role.POST_LOAN_MANAGER, "平台管理", "问题管理");
        setRoleButtonACL(dirs, Role.POST_LOAN_MANAGER, "平台管理", "意见反馈");
        setRoleButtonACL(dirs, Role.POST_LOAN_MANAGER, "系统设置", "产品配置");

        setRoleButtonACLWithExclude(dirs, Role.POST_LOAN_NORMAL, "贷后管理", "还款流水", "导出");
        setRoleButtonACL(dirs, Role.POST_LOAN_NORMAL, "贷后管理", "催收队列");


        setRoleButtonACL(dirs, Role.POST_LOAN_FINANCE, "贷后管理", "还款流水", "查看", "导出", "刷新");
        setRoleButtonACL(dirs, Role.POST_LOAN_FINANCE, "贷后管理", "贷后管理", "查看", "线下还款", "刷新", "导出");
        setRoleButtonACL(dirs, Role.POST_LOAN_FINANCE, "运营管理", "规则列表", "刷新");
        setRoleButtonACL(dirs, Role.POST_LOAN_FINANCE, "运营管理", "佣金审核结果列表");


        setRoleButtonACLWithExclude(dirs, Role.POST_LOAN_SENIOR, "贷后管理", "还款流水", "导出");
        setRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "贷后管理", "还款计划");
        setRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "贷后管理", "贷后管理", "查看", "提交扣款", "提交扣款(合利宝)", "拉黑", "洗白", "刷新");
        setRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "贷后管理", "催收队列");
        setRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "贷后管理", "催收信息");
        setRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "贷后管理", "批量扣款列表");
        removeRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "贷后管理", "批量扣款专用");
        removeRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "贷后管理", "批量扣款列表");


        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "审核管理");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "用户管理");
        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "用户管理", "用户列表", "详情(运营)");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "平台管理", "意见反馈");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "贷后管理");
        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "贷后管理", "批量扣款专用");
        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "贷后管理", "批量扣款列表");
        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "贷后管理", "贷后管理", "线下还款");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "系统设置", "系统用户");

        //setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "系统设置", "自动放款");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "审核管理");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "用户管理");
        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "用户管理", "用户列表", "详情(运营)");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "平台管理", "意见反馈");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "贷后管理");
        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "贷后管理", "批量扣款专用");
        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "贷后管理", "批量扣款列表");
        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "贷后管理", "贷后管理", "线下还款", "批量扣款");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "系统设置", "系统用户");

        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DATA, "审核管理", "待审批");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DATA, "审核管理", "人工审核", "详情", "拉黑", "转件");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DATA, "审核管理", "人工审核结果");
        setRoleButtonACLWithExclude(dirs, Role.RISK_MANAGEMENT_DATA, "审核管理", "审核管理", "放款", "放款(合利宝)", "拒绝");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DATA, "用户管理");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DATA, "平台管理", "意见反馈");
        setRoleButtonACLWithExclude(dirs, Role.RISK_MANAGEMENT_DATA, "贷后管理", "还款流水", "导出");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DATA, "贷后管理", "贷后管理", "查看", "转件", "拉黑", "洗白", "刷新");

        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "审核管理", "待审批");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "审核管理", "人工审核");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "审核管理", "人工审核结果", "详情");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "用户管理");
        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "用户管理", "用户列表", "详情(运营)","添加用户");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "平台管理", "意见反馈");
        setRoleButtonACLWithExclude(dirs, Role.RISK_MANAGEMENT_NORMAL, "贷后管理", "还款流水", "导出");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "贷后管理", "催收队列");
        setRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "贷后管理", "贷后管理", "查看", "拉黑", "洗白", "刷新");

        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "审核管理", "审核管理", "取消借款", "刷新");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "用户管理");
        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "用户管理", "用户列表", "详情","添加用户");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "平台管理", "意见反馈");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "平台管理", "问题管理");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "贷后管理");
        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "贷后管理", "批量扣款专用");
        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "贷后管理", "批量扣款列表");
        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "贷后管理", "贷后管理", "批量扣款");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "系统设置", "系统用户");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "运营管理", "邀请好友");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "财务管理","退款列表","详情","确认");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "财务管理","退款流水");

        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_NORMAL, "审核管理", "审核管理", "取消借款", "刷新");
        setRoleButtonACLWithExclude(dirs, Role.CUSTOM_SERVICE_NORMAL, "用户管理", "用户列表", "详情", "洗白","添加用户");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_NORMAL, "平台管理", "意见反馈");
        setRoleButtonACLWithExclude(dirs, Role.CUSTOM_SERVICE_NORMAL, "贷后管理", "还款流水", "导出");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_NORMAL, "贷后管理", "贷后管理", "查看");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_NORMAL, "贷后管理", "催收队列");
        setRoleButtonACL(dirs, Role.CUSTOM_SERVICE_NORMAL, "运营管理", "邀请好友");

        setRoleButtonACL(dirs, Role.OPERATION_MANAGER, "用户管理");
        removeRoleButtonACL(dirs, Role.OPERATION_MANAGER, "用户管理", "用户列表", "详情" ,"添加用户");
        setRoleButtonACL(dirs, Role.OPERATION_MANAGER, "平台管理", "意见反馈");
        setRoleButtonACL(dirs, Role.OPERATION_MANAGER, "平台管理", "问题管理");
        setRoleButtonACL(dirs, Role.OPERATION_MANAGER, "运营管理");
        setRoleButtonACL(dirs, Role.OPERATION_MANAGER, "系统设置", "数据字典", "修改", "查看详情");
        setRoleButtonACL(dirs, Role.OPERATION_MANAGER, "系统设置", "系统用户");

        setRoleButtonACL(dirs, Role.OPERATION_NORMAL, "用户管理");
        removeRoleButtonACL(dirs, Role.OPERATION_NORMAL, "用户管理", "用户列表", "详情","添加用户");
        setRoleButtonACL(dirs, Role.OPERATION_NORMAL, "平台管理", "意见反馈");
        setRoleButtonACL(dirs, Role.OPERATION_NORMAL, "平台管理", "问题管理");

        setRoleButtonACL(dirs, Role.OPERATION_NORMAL, "运营管理", "规则列表");
        setRoleButtonACL(dirs, Role.OPERATION_NORMAL, "运营管理", "邀请好友");
        setRoleButtonACL(dirs, Role.OPERATION_NORMAL, "运营管理", "佣金审核结果列表");
        setRoleButtonACL(dirs, Role.OPERATION_NORMAL, "运营管理", "佣金审核列表");
        //setRoleButtonACL(dirs, Role.OPERATION_NORMAL, "运营管理", "渠道扣量管理");
        setRoleButtonACL(dirs, Role.DEVELOPER_MANAGER);
        //removeRoleButtonACL(dirs, Role.DEVELOPER_MANAGER, "系统设置", "自动放款");

        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "审核管理", "待审批", "详情");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "审核管理", "人工审核", "详情");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "审核管理", "人工审核结果", "详情");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "审核管理", "审核管理", "详情", "刷新");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "审核管理", "审核员管理");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "审核管理", "人工审核报表");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "审核管理", "审核放款报表");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "用户管理");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "平台管理");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "运营管理");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "运营管理", "规则列表");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "运营管理", "邀请好友");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "运营管理", "佣金审核结果列表");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "运营管理", "佣金审核列表");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "运营管理", "渠道扣量管理");
        setRoleButtonACLWithExclude(dirs, Role.DEVELOPER_NORMAL, "贷后管理", "还款流水", "拉黑");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "贷后管理", "还款计划");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "贷后管理", "贷后管理", "查看", "刷新");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "贷后管理", "批量扣款专用", "查看", "刷新");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "贷后管理", "催收队列", "查看", "拉黑", "刷新");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "贷后管理", "催收信息");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "贷后管理", "催收工作报表");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "贷后管理", "批量扣款列表");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "系统设置");
        setRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "财务管理");

        //removeRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "系统设置", "自动放款");

        setRoleButtonACL(dirs, Role.SYSTEM_ADMIN);



        setRoleButtonACL(dirs, Role.POST_LOAN_FINANCE, "财务管理","退款列表","详情","退款","驳回");
        setRoleButtonACL(dirs, Role.POST_LOAN_FINANCE, "财务管理","退款流水");
//        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_NORMAL, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.DEVELOPER_MANAGER, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OPERATION_NORMAL, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OPERATION_MANAGER, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OUTSOURCE_MANAGER, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OUTSOURCE_NORMAL, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_FINANCE, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_MANAGER, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_NORMAL, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DATA, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "贷后管理", "贷后管理", "提交扣款(合利宝)");
//
//        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_NORMAL, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.DEVELOPER_MANAGER, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OPERATION_NORMAL, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OPERATION_MANAGER, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OUTSOURCE_MANAGER, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OUTSOURCE_NORMAL, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_FINANCE, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_MANAGER, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_NORMAL, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DATA, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "贷后管理", "催收队列", "提交扣款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "贷后管理", "催收队列", "提交扣款(合利宝)");
//
//        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_NORMAL, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.DEVELOPER_MANAGER, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OPERATION_NORMAL, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OPERATION_MANAGER, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OUTSOURCE_MANAGER, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OUTSOURCE_NORMAL, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_FINANCE, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_MANAGER, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_NORMAL, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DATA, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "审核管理", "审核管理", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "审核管理", "审核管理", "放款(合利宝)");
//
//        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_MANAGER, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.CUSTOM_SERVICE_NORMAL, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.DEVELOPER_MANAGER, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.DEVELOPER_NORMAL, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OPERATION_NORMAL, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OPERATION_MANAGER, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OUTSOURCE_MANAGER, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.OUTSOURCE_NORMAL, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_FINANCE, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_MANAGER, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_NORMAL, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.POST_LOAN_SENIOR, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DATA, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_DIRECTOR, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_MANAGER, "审核管理", "待审批", "放款(合利宝)");
//        removeRoleButtonACL(dirs, Role.RISK_MANAGEMENT_NORMAL, "审核管理", "待审批", "放款(合利宝)");
    }

    private void removeRoleButtonACL(List<Directory> dirs, final Role role, String... config) {
        Assert.isTrue(config.length > 0, "config length should be greater than 0.");
        Directory dir = getDirectory(dirs, config[0]);
        Assert.notNull(dir, "Directory " + config[0] + " should not be null.");

        List<String> configList = Arrays.asList(config);
        if (configList.size() == 1) {
            dir.getPages().stream().forEach(t -> t.getRoles().remove(role));
            dir.getRoles().remove(role);
            return;
        }

        Page page = getPage(dir, config[1]);
        Assert.notNull(page, "Page " + config[1] + " should not be null.");
        if (configList.size() > 2) {
            final List<String> includes = configList.subList(2, configList.size());
            page.getModules().stream().filter(t -> t instanceof Button).filter(t -> includes.contains(t.getDesc())).forEach(t -> Assert.isTrue(((Button) t).removeRole(role), "Role " + role.getDesc() + " should be removed from Button " + t.getDesc()));
        } else if (configList.size() > 1) {
            page.getRoles().remove(role);
        }
    }

    private void setRoleButtonACLWithExclude(List<Directory> dirs, Role role, String... config) {
        Assert.isTrue(config.length > 2, "config length should be greater than 2.");
        Directory dir = getDirectory(dirs, config[0]);
        Assert.notNull(dir, "Directory " + config[0] + " should not be null.");
        Page page = getPage(dir, config[1]);
        Assert.notNull(page, "Page " + config[1] + " should not be null.");

        List<String> configList = Arrays.asList(config);
        final List<String> excludes = configList.subList(2, configList.size());

        page.getModules().stream().filter(t -> t instanceof Button).filter(t -> !excludes.contains(t.getDesc())).forEach(t -> ((Button) t).addRole(role));

        dir.addRole(role);
        page.addRole(role);
    }

    private void setRoleButtonACL(List<Directory> dirs, Role role, String... config) {
        if (config == null || config.length == 0) {
            for (int i = 0; i < dirs.size(); i++) {
                Directory dir = dirs.get(i);
                setRoleButtonACL(dirs, role, dir.getDesc());
            }
        } else if (config.length == 1) {
            Directory dir = getDirectory(dirs, config[0]);
            Assert.notNull(dir, config[0] + " should not be null.");
            dir.getPages().stream().forEach(t -> setRoleButtonACL(dirs, role, dir.getDesc(), t.getDesc()));
            dir.addRole(role);
        } else if (config.length == 2) {
            Directory dir = getDirectory(dirs, config[0]);
            Assert.notNull(dir, "Directory " + config[0] + " should not be null.");
            Page page = getPage(dir, config[1]);
            Assert.notNull(page, "Page " + config[1] + " should not be null.");
            page.getModules().stream().filter(t -> t instanceof Button).forEach(t -> setRoleButtonACL(dirs, role, dir.getDesc(), page.getDesc(), t.getDesc()));
            page.addRole(role);
            dir.addRole(role);
        } else if (config.length > 2) {
            Directory dir = getDirectory(dirs, config[0]);
            Assert.notNull(dir, "Directory " + config[0] + " should not be null.");
            Page page = getPage(dir, config[1]);
            Assert.notNull(page, "Page " + config[1] + " should not be null.");
            for (int i = 2; i < config.length; i++) {
                Button button = getButton(page, config[i]);
                Assert.notNull(page, "Button " + config[1] + " should not be null.");
                button.addRole(role);
            }
            page.addRole(role);
            dir.addRole(role);
        }
    }

    private Button getButton(Page page, String name) {
        Optional<Module> module = page.getModules().stream().filter(t -> t instanceof Button).filter(t -> t.getDesc().equals(name)).findFirst();
        Assert.isTrue(module.isPresent(), "Page " + page.getDesc() + " should have the buttuon " + name);
        return (Button) module.get();
    }

    private Page getPage(Directory dir, String name) {
        Optional<Page> page = dir.getPages().stream().filter(t -> t.getDesc().equals(name)).findFirst();
        Assert.isTrue(page.isPresent(), "Directory " + dir.getDesc() + " should have the page " + name);
        return page.get();
    }

    private Directory getDirectory(List<Directory> dirs, String name) {
        Optional<Directory> dir = dirs.stream().filter(t -> t.getDesc().equals(name)).findFirst();
        return dir.get();
    }


    private Directory createSettingDir() {
        Directory settingDir = new Directory();
        settingDir.setId("ym-g");
        settingDir.setDesc("系统设置");


        Page lookupTypeManagePage = createLookupTypeManagePage();
        Page userTablesPage = createUserTablesPage();
        Page companyTablesPage = createCompanyTablesPage();
        Page productPage = createProductPage();

        settingDir.addPage(lookupTypeManagePage);
        settingDir.addPage(userTablesPage);
        settingDir.addPage(companyTablesPage);
        settingDir.addPage(productPage);

        return settingDir;
    }

    private Directory createLoanDir() {
        Directory loanDir = new Directory();
        loanDir.setId("ym-d");
        loanDir.setDesc("贷后管理");


        Page repaymentPage = createRepaymentPage();
        Page repaymentPlanPage = createRepaymentPlanPage();
        Page paymentInfoPage = createPaymentInfoPage();
        Page loanManagementPage = createLoanManagementPage();
        Page batchReducePage = createBatchReducePage();
        Page collectionListPage = createCollectionListPage();
        Page collectionInfoPage = createCollectionInfoPage();
        Page paymentReportFormsPage = createPaymentReportFormsPage();
        Page batchCollectionListPage = createBatchCollectionListPage();

        loanDir.addPage(repaymentPage);
        loanDir.addPage(repaymentPlanPage);
        loanDir.addPage(paymentInfoPage);
        loanDir.addPage(loanManagementPage);
        loanDir.addPage(batchReducePage);
        loanDir.addPage(collectionListPage);
        loanDir.addPage(collectionInfoPage);
        loanDir.addPage(paymentReportFormsPage);
        loanDir.addPage(batchCollectionListPage);

        return loanDir;
    }


    private Directory createOperationDir() {
        Directory operationDir = new Directory();
        operationDir.setId("ym-v");
        operationDir.setDesc("运营管理");

        Page couponPage = createCouponPage();
        Page drainagePage = createDrainagePage();
        Page commissionRulePage = createCommissionRulePage();
        Page commissionReviewPage = createCommissionReviewPage();
        Page inviteFriendsPage = createInviteFriendsPage();
        Page registerSourceManage = createRegisterSourceManage();
        Page commissionReviewBeforePage = createCommissionReviewBeforePage();

        operationDir.addPage(couponPage);
        operationDir.addPage(drainagePage);
        operationDir.addPage(commissionRulePage);
        operationDir.addPage(commissionReviewPage);
        operationDir.addPage(inviteFriendsPage);
        operationDir.addPage(commissionReviewBeforePage);
        operationDir.addPage(registerSourceManage);
        return operationDir;
    }

    private Directory createYoumiDir() {
        Directory youmiDir = new Directory();
        youmiDir.setId("ym-y");
        youmiDir.setDesc("平台管理");


        Page feedbackPage = createFeedbackPage();
        Page questionPage = createQuestionPage();
        Page messageTempPage = createMessageTempPage();
        Page smsTemPage = createSmsTempPage();
        youmiDir.addPage(feedbackPage);
        youmiDir.addPage(questionPage);
        youmiDir.addPage(messageTempPage);
        youmiDir.addPage(smsTemPage);
        return youmiDir;
    }


    private Directory createPersonDir() {
        Directory personDir = new Directory();
        personDir.setId("ym-u");
        personDir.setDesc("用户管理");


        Page personListPage = createPersonListPage();

        personDir.addPage(personListPage);

        return personDir;
    }

    private Directory createChannelDir() {
        Directory channelPersonDir = new Directory();
        channelPersonDir.setId("ym-c");
        channelPersonDir.setDesc("渠道商管理");

        Page channelListPage = createChannelListPage();
        channelPersonDir.addPage(channelListPage);
        return channelPersonDir;
    }

    private Directory createRefundDir() {
        Directory createRefundDir = new Directory();
        createRefundDir.setId("ym-x");
        createRefundDir.setDesc("财务管理");

        Page refundReviewPage = createRefundReviewPage();
        Page refundRecordPage = createRefundRecordPage();
        createRefundDir.addPage(refundReviewPage);
        createRefundDir.addPage(refundRecordPage);
        return createRefundDir;
    }


    private Directory createCheckDir() {
        Directory checkDir = new Directory();
        checkDir.setId("ym-z");
        checkDir.setDesc("审核管理");


        Page checkByOnePage = createCheckByOnePage();
        Page checkByManualOnePage = createCheckByManualOnePage();
        Page checkByManualResultPage = createCheckByManualResultPage();
        Page checkByOnedPage = createCheckByOnedPage();
        Page checkByManagerPage = createCheckByManagerPage();
        Page managerByManagerPage = createManagerByManagerPage();
        Page manualAuditReportPage = createManualAuditReportPage();
        Page auditLoanPage = createAuditLoanPage();
        checkDir.addPage(checkByOnePage);
        checkDir.addPage(checkByManualOnePage);
        checkDir.addPage(checkByManualResultPage);
        checkDir.addPage(checkByOnedPage);
        checkDir.addPage(checkByManagerPage);
        checkDir.addPage(managerByManagerPage);
        checkDir.addPage(manualAuditReportPage);
        checkDir.addPage(auditLoanPage);

        return checkDir;
    }

    private Page createManagerByManagerPage() {
        Page managerByManagerPage = new Page();
        managerByManagerPage.setId("ym-zc");
        managerByManagerPage.setPageUrl("managerbymanager.html");
        managerByManagerPage.setDesc("审核员管理");


        Button okButton = new Button();
        okButton.setId("tool_ok");
        okButton.setIndex("0");
        okButton.setDesc("启用");
        managerByManagerPage.addModule(okButton);

        Button noButton = new Button();
        noButton.setId("tool_no");
        noButton.setIndex("1");
        noButton.setDesc("禁用");
        managerByManagerPage.addModule(noButton);

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("2");
        addButton.setDesc("添加");
        managerByManagerPage.addModule(addButton);

        Button delButton = new Button();
        delButton.setId("tool_del");
        delButton.setIndex("3");
        delButton.setDesc("删除");
        managerByManagerPage.addModule(delButton);

        return managerByManagerPage;
    }

    private Page createManualAuditReportPage() {
        Page manualAuditReportPage = new Page();
        manualAuditReportPage.setId("ym-ze");
        manualAuditReportPage.setPageUrl("manualAuditReport.html");
        manualAuditReportPage.setDesc("人工审核报表");


        Button okButton = new Button();
        okButton.setId("tool_find");
        okButton.setIndex("0");
        okButton.setDesc("查看");
        manualAuditReportPage.addModule(okButton);

        return manualAuditReportPage;
    }

    private Page createAuditLoanPage() {
        Page manualAuditReportPage = new Page();
        manualAuditReportPage.setId("ym-zf");
        manualAuditReportPage.setPageUrl("auditLoan.html");
        manualAuditReportPage.setDesc("审核放款报表");


        Button okButton = new Button();
        okButton.setId("tool_find");
        okButton.setIndex("0");
        okButton.setDesc("查看");
        manualAuditReportPage.addModule(okButton);

        return manualAuditReportPage;
    }

    private Page createCheckByManagerPage() {
        Page checkByManagerPage = new Page();
        checkByManagerPage.setId("ym-zb");
        checkByManagerPage.setPageUrl("checkbymanager.html");
        checkByManagerPage.setDesc("审核管理");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("详情");
        checkByManagerPage.addModule(findButton);

        Button okButton = new Button();
        okButton.setId("tool_ok");
        okButton.setIndex("1");
        okButton.setDesc("放款");
        checkByManagerPage.addModule(okButton);

        Button noButton = new Button();
        noButton.setId("tool_no");
        noButton.setIndex("2");
        noButton.setDesc("拒绝");
        checkByManagerPage.addModule(noButton);

        Button transferButton = new Button();
        transferButton.setId("tool_transfer");
        transferButton.setIndex("3");
        transferButton.setDesc("转件");
        checkByManagerPage.addModule(transferButton);

        Button cancelButton = new Button();
        cancelButton.setId("tool_cancel");
        cancelButton.setIndex("4");
        cancelButton.setDesc("取消借款");
        checkByManagerPage.addModule(cancelButton);

        Button reloadButton = new Button();
        reloadButton.setId("tool_reload");
        reloadButton.setIndex("5");
        reloadButton.setDesc("刷新");
        checkByManagerPage.addModule(reloadButton);

        Button okButtonHelibao = new Button();
        okButtonHelibao.setId("tool_ok_helibao");
        okButtonHelibao.setIndex("7");
        okButtonHelibao.setDesc("放款(合利宝)");
        checkByManagerPage.addModule(okButtonHelibao);

        return checkByManagerPage;


    }

    private Page createCheckByOnedPage() {
        Page checkByOnedPage = new Page();
        checkByOnedPage.setId("ym-zd");
        checkByOnedPage.setPageUrl("checkbyoned.html");
        checkByOnedPage.setDesc("我的审批");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("详情");
        checkByOnedPage.addModule(findButton);

        return checkByOnedPage;
    }

    private Page createCheckByManualResultPage() {
        Page checkByManualResult = new Page();
        checkByManualResult.setId("ym-zmr");
        checkByManualResult.setPageUrl("checkbymanual_result.html");
        checkByManualResult.setDesc("人工审核结果");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("详情");
        checkByManualResult.addModule(findButton);

        Button exportButton = new Button();
        exportButton.setId("tool_export");
        exportButton.setIndex("1");
        exportButton.setDesc("导出");
        checkByManualResult.addModule(exportButton);

        return checkByManualResult;
    }

    private Page createCheckByOnePage() {
        Page checkByOnePage = new Page();
        checkByOnePage.setId("ym-za");
        checkByOnePage.setPageUrl("checkbyone.html");
        checkByOnePage.setDesc("待审批");


        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("详情");
        checkByOnePage.addModule(findButton);

        Button okButton = new Button();
        okButton.setId("tool_ok");
        okButton.setIndex("1");
        okButton.setDesc("放款");
        checkByOnePage.addModule(okButton);

        Button noButton = new Button();
        noButton.setId("tool_no");
        noButton.setIndex("2");
        noButton.setDesc("拒绝");
        checkByOnePage.addModule(noButton);

        Button blackButton = new Button();
        blackButton.setId("tool_black");
        blackButton.setIndex("3");
        blackButton.setDesc("拒绝并拉黑");
        checkByOnePage.addModule(blackButton);

        Button helibaoButton = new Button();
        helibaoButton.setId("tool_ok_helibao");
        helibaoButton.setIndex("4");
        helibaoButton.setDesc("放款(合利宝)");
        checkByOnePage.addModule(helibaoButton);

        return checkByOnePage;
    }

    private Page createCheckByManualOnePage() {
        Page checkByManualOnePage = new Page();
        checkByManualOnePage.setId("ym-zma");
        checkByManualOnePage.setPageUrl("checkbymanualone.html");
        checkByManualOnePage.setDesc("人工审核");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("详情");
        checkByManualOnePage.addModule(findButton);

        Button okButton = new Button();
        okButton.setId("tool_ok");
        okButton.setIndex("1");
        okButton.setDesc("通过");
        checkByManualOnePage.addModule(okButton);

        Button noButton = new Button();
        noButton.setId("tool_no");
        noButton.setIndex("2");
        noButton.setDesc("拒绝");
        checkByManualOnePage.addModule(noButton);

        Button blackButton = new Button();
        blackButton.setId("tool_black");
        blackButton.setIndex("3");
        blackButton.setDesc("拉黑");
        checkByManualOnePage.addModule(blackButton);

        Button transferButton = new Button();
        transferButton.setId("tool_transfer");
        transferButton.setIndex("4");
        transferButton.setDesc("转件");
        checkByManualOnePage.addModule(transferButton);

        return checkByManualOnePage;
    }

    private Page createPersonListPage() {
        Page personListPage = new Page();
        personListPage.setId("ym-ua");
        personListPage.setPageUrl("perlist.html");
        personListPage.setDesc("用户列表");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("详情");
        personListPage.addModule(findButton);

        Button kfButton = new Button();
        kfButton.setId("tool_kf");
        kfButton.setIndex("1");
        kfButton.setDesc("详情(运营)");
        personListPage.addModule(kfButton);

        Button okButton = new Button();
        okButton.setId("tool_ok");
        okButton.setIndex("2");
        okButton.setDesc("拉黑");
        personListPage.addModule(okButton);

        Button noButton = new Button();
        noButton.setId("tool_no");
        noButton.setIndex("3");
        noButton.setDesc("洗白");
        personListPage.addModule(noButton);

        Button newButton = new Button();
        newButton.setId("tool_new");
        newButton.setIndex("4");
        newButton.setDesc("新流程用户");
        personListPage.addModule(newButton);

        Button oldButton = new Button();
        oldButton.setId("tool_old");
        oldButton.setIndex("5");
        oldButton.setDesc("老流程用户");
        personListPage.addModule(oldButton);

        Button exportButton = new Button();
        exportButton.setId("tool_export");
        exportButton.setIndex("6");
        exportButton.setDesc("导出");
        personListPage.addModule(exportButton);

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("7");
        addButton.setDesc("添加用户");
        personListPage.addModule(addButton);

        return personListPage;
    }

    private Page createChannelListPage() {
        Page personListPage = new Page();
        personListPage.setId("ym-ca");
        personListPage.setPageUrl("channelPerlist.html");
        personListPage.setDesc("渠道用户列表");
        return personListPage;
    }

    private Page createFeedbackPage() {
        Page feedbackPage = new Page();
        feedbackPage.setId("ym-ya");
        feedbackPage.setPageUrl("feedback.html");
        feedbackPage.setDesc("意见反馈");
        return feedbackPage;
    }

    private Page createQuestionPage() {
        Page questionPage = new Page();
        questionPage.setId("ym-yb");
        questionPage.setPageUrl("question.html");
        questionPage.setDesc("问题管理");

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("0");
        addButton.setDesc("新增");
        questionPage.addModule(addButton);

        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("1");
        editButton.setDesc("编辑");
        questionPage.addModule(editButton);

        return questionPage;
    }

    private Page createMessageTempPage() {
        Page messageTempPage = new Page();
        messageTempPage.setId("ym-yc");
        messageTempPage.setPageUrl("message_temp.html");
        messageTempPage.setDesc("消息模板");

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("0");
        addButton.setDesc("新增");
        messageTempPage.addModule(addButton);

        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("1");
        editButton.setDesc("编辑");
        messageTempPage.addModule(editButton);

        return messageTempPage;
    }

    private Page createSmsTempPage() {
        Page smsTempPage = new Page();
        smsTempPage.setId("ym-yd");
        smsTempPage.setPageUrl("sms_temp.html");
        smsTempPage.setDesc("短信模板");

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("0");
        addButton.setDesc("新增");
        smsTempPage.addModule(addButton);

        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("1");
        editButton.setDesc("编辑");
        smsTempPage.addModule(editButton);

        return smsTempPage;
    }

    private Page createCouponPage() {
        Page couponPage = new Page();
        couponPage.setId("ym-va");
        couponPage.setPageUrl("coupon.html");
        couponPage.setDesc("优惠券管理");

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("0");
        addButton.setDesc("新增");
        couponPage.addModule(addButton);

        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("1");
        editButton.setDesc("编辑");
        couponPage.addModule(editButton);

        return couponPage;
    }

    private Page createRepaymentPage() {
        Page repaymentPage = new Page();
        repaymentPage.setId("ym-da");
        repaymentPage.setPageUrl("repayment.html");
        repaymentPage.setDesc("还款流水");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        repaymentPage.addModule(findButton);

        Button exportButton = new Button();
        exportButton.setId("tool_export");
        exportButton.setIndex("1");
        exportButton.setDesc("导出");
        repaymentPage.addModule(exportButton);

        Button blackButton = new Button();
        blackButton.setId("tool_black");
        blackButton.setIndex("2");
        blackButton.setDesc("拉黑");
        repaymentPage.addModule(blackButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("3");
        refreshButton.setDesc("刷新");
        repaymentPage.addModule(refreshButton);

        return repaymentPage;
    }


    private Page createRepaymentPlanPage() {
        Page repaymentPlanPage = new Page();
        repaymentPlanPage.setId("ym-db");
        repaymentPlanPage.setPageUrl("repaymentPlan.html");
        repaymentPlanPage.setDesc("还款计划");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        repaymentPlanPage.addModule(findButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("1");
        refreshButton.setDesc("刷新");
        repaymentPlanPage.addModule(refreshButton);

        return repaymentPlanPage;
    }

    private Page createPaymentInfoPage() {
        Page paymentInfoPage = new Page();
        paymentInfoPage.setId("paymentInfo");
        paymentInfoPage.setPageUrl("paymentInfo.html");
        paymentInfoPage.setDesc("扣款信息");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        paymentInfoPage.addModule(findButton);

        return paymentInfoPage;
    }

    private Page createLoanManagementPage() {
        Page loanManagementPage = new Page();
        loanManagementPage.setId("ym-dc");
        loanManagementPage.setPageUrl("loanManagement.html");
        loanManagementPage.setDesc("贷后管理");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        loanManagementPage.addModule(findButton);

        Button reduceButton = new Button();
        reduceButton.setId("tool_reduce");
        reduceButton.setIndex("1");
        reduceButton.setDesc("申请减免");
        loanManagementPage.addModule(reduceButton);

        Button offlineButton = new Button();
        offlineButton.setId("tool_offline");
        offlineButton.setIndex("2");
        offlineButton.setDesc("线下还款");
        loanManagementPage.addModule(offlineButton);

        Button reduceMoneyButton = new Button();
        reduceMoneyButton.setId("tool_reduce_money");
        reduceMoneyButton.setIndex("3");
        reduceMoneyButton.setDesc("提交扣款");
        loanManagementPage.addModule(reduceMoneyButton);

        Button payHelipayButton = new Button();
        payHelipayButton.setId("tool_reduce_money_helipay");
        payHelipayButton.setIndex("11");
        payHelipayButton.setDesc("提交扣款(合利宝)");
        loanManagementPage.addModule(payHelipayButton);

        Button reduceBatchButton = new Button();
        reduceBatchButton.setId("tool_reduce_batch");
        reduceBatchButton.setIndex("4");
        reduceBatchButton.setDesc("批量扣款");
        loanManagementPage.addModule(reduceBatchButton);

        Button transferButton = new Button();
        transferButton.setId("tool_transfer");
        transferButton.setIndex("5");
        transferButton.setDesc("转件");
        loanManagementPage.addModule(transferButton);

        Button okButton = new Button();
        okButton.setId("tool_ok");
        okButton.setIndex("6");
        okButton.setDesc("拉黑");
        loanManagementPage.addModule(okButton);

        Button noButton = new Button();
        noButton.setId("tool_no");
        noButton.setIndex("7");
        noButton.setDesc("洗白");
        loanManagementPage.addModule(noButton);

        Button exportButton = new Button();
        exportButton.setId("tool_export");
        exportButton.setIndex("8");
        exportButton.setDesc("导出");
        loanManagementPage.addModule(exportButton);

        Button exportRemarkButton = new Button();
        exportRemarkButton.setId("tool_export_remark");
        exportRemarkButton.setIndex("9");
        exportRemarkButton.setDesc("导出催收备注");
        loanManagementPage.addModule(exportRemarkButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("10");
        refreshButton.setDesc("刷新");
        loanManagementPage.addModule(refreshButton);

        return loanManagementPage;
    }

    private Page createBatchReducePage() {
        Page batchReducePage = new Page();
        batchReducePage.setId("ym-dh");
        batchReducePage.setPageUrl("batchReduce.html");
        batchReducePage.setDesc("批量扣款专用");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        batchReducePage.addModule(findButton);

        Button reduceBatchButton = new Button();
        reduceBatchButton.setId("tool_reduce_batch");
        reduceBatchButton.setIndex("1");
        reduceBatchButton.setDesc("批量扣款");
        batchReducePage.addModule(reduceBatchButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("2");
        refreshButton.setDesc("刷新");
        batchReducePage.addModule(refreshButton);

        return batchReducePage;
    }

    private Page createCollectionListPage() {
        Page collectionListPage = new Page();
        collectionListPage.setId("ym-dd");
        collectionListPage.setPageUrl("collectionList.html");
        collectionListPage.setDesc("催收队列");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        collectionListPage.addModule(findButton);

        Button collectionRemarkButton = new Button();
        collectionRemarkButton.setId("tool_collection_remark");
        collectionRemarkButton.setIndex("1");
        collectionRemarkButton.setDesc("催收备注");
        collectionListPage.addModule(collectionRemarkButton);

        Button reduceButton = new Button();
        reduceButton.setId("tool_reduce");
        reduceButton.setIndex("2");
        reduceButton.setDesc("提交扣款");
        collectionListPage.addModule(reduceButton);

        Button okButton = new Button();
        okButton.setId("tool_ok");
        okButton.setIndex("3");
        okButton.setDesc("拉黑");
        collectionListPage.addModule(okButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("4");
        refreshButton.setDesc("刷新");
        collectionListPage.addModule(refreshButton);

        Button reduceHelibaoButton = new Button();
        reduceHelibaoButton.setId("tool_reduce_helibao");
        reduceHelibaoButton.setIndex("5");
        reduceHelibaoButton.setDesc("提交扣款(合利宝)");
        collectionListPage.addModule(reduceHelibaoButton);

        return collectionListPage;
    }

    private Page createCollectionInfoPage() {
        Page collectionInfoPage = new Page();
        collectionInfoPage.setId("ym-de");
        collectionInfoPage.setPageUrl("collectionInfo.html");
        collectionInfoPage.setDesc("催收信息");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        collectionInfoPage.addModule(findButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("1");
        refreshButton.setDesc("刷新");
        collectionInfoPage.addModule(refreshButton);

        return collectionInfoPage;
    }

    private Page createPaymentReportFormsPage() {
        Page repaymentReportFormsPage = new Page();
        repaymentReportFormsPage.setId("ym-df");
        repaymentReportFormsPage.setPageUrl("repaymentReportForms.html");
        repaymentReportFormsPage.setDesc("催收工作报表");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        repaymentReportFormsPage.addModule(findButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("1");
        refreshButton.setDesc("刷新");
        repaymentReportFormsPage.addModule(refreshButton);

//        Button exportButton = new Button();
//        exportButton.setId("tool_export");
//        exportButton.setIndex("2");
//        exportButton.setDesc("导出");
//        repaymentReportFormsPage.addModule(refreshButton);

        return repaymentReportFormsPage;
    }

    private Page createBatchCollectionListPage() {
        Page batchCollectionListPage = new Page();
        batchCollectionListPage.setId("ym-dg");
        batchCollectionListPage.setPageUrl("beatchConllectionList.html");
        batchCollectionListPage.setDesc("批量扣款列表");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        batchCollectionListPage.addModule(findButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("1");
        refreshButton.setDesc("刷新");
        batchCollectionListPage.addModule(refreshButton);

        return batchCollectionListPage;
    }

    private Page createLookupTypeManagePage() {
        Page lookupTypeManagePage = new Page();
        lookupTypeManagePage.setId("ym-ga");
        lookupTypeManagePage.setPageUrl("lookup_type_manage.html");
        lookupTypeManagePage.setDesc("数据字典");

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("0");
        addButton.setDesc("添加");
        lookupTypeManagePage.addModule(addButton);

        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("1");
        editButton.setDesc("修改");
        lookupTypeManagePage.addModule(editButton);

        Button deleteButton = new Button();
        deleteButton.setId("tool_del");
        deleteButton.setIndex("2");
        deleteButton.setDesc("删除");
        lookupTypeManagePage.addModule(deleteButton);

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("3");
        findButton.setDesc("查看详情");
        lookupTypeManagePage.addModule(findButton);

        Button syncButton = new Button();
        syncButton.setId("tool_sync");
        syncButton.setIndex("4");
        syncButton.setDesc("同步");
        lookupTypeManagePage.addModule(syncButton);

        return lookupTypeManagePage;
    }

    private Page createUserTablesPage() {
        Page userTablesPage = new Page();
        userTablesPage.setId("ym-gb");
        userTablesPage.setPageUrl("userTables.html");
        userTablesPage.setDesc("系统用户");

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("0");
        addButton.setDesc("新增");
        userTablesPage.addModule(addButton);

        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("1");
        editButton.setDesc("修改");
        userTablesPage.addModule(editButton);

        Button syncButton = new Button();
        syncButton.setId("tool_sync");
        syncButton.setIndex("2");
        syncButton.setDesc("同步权限信息");
        userTablesPage.addModule(syncButton);

        return userTablesPage;
    }

    private Page createCompanyTablesPage() {
        Page companyTablesPage = new Page();
        companyTablesPage.setId("ym-gc");
        companyTablesPage.setPageUrl("companyTables.html");
        companyTablesPage.setDesc("公司列表");

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("0");
        addButton.setDesc("新增");
        companyTablesPage.addModule(addButton);

        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("1");
        editButton.setDesc("编辑");
        companyTablesPage.addModule(editButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("2");
        refreshButton.setDesc("刷新数据");
        companyTablesPage.addModule(refreshButton);

        return companyTablesPage;
    }

    private Page createProductPage() {
        Page productPage = new Page();
        productPage.setId("ym-gd");
        productPage.setPageUrl("product_manager.html");
        productPage.setDesc("产品配置");

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("0");
        addButton.setDesc("添加");
        productPage.addModule(addButton);

        Button detailButton = new Button();
        detailButton.setId("tool_detail");
        detailButton.setIndex("1");
        detailButton.setDesc("详情");
        productPage.addModule(detailButton);

        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("2");
        editButton.setDesc("修改");
        productPage.addModule(editButton);
        return productPage;
    }

    private Page createDrainagePage() {
        Page drainagePage = new Page();
        drainagePage.setId("ym-vb");
        drainagePage.setPageUrl("drainage.html");
        drainagePage.setDesc("引流管理");

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("0");
        addButton.setDesc("新增");
        drainagePage.addModule(addButton);

        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("1");
        editButton.setDesc("编辑");
        drainagePage.addModule(editButton);

        Button delButton = new Button();
        delButton.setId("tool_delete");
        delButton.setIndex("2");
        delButton.setDesc("删除");
        drainagePage.addModule(delButton);

        return drainagePage;
    }

    //生成规则管理列表
    private Page createCommissionRulePage() {
        Page drainagePage = new Page();
        drainagePage.setId("ym-vc");
        drainagePage.setPageUrl("commissionRuleTables.html");
        drainagePage.setDesc("规则列表");

        Button addButton = new Button();
        addButton.setId("tool_add");
        addButton.setIndex("0");
        addButton.setDesc("新增");
        drainagePage.addModule(addButton);

        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("1");
        editButton.setDesc("编辑");
        drainagePage.addModule(editButton);

        Button delButton = new Button();
        delButton.setId("tool_del");
        delButton.setIndex("2");
        delButton.setDesc("删除");
        drainagePage.addModule(delButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("3");
        refreshButton.setDesc("刷新");
        drainagePage.addModule(refreshButton);

        return drainagePage;
    }

    //生成审核结果管理列表
    private Page createCommissionReviewPage() {
        Page drainagePage = new Page();
        drainagePage.setId("ym-vd");
        drainagePage.setPageUrl("commissionReviewTables.html");
        drainagePage.setDesc("佣金审核结果列表");

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("0");
        refreshButton.setDesc("刷新");
        drainagePage.addModule(refreshButton);

        return drainagePage;
    }

    //生成审核结果管理列表
    private Page createInviteFriendsPage() {
        Page invitePage = new Page();
        invitePage.setId("ym-vf");
        invitePage.setPageUrl("inviteFriends.html");
        invitePage.setDesc("邀请好友");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        invitePage.addModule(findButton);

        Button exportButton = new Button();
        exportButton.setId("tool_export");
        exportButton.setIndex("1");
        exportButton.setDesc("导出");
        invitePage.addModule(exportButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("2");
        refreshButton.setDesc("刷新");
        invitePage.addModule(refreshButton);

        return invitePage;
    }

    //生成佣金审核列表
    private Page createCommissionReviewBeforePage() {
        Page reviewPage = new Page();
        reviewPage.setId("ym-ve");
        reviewPage.setPageUrl("commissionReviewBeforeTables.html");
        reviewPage.setDesc("佣金审核列表");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("查看");
        reviewPage.addModule(findButton);

        Button okButton = new Button();
        okButton.setId("tool_ok");
        okButton.setIndex("1");
        okButton.setDesc("通过");
        reviewPage.addModule(okButton);

        Button noButton = new Button();
        noButton.setId("tool_no");
        noButton.setIndex("2");
        noButton.setDesc("拒绝");
        reviewPage.addModule(noButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("3");
        refreshButton.setDesc("刷新");
        reviewPage.addModule(refreshButton);

        return reviewPage;
    }


    //生成退款列表
    private Page createRefundReviewPage() {
        Page reviewPage = new Page();
        reviewPage.setId("ym-xa");
        reviewPage.setPageUrl("refundReviewTables.html");
        reviewPage.setDesc("退款列表");

        Button findButton = new Button();
        findButton.setId("tool_find");
        findButton.setIndex("0");
        findButton.setDesc("详情");
        reviewPage.addModule(findButton);

        Button okButton = new Button();
        okButton.setId("tool_ok");
        okButton.setIndex("1");
        okButton.setDesc("确认");
        reviewPage.addModule(okButton);

        Button noButton = new Button();
        noButton.setId("tool_no");
        noButton.setIndex("2");
        noButton.setDesc("退款");
        reviewPage.addModule(noButton);

        Button refreshButton = new Button();
        refreshButton.setId("tool_refresh");
        refreshButton.setIndex("3");
        refreshButton.setDesc("驳回");
        reviewPage.addModule(refreshButton);

        return reviewPage;
    }

    //生成退款流水
    private Page createRefundRecordPage() {
        Page reviewPage = new Page();
        reviewPage.setId("ym-xb");
        reviewPage.setPageUrl("refundRecord.html");
        reviewPage.setDesc("退款流水");

        return reviewPage;
    }

    //生成渠道基数管理
    private Page createRegisterSourceManage() {
        Page reviewPage = new Page();
        reviewPage.setId("ym-vg");
        reviewPage.setPageUrl("register_source_manage.html");
        reviewPage.setDesc("渠道扣量管理");
        Button editButton = new Button();
        editButton.setId("tool_edit");
        editButton.setIndex("1");
        editButton.setDesc("编辑");
        reviewPage.addModule(editButton);
        return reviewPage;
    }
}
