package com.jhh.dc.baika.api.product;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.app.Banner;
import com.jhh.dc.baika.entity.app.Product;

import java.util.List;
import java.util.Map;

/**
 *  产品相关操作
 */
public interface ProductService {

    /**
     *  根据设备查询产品信息 和按钮信息
     * @param device 产品Id
     * @return
     */
    ResponseDo<Map<String,Object>> getProduct(String device);
    ResponseDo<Map<String,Object>> getProduct(String device, String productId);

    ResponseDo<List<Product>> getProductAll();

    /**
     * 删除产品
     * @param productId
     * @return
     */
    ResponseDo delProduct(String productId);

    /**
     *  添加新产品
     * @param product
     * @return
     */
    ResponseDo saveProduct(Product product);

    /**
     * 修改产品信息
     * @param product
     * @return
     */
    ResponseDo updateProduct(Product product);


    ResponseDo<Map<String,Object>> getBanner();
}
