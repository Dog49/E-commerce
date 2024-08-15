package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {


    ERROR(1,"SERVICE ERROR"),
    SUCCESS(0,"SUCCESS"),
    PASSWORD_ERROR(1, "WRONG_PASSWORD"),
    USER_EXIST(2, "USER_EXIST"),
    NEED_LOGIN(10, "PLEAST LOGIN FIRST"),

    ;

    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
