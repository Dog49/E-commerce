package com.imooc.mall.service.impl;

import com.google.gson.Gson;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.enums.ProductStatusEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.Cart;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.vo.CartProductVo;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import form.CartAddForm;
import form.CartUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements ICartService {

    private final static String CART_REDIS_KEY_TEMPLE = "cart_%d";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private Gson gson = new Gson();

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm form) {
        Product product = productMapper.selectByPrimaryKey(form.getProductId());
        Integer quantity = 1;

        //To determine if the product exists
        if(product == null){
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }

        //To determine if the product is on sale
        if(!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())){
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETED);
        }

        //To determine if the product is in stock
        if(product.getStock() <= 0){
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_NOT_ENOUGH);
        }

        //write in Redis
        //key
        //The product data that need to be saved in redis: productId, quantity, selected
        //Other can be read from the database directly when needed for
//        Cart cart = new Cart(product.getId(), quantity, form.getSelected());
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLE, uid);//key
        String value = opsForHash.get(redisKey, String.valueOf(product.getId()));//value
        Cart cart;
        if(StringUtils.isEmpty(value)){
            //No such product, add a new one
            cart = new Cart(product.getId(), quantity, form.getSelected());
        }else{
            //There is such a product, quantity + 1;
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
        }
        opsForHash.put(redisKey, String.valueOf(product.getId()), gson.toJson(cart));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLE, uid);//key
        Map<String, String> entries = opsForHash.entries(redisKey);

        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();

        boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;

        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            //TODO Need to be optimized, use in of mysql
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product != null) {
                CartProductVo cartProductVo = new CartProductVo(
                        product.getId(),
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getDetail(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );
                cartProductVoList.add(cartProductVo);

                if (!cart.getProductSelected()) {
                    //If there is a product that is not selected, set the selectAll to false
                    selectAll = false;
                }


                if(cart.getProductSelected()){
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }
            }

            cartTotalQuantity += cart.getQuantity();
        }
        cartVo.setSelectedAll(selectAll);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.getCartTotalPrice();
        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {
        //Before updating, search the content firstly
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLE, uid);//key
        String value = opsForHash.get(redisKey, String.valueOf(productId));//value
        Cart cart;
        if(StringUtils.isEmpty(value)){
            //No such product, report error
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_SELECTED_NOT_EXIST);
        }

        //There is such a product, change the quantity;
        cart = gson.fromJson(value, Cart.class);
        if(form.getQuantity() != null && form.getQuantity() >= 0){
            //Quantity is not null and greater than 0, update the quantity
            //It should be the quantity in form, not in cart
            cart.setQuantity(form.getQuantity());
        }
        if(form.getSelected() != null) {
            cart.setProductSelected(form.getSelected());
        }

        opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));

        return list(uid);

    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLE, uid);//key
        String value = opsForHash.get(redisKey, String.valueOf(productId));//value
        Cart cart;
        if(StringUtils.isEmpty(value)){
            //No such product, report error
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_SELECTED_NOT_EXIST);
        }

        opsForHash.delete(redisKey, String.valueOf(productId));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLE, uid);//key
        Map<String, String> entries = opsForHash.entries(redisKey);


       for (Cart cart: listForCart(uid)) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey, String.valueOf(cart.getProductId()), gson.toJson(cart));
       }
       return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unSelectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLE, uid);//key
        Map<String, String> entries = opsForHash.entries(redisKey);

        for(Cart cart:listForCart(uid)) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey, String.valueOf(cart.getProductId()), gson.toJson(cart));
        };

        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);
        return ResponseVo.success(sum);
    }

    private List<Cart> listForCart(Integer uid) {
        // Create a HashOperation object that allows us to perform operations on hash data stored in Redis.
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        // Create a key for the Redis hash, which is placeholder for the user ID.
        String redisKey = String.format(CART_REDIS_KEY_TEMPLE, uid);//key
        // Get all entries from the Redis hash. Each entry is a key-value pair, where the key is the product ID and the value is the JSON string representation of the Cart object.
        Map<String, String> entries = opsForHash.entries(redisKey);

        //Create a new ArrayList object to strore the shopping carts.
        List<Cart> cartList = new ArrayList<>();
        for(Map.Entry<String, String> entry : entries.entrySet()){//Starts a for-each loop to iterate through the entries in the entries Map.
            //Covert the JSON string to a Cart object using the Gson library.
            cartList.add(gson.fromJson(entry.getValue(), Cart.class));
        }

        return cartList;

    }
}
