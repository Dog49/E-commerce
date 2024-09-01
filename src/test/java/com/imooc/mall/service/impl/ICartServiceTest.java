package com.imooc.mall.service.impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.service.ICartService;
import form.CartAddForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.*;

public class ICartServiceTest extends MallApplicationTests {


    @Qualifier("cartServiceImpl")
    @Autowired
    private ICartService cartService;

    @Test
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(27);
        form.setSelected(true);
        cartService.add(1,form);
    }
}