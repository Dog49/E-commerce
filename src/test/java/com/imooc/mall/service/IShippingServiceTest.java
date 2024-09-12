package com.imooc.mall.service;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.vo.ResponseVo;
import form.ShippingForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.ws.Response;

import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
public class IShippingServiceTest extends MallApplicationTests {


    @Autowired
    private IShippingService shippingService;

    private Integer uid = 1;

    private Integer shippingId;

    private ShippingForm form;

    @Before
    public void before() {
        form = new ShippingForm();
        form.setReceiverName("张三");
        form.setReceiverPhone("010123457");
        form.setReceiverMobile("13539010637");
        form.setReceiverProvince("广东");
        form.setReceiverCity("东莞");
        form.setReceiverDistrict("东城区");
        form.setReceiverAddress("东城街道");
        form.setReceiverZip("523000");

        add();
    }


    public void add() {
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid, form);
        log.info("result={}", responseVo);
        this.shippingId = responseVo.getData().get("shippingId");
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @After
    public void delete() {

        ResponseVo responseVo = shippingService.delete(uid, shippingId);
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void update() {
        form.setReceiverCity("广州市");
        ResponseVo responseVo = shippingService.update(uid, shippingId, form);
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo list = shippingService.list(uid, 1, 10);
        log.info("result={}", list);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), list.getStatus());
    }
}