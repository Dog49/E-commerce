package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {


    ERROR(1,"SERVICE ERROR"),
    SUCCESS(0,"SUCCESS"),
    PASSWORD_ERROR(1, "WRONG_PASSWORD"),
    USERNAME_EXIST(2, "USERNAME_EXIST"),
    EMAIL_EXIST(3, "EMAIL_EXIST"),
    PARAM_ERROR(4, "PARAM_ERROR"),
    NEED_LOGIN(10, "PLEAST LOGIN FIRST"),

    ;

    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
