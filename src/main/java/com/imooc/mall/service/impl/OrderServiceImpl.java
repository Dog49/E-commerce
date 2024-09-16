package com.imooc.mall.service.impl;

import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;

public class OrderServiceImpl implements IOrderService {
    @Override
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //Shipping address validation

        //Get Cart, and validation (Check if the product is still available)

        //Calculate total price

        //Generate order and orderItem

        //Reduce product stock

        //Update cart(Item Selected)

        //Construct orderVo
        return null;
    }
}
