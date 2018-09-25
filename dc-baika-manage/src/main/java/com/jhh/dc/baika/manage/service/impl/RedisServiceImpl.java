package com.jhh.dc.baika.manage.service.impl;

import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.manage.mapper.SystemUserMapper;
import com.jhh.dc.baika.manage.service.common.RedisService;
import com.jhh.dc.baika.common.util.Detect;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

@Service
public class RedisServiceImpl implements RedisService {

    private Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    SystemUserMapper collectorsMapper;

    @Override
    public void saveQueryTotalItem(String type, String keySuffix, long totalCount) {
        String key = Constants.YM_ADMIN_SYSTEN_KEY, field = "";
        if (Constants.TYPE_LOAN.equals(type)) {//贷后管理
            key += Constants.MANAGE_LOAN_COUNT;
            field = "loan_";
        } else if (Constants.TYPE_REPAY.equals(type)) {//还款流水
            key += Constants.MANAGE_REPAY_COUNT;
            field = "repay_";
        } else if (Constants.TYPE_REPAY_PLAN.equals(type)) {
            key += Constants.MANAGE_REPAY_PLAN_COUNT;
            field = "rpl_";
        } else if (Constants.TYPE_COLL_INFO.equals(type)) {
            key += Constants.TYPE_COLL_INFO_COUNT;
            field = "coll_";
        }
        logger.info("save key is : {}", key);
        jedisCluster.hset(key, field + keySuffix, String.valueOf(totalCount));
    }

    @Override
    public long selectQueryTotalItem(String type, String field) {
        String key = getRegion(type);
        logger.info("redis key : {}", key);
        String totalCountStr = jedisCluster.hget(key, field);
        logger.info("redis result : {}", totalCountStr);
        if (!StringUtils.isEmpty(totalCountStr) && !StringUtils.endsWith(totalCountStr, "nil")) {
            return Long.valueOf(totalCountStr);
        }
        return 0;
    }

    private String getRegion(String type) {
        String key = Constants.YM_ADMIN_SYSTEN_KEY;
        if (Constants.TYPE_LOAN.equals(type)) {//贷后管理
            key += Constants.MANAGE_LOAN_COUNT;
        } else if (Constants.TYPE_REPAY.equals(type)) {//还款流水
            key += Constants.MANAGE_REPAY_COUNT;
        } else if (Constants.TYPE_REPAY_PLAN.equals(type)) {
            key += Constants.MANAGE_REPAY_PLAN_COUNT;
        } else if (Constants.TYPE_COLL_INFO.equals(type)) {
            key += Constants.TYPE_COLL_INFO_COUNT;
        } else if (Constants.TYPE_AUTH_INFO.equals(type)) {
            key += Constants.TYPE_AUTH_INFO_COUNT;
        }
        return key;
    }


    @Override
    public int selectDownloadCount() {
        String key = Constants.YM_ADMIN_SYSTEN_KEY + Constants.DOWNLOAD_COUNT;
        String countValue = jedisCluster.get(key);
        return StringUtils.isEmpty(countValue) ? 0 : Integer.valueOf(countValue);
    }

    @Override
    public String selectCollertorsUserName(String userId){
        if(!Detect.notEmpty(userId)){
            return null;
        }
        String key = Constants.YM_ADMIN_SYSTEN_KEY + Constants.COLLECTORS_USERID;
        String collertorsStr = jedisCluster.hget(key,userId);

        if(collertorsStr == null){
            String userSysNo = collectorsMapper.getNameBySysNo(userId);
            if(Detect.notEmpty(userSysNo)){
                jedisCluster.hset(key,userId, userSysNo);
            }
        }
        return collertorsStr;
    }

    @Override
    public String getRedisByKey(String key) {
        return jedisCluster.get(key);
    }
}
