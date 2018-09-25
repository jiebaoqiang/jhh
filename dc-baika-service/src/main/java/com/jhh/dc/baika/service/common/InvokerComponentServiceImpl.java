package com.jhh.dc.baika.service.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jhh.dc.baika.api.common.InvokerComponentService;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.PropertiesReaderUtil;
import com.jhh.redis.JedisClusterBuilder;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ContextLoader;
import redis.clients.jedis.JedisCluster;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

public class InvokerComponentServiceImpl implements InvokerComponentService {
    @Value("${jdbc.url}")
    private String database;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.driver}")
    private String driverClass;

    private String hosts = PropertiesReaderUtil.read("dubbo","dubbo.registry.address");

    @Override
    public NoteResult invokeComponent() {

        NoteResult response = new NoteResult();
        response.setCode(CodeReturn.SUCCESS_CODE);
        response.setInfo("系统正常");
        Map<String, Object> result = Maps.newHashMap();

        Map<String, Object> dbResult = test2Db();
        List<Map<String, Object>> redisResult = test2Redis();
        List<Map<String, Object>> zkResult = test2Zookeeper();
        result.put("dbStatus", dbResult);
        result.put("redisStatus", redisResult);
        result.put("zkStatus", zkResult);
        response.setData(result);

        return response;
    }


    /**
     * 测试DB是否连接正常
     */
    private Map<String, Object> test2Db() {
        Map<String, Object> result = Maps.newHashMap();
        SqlSessionFactoryBean sqlSessionFactoryBean = ContextLoader.getCurrentWebApplicationContext().getBean(SqlSessionFactoryBean.class);


        try {
            if (ObjectUtils.isEmpty(sqlSessionFactoryBean)) {
                // 重新建立数据库连接
                Class.forName(driverClass);
                Connection connection = DriverManager.getConnection(database,username,password);
                if (ObjectUtils.isEmpty(connection)) {
                    result.put("code2", CodeReturn.fail);
                    result.put("msg2", "数据库连接异常，数据已关闭，连接URL " + database.substring(0, database.indexOf("?")));

                } else {
                    result.put("code2", CodeReturn.success);
                    result.put("msg2", "数据库连接正常，连接URL " + database.substring(0, database.indexOf("?")));
                }
                return result;
            }


            SqlSessionFactory factory = sqlSessionFactoryBean.getObject();
            SqlSession sqlSession = factory.openSession();
            if (sqlSession.getConnection().isClosed()) {
                result.put("code", CodeReturn.fail);
                result.put("msg", "数据库连接异常，数据已关闭，连接URL " + database.substring(0, database.indexOf("?")));
            } else {
                result.put("code", CodeReturn.success);
                result.put("msg", "数据库连接正常，连接URL " + database.substring(0, database.indexOf("?")));
            }

        } catch (Exception e) {
            result.put("code", CodeReturn.fail);
            result.put("msg", "数据库连接异常" + e.getMessage());
        }
        return result;
    }

    /**
     * 测试zk连接是否正常
     */
    private List<Map<String, Object>> test2Zookeeper() {
        String[] ipAddress = hosts.split(",");
        List<Map<String, Object>> result = Lists.newArrayList();
        for (String ip : ipAddress) {
            String[] temp = ip.split(":");
            String host = temp[0];
            String port = temp[1];
            result.add(executeZKStatus(host, port));
        }
        return result;
    }

    private Map<String, Object> executeZKStatus(String host, String port) {
        Map<String, Object> result = Maps.newHashMap();
        String cmd = "stat";
        Socket sock = null;
        BufferedReader reader = null;
        try {
            sock = new Socket(host.trim(), Integer.parseInt(port.trim()));
            OutputStream outstream = sock.getOutputStream();
            // 通过Zookeeper的四字命令获取服务器的状态
            outstream.write(cmd.getBytes());
            outstream.flush();
            sock.shutdownOutput();

            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Mode: ")) {
                    System.out.println(line.replaceAll("Mode: ", "").trim());
                }
            }
            result.put("code", CodeReturn.success);
            result.put("msg", "ZK 连接正常" + "【主机IP】： " + host + "【端口】：" + port);
        } catch (Exception e) {
            result.put("code", CodeReturn.fail);
            result.put("msg", "ZK 异常" + e.getMessage() + "【主机IP】： " + host + "【端口】：" + port);
        } finally {
            try {
                if (sock != null) {
                    sock.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 测试redis连接是否正常
     */
    private List<Map<String, Object>> test2Redis() {

        List<Map<String, Object>> list = Lists.newArrayList();
        JedisCluster jedisCluster = ContextLoader.getCurrentWebApplicationContext().getBean(JedisCluster.class);

        if (ObjectUtils.isEmpty(jedisCluster)) {
            Map<String, Object> result = Maps.newHashMap();
            result.put("code", CodeReturn.fail);
            result.put("msg", "redis连接异常，所有节点");
            // 尝试重新连接
            JedisClusterBuilder builder = new JedisClusterBuilder();

            builder.setMaxAttempts(Integer.parseInt(PropertiesReaderUtil.read("redis","redis.maxAttempts")));
            builder.setAddressConfigs(PropertiesReaderUtil.read("redis","redis.configaddress"));
            builder.setTimeout(Integer.parseInt(PropertiesReaderUtil.read("redis","redis.timeout")));
            builder.setMaxRedirections(Integer.parseInt(PropertiesReaderUtil.read("redis","redis.maxRedirections")));
            builder.setPassword(PropertiesReaderUtil.read("redis","redis.password"));
            builder.setIsAuth(1);

            GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
            genericObjectPoolConfig.setMaxIdle(Integer.parseInt(PropertiesReaderUtil.read("redis","redis.maxIdle")));
            genericObjectPoolConfig.setMaxTotal(Integer.parseInt(PropertiesReaderUtil.read("redis","redis.maxTotal")));
            builder.setGenericObjectPoolConfig(genericObjectPoolConfig);
            try{
                JedisCluster jedis = builder.build();

                if (ObjectUtils.isEmpty(jedis)) {
                    result.put("msg2","redis 重新连接也失败，表明所有节点已宕机，请及时重启服务");
                    result.put("code2",CodeReturn.fail);
                }
            } catch (Exception e){
                result.put("msg2","redis 重新连接也失败，表明所有节点已宕机，请及时重启服务 " + e.getMessage());
                result.put("code2",CodeReturn.fail);
            }

            list.add(result);
            return list;
        }

        // 迭代所有reids 节点
        jedisCluster.getClusterNodes().forEach((key, jedisPool) -> {
            Map<String, Object> closeMap = Maps.newHashMap();
            if (jedisPool.isClosed() || !jedisPool.getResource().isConnected()) {
                closeMap.put("code", CodeReturn.fail);
                closeMap.put("msg", String.format("节点【%s】异常", key));
            } else {
                closeMap.put("code", CodeReturn.success);
                closeMap.put("msg", String.format("节点【%s】连接正常", key));
            }
            list.add(closeMap);
        });

        return list;
    }

}
