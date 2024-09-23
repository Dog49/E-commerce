package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {

    PAY_ONLINE(1),
    ;

    final Integer code;

    PaymentTypeEnum(Integer code) {
        this.code = code;
    }


}
