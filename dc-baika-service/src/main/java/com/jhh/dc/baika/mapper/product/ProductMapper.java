package com.jhh.dc.baika.mapper.product;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

import com.jhh.dc.baika.entity.app.Product;
import com.jhh.dc.baika.entity.app_vo.SignInfo;

/**
 * 2017/12/28.
 */
public interface ProductMapper extends Mapper<Product> {

   List<Product> selectByDevice(String device);

   List<Product> getProductAll();

   SignInfo getSignInfo(String per_id);

}
