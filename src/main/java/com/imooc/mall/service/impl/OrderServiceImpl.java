package com.imooc.mall.service.impl;

import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.Cart;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.pojo.Shipping;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //Shipping address validation
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if(shipping == null) {
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }
        //Get Cart, and validation (Check if the product is still available)
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }

        //Check the stock of the product
        //Get the products from cartList
        Set<Integer> productIdSet = cartList.stream()
                    .map(Cart::getProductId)
                    .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product)); //key: productId, value: product

        for(Cart cart : cartList) {
            //Check database by productId
            Product product = map.get(cart.getProductId());
            //Check if the product exist
            if(product == null) {
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST, "Product " + cart.getProductId() + " does not exist");
            }
            //Check if the stock is enough
            if(product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_NOT_ENOUGH, "Product " + product.getName() + " stock is not enough");
            }

        }


        //Calculate total price

        //Generate order and orderItem

        //Reduce product stock

        //Update cart(Item Selected)

        //Construct orderVo
        return null;
        }
}
