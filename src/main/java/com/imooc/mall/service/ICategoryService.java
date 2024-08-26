package com.imooc.mall.service;

import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface ICategoryService {

    ResponseVo<List<CategoryVo>> selectAll();


}
