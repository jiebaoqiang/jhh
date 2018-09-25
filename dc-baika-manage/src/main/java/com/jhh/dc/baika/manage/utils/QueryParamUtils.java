package com.jhh.dc.baika.manage.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * 查询参数构建工具,仅支持[ "contains","=", "between"]
 */
public class QueryParamUtils {
    private final static String eq = "=";
    private final static String contains = "contains";
    private final static String startswith = ">=";
    private final static String start = ">";
    private final static String endswith = "<=";
    private final static String end = "<";
    
    public static Map<String,Object> buildRepaymentPlanQueryMap(Map<String,Object> resultMap,String queryParams){
        if(StringUtils.isEmpty(queryParams)){
            return resultMap;
        }
        JSONArray paramArray = JSON.parseArray(queryParams);
        if(queryParams.contains("and")){//多个参数
            for(int i=0;i<paramArray.size();i++){
                Object o  = paramArray.get(i);
                if(o instanceof JSONArray){
                    JSONArray oa = (JSONArray)o;
                    Object subO  = oa.get(0);
                    if(subO instanceof JSONArray){
                        String po = ((JSONArray) subO).toJSONString();
                        buildRepaymentPlanQueryMap(resultMap,po);
                    }else{
                        createParams(resultMap, oa);
                    }
                }
            }
        }else{
            createParams(resultMap, paramArray);
        }
        return resultMap;
    }

    public static Map getParams(Map<String, String[]> args) {
        Iterator<String> keys = args.keySet().iterator();
        Map arg = new HashMap();
        while (keys.hasNext()) {
            String key = keys.next();
            if("filter".equals(key)) {
                String[] filter = args.get(key);
                if(filter.length < 1) {
                    continue;
                }
                if(StringUtils.isEmpty(filter[0])){
                    continue;
                }
                String st = filter[0];
                loopJson(arg, st);
            }else if ("sort".equals(key)) {
                String[] sort = args.get(key);
                if(sort.length < 1) {
                    continue;
                }
                if(StringUtils.isEmpty(sort[0])) {
                    continue;
                }

                JSONObject jo = JSON.parseArray(sort[0]).getJSONObject(0);
                arg.put("selector", jo.get("selector"));

                if(jo.getBoolean("desc")){
                    arg.put("desc", "desc");
                }else{
                    arg.put("desc", "asc");
                }
            }
        }
        return arg;
    }

    public static Map getargs(Map<String, String[]> args) {
        Iterator<String> keys = args.keySet().iterator();
        Map arg = new HashMap();
        while (keys.hasNext()) {
            String key = keys.next();
            if ("filter".equals(key)) {
                String[] filter = args.get(key);
                if (filter.length > 0 && StringUtils.isNotEmpty(filter[0])) {
                    String st = filter[0];
                    JSONArray js = JSON.parseArray(st);
                    for (int i = 0; i < js.size(); i++) {
                        if (!"and".equals(js.get(i).toString())) {
                            if (js.get(i) instanceof JSONArray) {
                                JSONArray jss = JSON.parseArray(js.get(i)
                                        .toString());
                                if (jss.get(0) instanceof JSONArray) {
                                    JSONArray jsdate = jss;
                                    for (int j = 0; j < jsdate.size(); j++) {
                                        setDate(arg, jsdate.get(j));
                                    }
                                } else {
                                    Object o = jss.get(2);
                                    if (o instanceof JSONObject) {
                                        arg.put(jss.get(0),
                                                ((JSONObject) o).get("value"));
                                    } else {
                                        setDate(arg, jss);
                                        arg.put(jss.get(0), jss.get(2));
                                    }
                                }
                            } else {
                                Object o = js.get(2);
                                if (o instanceof JSONObject) {
                                    arg.put(js.get(0),
                                            ((JSONObject) o).get("value"));
                                } else {
                                    arg.put(js.get(0), js.get(2));
                                }
                                break;
                            }
                        }
                    }
                }
            } else if ("sort".equals(key)) {
                String[] sort = args.get(key);
                if (sort.length > 0 && StringUtils.isNotEmpty(sort[0])) {
                    JSONObject jo = JSON.parseArray(sort[0]).getJSONObject(0);
                    arg.put("selector", jo.get("selector"));

                    if(jo.getBoolean("desc")){
                        arg.put("desc", "desc");
                    }else{
                        arg.put("desc", "asc");
                    }
                }
            }
        }
        return arg;
    }

    public static Map getargs(Map<String, String[]> args, String orderBy, String desc) {
        Map param = getargs(args);
        if(!Detect.notEmpty(param.get("selector") + "") ){
            param.put("selector", orderBy);
            param.put("desc", desc);
        }
        return param;
    }

    public static void setDate(Map<String, Object> arg, Object js) {
        if (js instanceof JSONArray) {
            JSONArray jss = (JSONArray) js;
            if (jss.get(1).toString().indexOf(">") > -1) {
                arg.put(jss.getString(0) + "_start", jss.getString(2));
            } else if (jss.get(1).toString().indexOf("<") > -1) {
                arg.put(jss.getString(0) + "_end", jss.getString(2));
            }
        }
    }
    
    /**
     * json值转换
     * @param key
     * @param data
     * @return
     */
	public static String getJsonData(String[] key, String[] data){
		Assertion.notEmpty(key, "key不能为空");
		Assertion.isTrue(key.length != data.length, "key和数据不匹配");
		
		Map<String, String> map = new HashMap<String, String>();
		for(int i = 0 ; i< key.length; i++){
			map.put(key[i], data[i]);
		}
		return JSON.toJSONString(map);
	}
	
	/**
     * json值转换
     * @param key
     * @param data
     * @return
     */
	public static String getJsonData(String key, String data){
		Assertion.notEmpty(key, "key不能为空");
		Map<String, String> map = new HashMap<String, String>();
		map.put(key, data);
		return JSON.toJSONString(map);
	}

    private static void loopJson(Map map, String jsonString) {
        JSONArray js = JSON.parseArray(jsonString);
        for (Object json : js) {
            if ("and".equals(json.toString())) {
                continue;
            }
            if (json instanceof JSONArray) {
                loopJson(map, ((JSONArray) json).toJSONString());
            }else{
                createParams(map, js);
                break;
            }
        }
    }

    private static void createParams(Map<String, Object> resultMap, JSONArray subArray) {
        String key = subArray.get(0) + "";
        String patten = subArray.get(1) + "";
        Object value = subArray.get(2);

        if (value instanceof JSONObject) {
            value = ((JSONObject) value).get("value");
        }

        switch (patten){
            case eq :
                resultMap.put(key+"Eq",value);
                break;
            case contains:
                resultMap.put(key+"Contains",value);
                break;
            case startswith:
                resultMap.put(key+"Startswith",value);
                break;
            case start:
                resultMap.put(key+"Start",value);
                break;
            case endswith:
                resultMap.put(key+"Endswith",value);
                break;
            case end:
                resultMap.put(key+"End",value);
                break;
        }
    }


    public static void buildPage(HttpServletRequest request, int customSize) {
        buildPage(request, customSize, true);
    }

    public static void buildPage(HttpServletRequest request, boolean count) {
        buildPage(request, 0, count);
    }

    public static void buildPage(HttpServletRequest request) {
        buildPage(request,0);
    }

    public static void buildPage(HttpServletRequest request,int customSize, boolean count) {
        int offset = Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size ;
        if(Detect.isPositive(customSize)){
            size = customSize;
        }else{
            size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));

        }
        PageHelper.offsetPage(offset, size, count);
    }

}
