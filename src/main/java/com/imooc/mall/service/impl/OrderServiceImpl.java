package com.imooc.mall.service.impl;

import com.imooc.mall.dao.OrderItemMapper;
import com.imooc.mall.dao.OrderMapper;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.enums.OrderStatusEnum;
import com.imooc.mall.enums.PaymentTypeEnum;
import com.imooc.mall.enums.ProductStatusEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.*;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderMapper orderMapper;

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
        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();
        for(Cart cart : cartList) {
            //Check database by productId
            Product product = map.get(cart.getProductId());
            //Check if the product exist
            if(product == null) {
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST, "Product: " + cart.getProductId() + " does not exist");
            }
            //Check the status of product
            if(ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())) {
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETED, "Product: " + product.getName() + " is not on sale");
            }

            //Check if the stock is enough
            if(product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_NOT_ENOUGH, "Product: " + product.getName() + " stock is not enough");
            }

            //Because the data in Order can be gotten from orderItem, so use this function to get the data

            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);
        }


        //Calculate total price

        //Generate order and orderItem
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);
        //Save order and orderItem into database
        int row = orderMapper.insertSelective(order);
        if(row <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }


        //Reduce product stock

        //Update cart(Item Selected)

        //Construct orderVo
        return null;
        }

        /*
        * //TODO: write notes
        * */
    private Order buildOrder(Integer uid, Long orderNo, Integer shippingId, List<OrderItem> orderItemList) {
        BigDecimal payment = orderItemList.stream().
                map(OrderItem::getTotalPrice).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());

        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        return order;
    }

        /*
        * 企业级：分布式唯一id
        * @return
        * TODO: Need to improve
        * */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(new BigDecimal(quantity)));
        return item;
    }
}
