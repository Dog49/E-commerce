package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {


    ERROR(-1,"SERVICE ERROR"),
    SUCCESS(0,"SUCCESS"),
    PASSWORD_ERROR(1, "WRONG_PASSWORD"),
    USERNAME_EXIST(2, "USERNAME_EXIST"),
    EMAIL_EXIST(3, "EMAIL_EXIST"),
    PARAM_ERROR(4, "PARAM_ERROR"),
    NEED_LOGIN(10, "PLEAST LOGIN FIRST"),

    USERNAME_OR_PASSWORD_ERROR(11, "USERNAME OR PASSWORD ERROR"),
    PRODUCT_NOT_EXIST(12, "PRODUCT_NOT_EXIST"),
    PRODUCT_OFF_SALE_OR_DELETED(13, "PRODUCT_OFF_SALE_OR_DELETED"),
    PRODUCT_STOCK_NOT_ENOUGH(14, "PRODUCT_STOCK_NOT_ENOUGH"),
    CART_PRODUCT_SELECTED_NOT_EXIST(15, "CART_PRODUCT_SELECTED_NOT_EXIST"),
    ;

    final Integer code;

    final String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
