package com.imooc.mall.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import form.CartAddForm;
import form.CartUpdateForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@Slf4j
public class ICartServiceTest extends MallApplicationTests {


    @Qualifier("cartServiceImpl")
    @Autowired
    private ICartService cartService;

    private Gson gson  = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(29);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(1,form);
        log.info("list={}",gson.toJson(responseVo));

    }

    @Test
    public void list() {
        ResponseVo<CartVo> list = cartService.list(1);
        log.info("list={}",gson.toJson(list));

    }

    @Test
    public void update() {
        CartUpdateForm form = new CartUpdateForm();
        form.setQuantity(5);
        form.setSelected(false);
        ResponseVo<CartVo> responseVo = cartService.update(1,29,form);
        log.info("list={}",gson.toJson(responseVo));
    }

    @Test
    public void delete() {
        ResponseVo<CartVo> responseVo = cartService.delete(1,28);
        log.info("result={}",gson.toJson(responseVo));
    }
}