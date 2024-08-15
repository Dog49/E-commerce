package com.imooc.mall.controller;


import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.User;
import com.imooc.mall.vo.ResponseVo;
import form.UserForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Objects;

import static com.imooc.mall.enums.ResponseEnum.NEED_LOGIN;
import static com.imooc.mall.enums.ResponseEnum.PARAM_ERROR;


@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @PostMapping("/register")
    public ResponseVo register(@Valid @RequestBody UserForm userForm, BindingResult bindingResult){



      if (bindingResult.hasErrors()){
            log.error("Register Error,{} {}"
                    , Objects.requireNonNull(bindingResult.getFieldError()).getField()
                    ,bindingResult.getFieldError().getDefaultMessage()
            );
            return ResponseVo.error(PARAM_ERROR, bindingResult);

        }
        log.info("username={}", userForm.getUsername());
        return ResponseVo.error(NEED_LOGIN);
    }
}
