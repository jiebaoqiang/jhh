package com.jhh.dc.baika.entity.enums;

import com.jhh.dc.baika.common.util.CodeReturn;
import org.apache.commons.lang.StringUtils;

/**
 * Create by Jxl on 2017/9/12
 */
public enum BorrowStatusEnum implements CodeBaseEnum{

    APPLY_PROCESS("BS001","申请中"),
    WAIT_SIGN("BS002","待签约"),
    SIGNED("BS003","已签约"),
    WAIT_REPAY("BS004","待还款"),   // 待付款/待还款
    EXPIRE_REPAY("BS005","逾期未还"),
    NORMAL_REPAY("BS006","正常结清"),
    CANCEL("BS007","已取消"),
    REJECT_AUDIT("BS008","审核未通过"),
    REJECT_AUTO_AUDIT("BS009","电审未通过"),
    EXPIRE_SETTLE("BS010","逾期结清"),
    LOAN_PROCESS("BS011","放款中"), //发放中/放款中
    LOAN_FAIL("BS012","放款失败"),//发放失败/放款失败
    EARLY_SETTLE("BS014","提前结清"),
    WAIT_PAY("BS015","待缴费"),
    PAY_ING("BS016","缴费中"),
    PAY_FAIL("BS017","缴费失败"),
    PAY_SUCESS("BS018","已缴费");

    BorrowStatusEnum(String code,String describe){
        this.code = code;
        this.describe = describe;
    }

    @Override
    public String getCode() {
        return code;
    }
    @Override
    public String description() {
        return describe;
    }

    public static String getDescByCode(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        for (BorrowStatusEnum statusEnum : BorrowStatusEnum.values()) {
            if(StringUtils.equals(statusEnum.code,code)){
                return statusEnum.describe;
            }
        }
        return null;
    }

    public static   String getBorrStatusValue(String borrStatus,String productType){
        if(StringUtils.isEmpty(borrStatus)||"null".equals(borrStatus)|| StringUtils.isEmpty(productType)||"null".equals(productType)){
            return "";
        }
        String borrStatusValue=BorrowStatusEnum.getDescByCode(borrStatus);
        if(productType.equals(CodeReturn.PRODUCT_TYPE_CODE_CARD)){
            if(borrStatus.equals(BorrowStatusEnum.WAIT_REPAY.getCode())){   //BS004
                return "待付款";
            }
            if(borrStatus.equals(BorrowStatusEnum.LOAN_PROCESS.getCode())){ //BS011
                return "发放中";
            }
            if(borrStatus.equals(BorrowStatusEnum.LOAN_FAIL.getCode())){    //BS012
                return "发放失败";
            }
        }
        return borrStatusValue;
    }

    private String code;

    private String describe;

}
