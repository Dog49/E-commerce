package com.imooc.pay.controller;

import com.imooc.pay.service.impl.PayService;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;
    @RequestMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType")BestPayTypeEnum bestPayTypeEnum) {
        PayResponse response = payService.create(orderId, amount, bestPayTypeEnum);
        //将codeurl存到map里，并且传到视图create.ftl
        Map<String, String> map = new HashMap<>();

        //The rendering logic or UI view varies based on the selected payment method.
        //WXPAY_NATIVE use codeUrl, ALIPAY_PC use body
        if (bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE) {
            map.put("codeUrl", response.getCodeUrl());
            return new ModelAndView("createForWxpay", map);
        }else if (bestPayTypeEnum == BestPayTypeEnum.ALIPAY_PC) {
            map.put("body", response.getBody());
            return new ModelAndView("createForAlipay", map);
        }

        throw new RuntimeException("Unknow pay type");
    }
    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData) {
//        receive the notification from alipay like pay completed
        return payService.asyncNotify(notifyData);
    }
}
