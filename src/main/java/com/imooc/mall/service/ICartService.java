package com.imooc.mall.service;

import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import form.CartAddForm;
import org.springframework.stereotype.Service;

@Service
public interface ICartService {

    ResponseVo<CartVo> add(Integer uid, CartAddForm form);

    ResponseVo<CartVo> list(Integer uid);
}
