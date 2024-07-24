package com.imooc.mall.dao;

import com.imooc.mall.MallApplication;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.pojo.Order;
import com.imooc.mall.pojo.OrderItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class OrderItemMapperTest extends MallApplicationTests {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Test
    public void deleteByPrimaryKey() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void insertSelective() {
    }

    @Test
    public void selectByPrimaryKey() {
        OrderItem orderItem = orderItemMapper.selectByPrimaryKey(1);
        System.out.println(orderItem.toString());

    }

    @Test
    public void updateByPrimaryKeySelective() {
    }

    @Test
    public void updateByPrimaryKey() {
    }
}