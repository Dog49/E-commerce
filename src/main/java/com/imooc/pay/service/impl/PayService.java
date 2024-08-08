package com.imooc.pay.service.impl;

import com.imooc.pay.dao.PayInfoMapper;
import com.imooc.pay.enums.PayPlatformEnum;
import com.imooc.pay.pojo.PayInfo;
import com.imooc.pay.service.IPayService;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Slf4j
@Service
public class PayService implements IPayService {

    @Autowired
    private BestPayService bestPayService;

    @Autowired
    private PayInfoMapper payInfoMapper;

    /* 创建支付订单 Create Pay Order
     * @param orderId
     * @param amount
     */
    @Override
    public PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {
        //写入数据库
        PayInfo payInfo = new PayInfo(Long.parseLong(orderId),
                                    PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                                    OrderStatusEnum.NOTPAY.name(),
                                    amount);

        //Use insertSelective instead of insert because the info we import lack of other information such as id
        payInfoMapper.insertSelective(payInfo);

        PayRequest request = new PayRequest();
        request.setOrderName("11528905-最好的支付sdk");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        request.setPayTypeEnum(bestPayTypeEnum);


        PayResponse response = bestPayService.pay(request);
        log.info("Initiate Payment response={}", response);

        return response;

    }

    /*
    * @param NotifyData
    * */
    @Override
    public String asyncNotify(String notifyData) {
        //1. 签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("Async Notify payResponse={}", payResponse);

        //2.金额校验（Search order from database）
        //Send alert through message when Serious situation
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        if (payInfo == null) {
            throw new RuntimeException("PayInfo not exist");
        }
        //if the order status is not "Success"
        if (!OrderStatusEnum.SUCCESS.name().equals(payInfo.getPlatformStatus())) {
            if(payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                throw new RuntimeException("The amount is not correct between async notify and database, orderNo=" + payInfo.getOrderNo());
            }

            //3. 修改订单支付状态
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            payInfo.setUpdateTime(null);
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }

        //TODO pay发送MQ消息，mall接受MQ消息

        //4.If Paid, tell the pay platform that do not send the notification again
        if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        } else if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            return "success";
        }
        throw new RuntimeException("Unknown pay platform in async notify");
    }

    @Override
    public PayInfo queryByOrderId(String orderId) {
        return payInfoMapper.selectByOrderNo(Long.parseLong(orderId));
    }


}
