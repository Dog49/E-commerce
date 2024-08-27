package com.imooc.mall.service.impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.service.IProductService;
import com.imooc.mall.vo.ProductVo;
import com.imooc.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class IProductServiceImplTest extends MallApplicationTests {

    @Autowired
    private IProductService productService;



//    @Test
//    public void list() {
//        productService.list(null, 1, 1);
//    }

    @Test
    public void list() {
        ResponseVo<List<ProductVo>> productVo = productService.list(null, 1, 1);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), productVo.getStatus());
    }
}