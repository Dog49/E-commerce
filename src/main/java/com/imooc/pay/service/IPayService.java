package com.imooc.pay.service;


import org.springframework.stereotype.Service;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

@Service
public interface IPayService {
    /*
     * Create a new pay order
     */
    PayResponse create (String orderId, BigDecimal amount);
}
