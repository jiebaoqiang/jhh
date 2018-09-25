package com.jhh.dc.baika.service.product;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.constant.RedisConstant;
import com.jhh.dc.baika.mapper.app.CodeValueMapper;
import com.jhh.dc.baika.mapper.product.BannerMapper;
import com.jhh.dc.baika.mapper.product.ProductMapper;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.product.ProductService;
import com.jhh.dc.baika.entity.app.Banner;
import com.jhh.dc.baika.entity.app.Product;
import com.jhh.dc.baika.entity.manager.CodeValue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品相关逻辑
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory
            .getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private CodeValueMapper codeValueMapper;


    @Override
    public ResponseDo<Map<String, Object>> getProduct(String device) {
        ResponseDo<Map<String, Object>> responseDo = new ResponseDo<>();
        List<Product> product;
        try {
            String str = jedisCluster.hget(RedisConstant.PRODUCT_KEY, device);
            if (StringUtils.isEmpty(str)) {
                product = productMapper.selectByDevice(device);
                jedisCluster.hset(RedisConstant.PRODUCT_KEY, device, JSONObject.toJSONString(product));
            } else {
                product = JSONObject.parseArray(str, Product.class);
            }
            responseDo.setCode(StateCode.SUCCESS_CODE);
            responseDo.setInfo(StateCode.SUCCESS_MSG);
            Map<String, Object> map = new HashMap<>(16);
            map.put("product", product);
            map.put("codeValue", getPhoneService(device, product));
            responseDo.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            responseDo.setCode(StateCode.SYSTEM_CODE);
            responseDo.setInfo(StateCode.SYSTEM_MSG);
        }
        return responseDo;
    }

    @Override
    public ResponseDo<Map<String, Object>> getProduct(String device, String productId) {
        ResponseDo<Map<String, Object>> responseDo = new ResponseDo<>();
        List<Product> product;
        try {
            String str = jedisCluster.hget(RedisConstant.PRODUCT_KEY, productId);
            if (StringUtils.isEmpty(str)) {
                Product p = new Product();
                p.setId(Integer.parseInt(productId));
                product = productMapper.select(p);
                jedisCluster.hset(RedisConstant.PRODUCT_KEY, productId, JSONObject.toJSONString(product));
            } else {
                product = JSONObject.parseArray(str, Product.class);
            }
            responseDo.setCode(StateCode.SUCCESS_CODE);
            responseDo.setInfo(StateCode.SUCCESS_MSG);
            Map<String, Object> map = new HashMap<>(16);
            map.put("product", product);
            map.put("codeValue", getPhoneService(device, product));
            responseDo.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            responseDo.setCode(StateCode.SYSTEM_CODE);
            responseDo.setInfo(StateCode.SYSTEM_MSG);
        }
        return responseDo;
    }


    @Override
    public ResponseDo delProduct(String productId) {
        ResponseDo<Object> responseDo = new ResponseDo<>();
        try {
            productMapper.deleteByPrimaryKey(productId);
            responseDo.setCode(StateCode.SUCCESS_CODE);
            responseDo.setInfo(StateCode.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            responseDo.setCode(StateCode.SYSTEM_CODE);
            responseDo.setInfo(StateCode.SYSTEM_MSG);
        }
        return responseDo;
    }

    @Override
    public ResponseDo<List<Product>> getProductAll() {
        ResponseDo<List<Product>> responseDo = new ResponseDo<>();
        List product;
        try {
            String str = jedisCluster.get(RedisConstant.PRODUCT_ALL_KEY);
            if (StringUtils.isEmpty(str) || str.equals("[]")) {
                product = productMapper.getProductAll();
                jedisCluster.set(RedisConstant.PRODUCT_ALL_KEY, JSONObject.toJSONString(product));
            } else {
                product = JSONObject.parseObject(str, List.class);
            }
            responseDo.setCode(StateCode.SUCCESS_CODE);
            responseDo.setInfo(StateCode.SUCCESS_MSG);
            responseDo.setData(product);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            responseDo.setCode(StateCode.SYSTEM_CODE);
            responseDo.setInfo(StateCode.SYSTEM_MSG);
        }
        return responseDo;
    }

    @Override
    public ResponseDo saveProduct(Product product) {
        logger.info("增加产品请求参数 product" + product);
        ResponseDo<Object> responseDo = new ResponseDo<>();
        try {
            productMapper.insertSelective(product);
            jedisCluster.del(RedisConstant.PRODUCT_ALL_KEY);
            responseDo.setCode(StateCode.SUCCESS_CODE);
            responseDo.setInfo(StateCode.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            responseDo.setCode(StateCode.SYSTEM_CODE);
            responseDo.setInfo(StateCode.SYSTEM_MSG);
        }
        return responseDo;
    }

    @Override
    public ResponseDo updateProduct(Product product) {
        logger.info("修改产品请求参数 product" + product);
        ResponseDo responseDo = new ResponseDo<>();
        try {
            productMapper.updateByPrimaryKeySelective(product);
            jedisCluster.hdel(RedisConstant.PRODUCT_KEY, String.valueOf(product.getId()));
            jedisCluster.del(RedisConstant.PRODUCT_ALL_KEY);
            responseDo.setCode(StateCode.SUCCESS_CODE);
            responseDo.setInfo(StateCode.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            responseDo.setCode(StateCode.SYSTEM_CODE);
            responseDo.setInfo(StateCode.SYSTEM_MSG);
        }
        return responseDo;
    }


    @Override
    public ResponseDo<Map<String, Object>> getBanner() {
        logger.info("获取banner图片信息-------------------------------------------");
        ResponseDo<Map<String, Object>> responseDo = ResponseDo.newSuccessDo();
        Map<String, Object> map = new HashMap<>();
        List<Banner> banners = bannerMapper.selectAll();
        map.put("banner", banners);
        responseDo.setData(map);
        return responseDo;
    }

    private List<CodeValue> getPhoneService(String device, List<Product> products) {
        List<CodeValue> codeValues = codeValueMapper.getCodeValueAll(device + "_phone_services");
        codeValues.forEach(v -> {
            Product p = products.get(0);
//            if ("rent_phone".equals(v.getCodeCode()) && v.getDescription() != null) {
//                String rent = String.format("%.2f", p.getRent());
//                String deposit = String.format("%.2f", p.getDeposit());
//                v.setDescription(String.format(v.getDescription(), p.getTerm() * p.getTermDay(), p.getTerm(), rent, deposit));
//                v.setDescriptions(String.format(v.getDescriptions(), p.getTerm() * p.getTermDay(), p.getTerm(), rent, deposit));
//            } else if ("recycling_phone".equals(v.getCodeCode()) && v.getDescription() != null) {
//                String ransom = String.format("%.2f", p.getRansom());
//                v.setDescription(String.format(v.getDescription(), ransom));
//                v.setDescriptions(String.format(v.getDescriptions(), ransom));
//            }
        });

        return codeValues;
    }


}
