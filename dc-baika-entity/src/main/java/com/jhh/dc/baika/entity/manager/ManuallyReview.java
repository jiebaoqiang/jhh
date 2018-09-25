package com.jhh.dc.baika.entity.manager;

import com.jhh.dc.baika.entity.enums.BorrowStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.poi.excel.annotation.Excel;


/**
 * Created by wanzezhong on 2018/1/4.
 */
@Getter @Setter
public class ManuallyReview {

    private Integer id;
    private Integer perId;
    @Excel(name="合同编号")
    private String borrNum;
    @Excel(name="姓名")
    private String name;
    @Excel(name="身份证号")
    private String cardNum;
    @Excel(name="手机号码")
    private String phone;
    @Excel(name="通讯录个数")
    private String contactNum;
    @Excel(name="产品类型")
    private String productName;
    @Excel(name="贷款金额")
    private String borrAmount;
    @Excel(name="银行名称")
    private String bankName;
    @Excel(name="银行卡号")
    private String bankCard;
    private String borrStatus;
    @Excel(name="贷款状态")
    private String borrStatusValue;
    private String borrUpStatus;
    @Excel(name="上单状态")
    private String borrUpStatusValue;
    @Excel(name="人工拒绝理由")
    private String reason;
    @Excel(name="认证说明")
    private String description;
    @Excel(name="审核人")
    private String employeeName;
    @Excel(name="审核时间")
    private String auditTime;
    @Excel(name="签约时间")
    private String makeborrDate;

    private String createDate;
    private String baikeluStatus;

    public void setBorrStatus(String borrStatus) {
        this.borrStatus = borrStatus;
        String desc = BorrowStatusEnum.getDescByCode(borrStatus);
        if(StringUtils.isNotBlank(desc)){
            this.borrStatusValue = desc;
        }else{
            this.borrStatusValue = borrStatus;
        }
    }

    public void setBorrUpStatus(String borrUpStatus) {
        this.borrUpStatus = borrUpStatus;
        String desc = BorrowStatusEnum.getDescByCode(borrUpStatus);
        if(StringUtils.isNotBlank(desc)){
            this.borrUpStatusValue = desc;
        }else{
            this.borrUpStatusValue = borrUpStatus;
        }
    }
}
