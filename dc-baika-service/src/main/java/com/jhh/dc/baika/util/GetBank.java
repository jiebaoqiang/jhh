package com.jhh.dc.baika.util;

/**
 * 银行手动维护
 */
public class GetBank {

    public static String getname(String bankId) {
        String linkCode = "";
        if ("1".equals(bankId)){
            linkCode ="BOC";
        }else if("2".equals(bankId)){
            linkCode ="ABC";
        }else if ("3".equals(bankId)){
            linkCode ="ICBC";
        }else if("4".equals(bankId)){
            linkCode ="CCB";
        }else if("5".equals(bankId)){
            linkCode ="COMM";
        }else if("6".equals(bankId)){
            linkCode ="SZPAB";
        }else if("7".equals(bankId)){
            linkCode ="SPDB";
        }else if("8".equals(bankId)){
            linkCode ="CITIC";
        }else if("9".equals(bankId)){
            linkCode ="CIB";
        }else if("10".equals(bankId)){
            linkCode ="CEB";
        }else if("11".equals(bankId)){
            linkCode ="PSBC";
        }else if("12".equals(bankId)){
            linkCode ="BOS";
        }else if("25".equals(bankId)){
            linkCode ="BJRCB";
        }else if("26".equals(bankId)){
            linkCode ="BCCB";
        }else if("27".equals(bankId)){
            linkCode ="GDB";
        }else if("28".equals(bankId)){
            linkCode ="HXB";
        }else if("29".equals(bankId)){
            linkCode ="CMBC";
        }else if("30".equals(bankId)){
            linkCode ="SPDB";
        }else if("31".equals(bankId)){
            linkCode ="SHRCB";
        }else if("32".equals(bankId)){
            linkCode ="PSBC";
        }else if("33".equals(bankId)){
            linkCode ="CMB";
        }else if("34".equals(bankId)){
            linkCode ="CITIC";
        }
        return linkCode;
    }

    public static void main(String[] args) {

        String cardNum= "6215581306000665451";


        //System.out.print(getBankId(cardNum));

    }

}

