package com.jhh.dc.baika.manage.utils;

import com.jhh.dc.baika.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map 转换工具类
 */
public class MapUtils {

    private static final Logger logger = LoggerFactory
            .getLogger(MapUtils.class);
    /**
     * 将一个类查询方式加入map（属性值为int型时，0时不加入，
     * 属性值为String型或Long时为null和“”不加入）
     *
     */
    public static Map<String, String> setConditionMap(Object obj){
        Map<String,String> map = new LinkedHashMap<String, String>();
        if(obj==null){
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields){
            String fieldName =  field.getName();
            if(getValueByFieldName(fieldName,obj)!=null &&! "".equals(getValueByFieldName(fieldName,obj)))
                if (field.getType() == Date.class) {
                    map.put(fieldName, DateUtil.getDateStringToHHmmss((Date) getValueByFieldName(fieldName, obj)));
                } else {
                    map.put(fieldName, String.valueOf(getValueByFieldName(fieldName, obj)));
                }
        }
        logger.info(obj.getClass()+"*************MapUtils转换成Map的结果为********\n"+map);
        return map;

    }

    /**
     * 根据属性名获取该类此属性的值
     * @param fieldName
     * @param object
     * @return
     */
    private static Object getValueByFieldName(String fieldName,Object object){
        String firstLetter=fieldName.substring(0,1).toUpperCase();
        String getter = "get"+firstLetter+fieldName.substring(1);
        try {
            Method method = object.getClass().getMethod(getter);
            Object value = method.invoke(object);
            return value;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     *  第三方返回响应转换
     * @param str
     * @return
     */
    public static Map<String,String> stringToMap(String str,String type){
        Map<String,String> map = new HashMap<String, String>();
        String [] res = str.split("&");
        for (int i =0; i<res.length;i++){
            String [] result = res[i].split("=");
            map.put(result[0],result[1]);
        }
        //查询接口使用
        if (map.get("tradeList")!=null){
                String [] result = map.get("tradeList").split("\\^");
            if ("withhold".equals(type)){
                map.put("tradelist",result[6]);
                map.put("memo",result[result.length-1]);
            }else {
                map.put("tradelist",result[3]);
                map.put("memo",result[result.length-2]);
            }
        }
        return map;
    }

    public static String random(){
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String orderTime = format.format(new Date());
        java.text.DecimalFormat def = new java.text.DecimalFormat("000000");
        int b=(int)(Math.random()*1000000%1000000);//产生0-1000000的整数随机数
        String lstStr = def.format(b);
        return orderTime + lstStr;
    }
}
