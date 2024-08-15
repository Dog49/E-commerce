package com.imooc.mall.controller;


import com.imooc.mall.pojo.User;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @PostMapping("/register")
    public ResponseVo register(@RequestBody User user){
        log.info("username={}", user.getUsername());
        return ResponseVo.success("Register success");
    }
}
