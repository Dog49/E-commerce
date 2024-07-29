package com.imooc.pay.service.impl;

import com.imooc.pay.service.IPayService;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.imooc.pay.config.BestPayConfig;

import java.math.BigDecimal;

@Slf4j
@Service
public class PayService implements IPayService {

    @Autowired
    private BestPayService bestPayService;

    /* 创建支付订单 Create Pay Order
     * @param orderId
     * @param amount
     */
    @Override
    public PayResponse create(String orderId, BigDecimal amount) {


        PayRequest request = new PayRequest();
        request.setOrderName("11528905-最好的支付sdk");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);


        PayResponse response = bestPayService.pay(request);
        log.info("response={}", response);

        return response;

    }

    /*
    * @param NotifyData
    * */
    @Override
    public void asyncNotify(String notifyData) {
//        1. 签名校验
        PayResponse payResponse= bestPayService.asyncNotify(notifyData);
        log.info("payResponse={}",payResponse);
    }
}
