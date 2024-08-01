package com.imooc.pay.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.junit.Test;
import com.imooc.pay.PayApplicationTests;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PayServiceTest extends PayApplicationTests{

    @Autowired
    private PayService payService;

    @Test
    public void create() {
//        BigDecimal.valueOf(0.01)
//        new BigDecimal("0.01");
        payService.create("12551667891446", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);

    }

}