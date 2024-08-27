package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ProductStatusEnum {

    ON_SALE(1,"onSale"),

    OFF_SALE(2,"offSale"),

    DELETED(3,"deleted"),
    ;

    final Integer code;

    final String desc;

    ProductStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
