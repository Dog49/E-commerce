package com.imooc.mall.service.impl;

import com.imooc.mall.dao.CategoryMapper;
import com.imooc.mall.pojo.Category;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.imooc.mall.consts.MallConst.ROOT_PARENT_ID;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /*
    *  Time: http > disk > memory > cpu
    * mysql > redis > memcache > cpu cache
    * Never do http request in for loop
    * Never do mysql request in for loop
    *
    * */
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
//        List<CategoryVo> categoryVoList = new ArrayList<>();
        List<Category> categories = categoryMapper.selectAll();

//        for(Category category : categories){
//            if(category.getParentId().equals(ROOT_PARENT_ID)){
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(category,categoryVo);
//                categoryVoList.add(categoryVo);
//            }
//        }

        //lambda + Stream
        List<CategoryVo> categoryVoList = categories.stream()
                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID)) //过滤出parentId为0的
                .map(this::category2CategoryVo) //将category转为categoryVo
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                .collect(Collectors.toList()); //Collect to the Collection

        findSubCategory(categories, categoryVoList); //递归调用;
        return ResponseVo.success(categoryVoList);
    }

    private CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    private void findSubCategory(List<Category> categories, List<CategoryVo> categoryVoList){
        for(CategoryVo categoryVo : categoryVoList){
            List<CategoryVo> subCategoryVoList = new ArrayList<>();
            for(Category category : categories){
                if(categoryVo.getId().equals(category.getParentId())){
                    subCategoryVoList.add(category2CategoryVo(category)); //将category转为categoryVo, and add it to VoList
                }
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                categoryVo.setSubCategories(subCategoryVoList);
                findSubCategory(categories, subCategoryVoList);
            }
        }
    }

}
