package com.jhh.dc.baika.entity.manager;
import com.jhh.dc.baika.common.constant.Constants;
import com.jhh.dc.baika.common.util.EnumUtils;
import com.jhh.dc.baika.entity.enums.BorrowStatusEnum;
import com.jhh.dc.baika.common.util.DateUtil;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;


/**
 * Created by wanzezhong on 2018/1/4.
 */
@Getter @Setter @Resource
public class UserList {

    @Excel(name="手机号码")
    private String phone;
    @Excel(name="用户姓名")
    private String name;
    @Excel(name="身份证号")
    private String card_num;
    @Excel(name="黑名单")
    private String blacklist;
    @Excel(name="当前认证节点")
    private String node_code;
    @Excel(name="节点状态")
    private String node_status;
    @Excel(name="当前合同状态")
    private String borrow_status;
    @Excel(name="渠道来源")
    private String source_name;
    @Excel(name="注册时间")
    private String create_date;
    @Excel(name="认证说明")
    private String description;


    public void setBorrow_status(String borrow_status) {
        this.borrow_status = borrow_status;
        String desc = BorrowStatusEnum.getDescByCode(borrow_status);
        if(StringUtils.isNotBlank(desc)){
            this.borrow_status = desc;
        }else{
            this.borrow_status = borrow_status;
        }
    }
    public void setNode_code(String node_code) {
        this.node_code = node_code;
        if(StringUtils.isNotBlank(node_code)){
            this.node_code=Constants.RiskBpmNode.getNameById(Integer.parseInt(node_code));
        }
    }
    public void setBlacklist(String blacklist) {
        if ("Y".equals(blacklist)){
            this.blacklist = "是";
        }else {
            this.blacklist = "否";
        }
    }

    public void setNode_status(String node_status) {
        this.node_status = node_status;
        if(StringUtils.isNotBlank(node_status)) {
            this.node_status = Constants.RiskBpmNodeStatus.getStatusNameByCode(node_status);
        }
    }


    public void setCreate_date(String create_date) {
        this.create_date = DateUtil.stampToDate(create_date);
    }
}
