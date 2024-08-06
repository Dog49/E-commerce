package com.imooc.pay.service;


import com.imooc.pay.pojo.PayInfo;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.springframework.stereotype.Service;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

@Service
public interface IPayService {
    /*
     * Create a new pay order
     */


    /* 创建支付订单 Create Pay Order
     * @param orderId
     * @param amount
     */
    PayResponse create (String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    /*
    * Async Notify
    * @param notifyData
    *
    */
    String asyncNotify (String notifyData);

     /*
     * Query Pay Order by orderId
     * @param orderId
     * @return
     * */
    PayInfo queryByOrderId(String orderId);
}
