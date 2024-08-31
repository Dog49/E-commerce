package com.imooc.mall.controller;


import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import form.CartAddForm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CartController {
    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm form) {
        return null;
    }
}
