package com.imooc.pay.service;


import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface IPayService {
    /*
     * Create a new pay order
     */
    void create (String orderId, BigDecimal amount);
}
