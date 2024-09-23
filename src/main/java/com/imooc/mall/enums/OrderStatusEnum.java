package com.imooc.mall.enums;

import lombok.Getter;

import javax.management.loading.MLetContent;

@Getter
public enum OrderStatusEnum {
    CANCEL(0,"ORDER CANCEL"),
    CREATED(10,"ORDER CREATED"),
    PAID(20,"ORDER PAID"),
    NO_PAY(30,"ORDER NOT PAY"),
    SHIPPED(40,"ORDER SHIPPED"),
    ORDER_TRADE_SUCCESS(50,"ORDER TRADE SUCCESS"),
    ORDER_TRADE_CLOSED(0,"ORDER TRADE CLOSED"),

    ;


    final Integer code;

    final String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
