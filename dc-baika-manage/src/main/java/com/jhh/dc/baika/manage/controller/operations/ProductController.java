package com.jhh.dc.baika.manage.controller.operations;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.product.ProductService;
import com.jhh.dc.baika.entity.app.Product;
import com.jhh.dc.baika.entity.app.ProductTerm;
import com.jhh.dc.baika.manage.entity.Result;
import com.jhh.dc.baika.common.util.DFSUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:system.properties")
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;
    @Value("${uploadUrl}")
    private String uploadUrl;

    /**
     * 查询产品
     */
    @RequestMapping(value = "/getProducts")
    public String getProducts(HttpServletRequest request) {
        Result result = new Result();
        int offset = Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));
        ResponseDo<List<Product>> rsp = productService.getProductAll();
        PageInfo<Product> pageInfo = new PageInfo();
        pageInfo.setTotal(rsp.getData().size());
        pageInfo.setList(rsp.getData());
        return JSON.toJSONString(pageInfo);
    }

    /**
     * 添加产品
     */
    @RequestMapping(value = "/addProduct")
    public ResponseDo addProduct(HttpServletRequest request) {
        ResponseDo result = new ResponseDo();
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File(""));
            ServletFileUpload upload = new ServletFileUpload(factory);
            Map<String, String> map = new HashMap(32);
            List<FileItem> fileItems = upload.parseRequest(request);
            FileItem fileItem = null;
            for (FileItem fi : fileItems) {
                //表示文件
                if (!fi.isFormField() && fi.getFieldName().equals("product_icon")) {
                    fileItem = fi;
                } else if (fi.isFormField()) {
                    if (map.containsKey(fi.getFieldName())) {
                        map.put(fi.getFieldName(), map.get(fi.getFieldName()) + "|" + fi.getString("UTF-8"));
                    } else {
                        map.put(fi.getFieldName(), fi.getString("UTF-8"));
                    }

                }
            }
            if (fileItem != null) {
                MultipartFile file = new CommonsMultipartFile(fileItem);
                String fileSavePath = DFSUtil.uploadFile(uploadUrl, file, "png");
                if (!StringUtils.isEmpty(fileSavePath)) {
                    map.put("product_icon", fileSavePath);
                }
            }
            Product product = convertRequestToPro(map);
            ResponseDo rsp = productService.saveProduct(product);
        } catch (FileUploadException e) {
            e.printStackTrace();
            log.info("添加产品异常:{}", e.getMessage());
            result.setInfo(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("添加产品异常:{}", e.getMessage());
            result.setInfo(e.getMessage());
        }
        return result;
    }
    /**
     * 支持添加和修改期
     * */
    @RequestMapping(value = "/updateProduct")
    public ResponseDo updateProduct(HttpServletRequest request) {
        ResponseDo result = new ResponseDo();
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File(""));
            ServletFileUpload upload = new ServletFileUpload(factory);
            Map<String, String> map = new HashMap(32);
            List<FileItem> fileItems = upload.parseRequest(request);
            FileItem fileItem = null;
            for (FileItem fi : fileItems) {
                //表示文件
                if (!fi.isFormField() && fi.getFieldName().equals("product_icon")) {
                    fileItem = fi;
                } else if (fi.isFormField()) {
                    if (map.containsKey(fi.getFieldName())) {
                        map.put(fi.getFieldName(), map.get(fi.getFieldName()) + "|" + fi.getString("UTF-8"));
                    } else {
                        map.put(fi.getFieldName(), fi.getString("UTF-8"));
                    }

                }
            }
            if (fileItem != null) {
                MultipartFile file = new CommonsMultipartFile(fileItem);
                String fileSavePath = DFSUtil.uploadFile(uploadUrl, file,"png");
                if (!StringUtils.isEmpty(fileSavePath)) {
                    map.put("product_icon", fileSavePath);
                }
            }else{
                map.remove("product_icon");
            }
            Product product = convertRequestToPro(map);
            ResponseDo rsp = productService.updateProduct(product);
        } catch (FileUploadException e) {
            e.printStackTrace();
            log.info("修改产品异常:{}", e.getMessage());
            result.setInfo(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("修改产品异常:{}", e.getMessage());
            result.setInfo(e.getMessage());
        }
        return result;
    }


    /**
     * 把map转化为product对象
     */
    public Product convertRequestToPro(Map<String, String> map) {
        JSONObject job = (JSONObject) JSONObject.toJSON(map);
        Product product = JSONObject.toJavaObject(job, Product.class);
        return product;
    }
}
