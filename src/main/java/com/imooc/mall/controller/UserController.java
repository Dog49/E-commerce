package com.imooc.mall.controller;


import com.imooc.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @PostMapping("/register")
    public void register(@RequestBody User user){
        log.info("username={}", user.getUsername());
    }
}
