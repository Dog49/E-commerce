package com.imooc.mall.pojo;

//po(persistent object)
//pojo(plian ordianry java object)


import lombok.Data;

import java.util.Date;

@Data
public class Category {
    private Integer id;
    private Integer parentId;
    private String name;
    private Integer status;
    private Integer sortOrder;
    private Date createTime;
    private Date updateTime;

}
