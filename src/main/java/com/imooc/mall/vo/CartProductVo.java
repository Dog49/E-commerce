package com.imooc.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductVo {

    private Integer productId;


    // The quantity of the product in the shopping cart
    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private String productDetail;

    private BigDecimal productPrice;

    private Integer productStatus;

    //TotalPrice equal to quantity * productPrice
    private BigDecimal productTotalPrice;

    private Integer productStock;

    //To determine whether the product is selected
    private Boolean productSelected;

}
