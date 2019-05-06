package com.tnpy.mes.controller;

import com.tnpy.mes.service.chargePackService.IChargePackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-05-06 14:37
 */
@RestController
@RequestMapping(value ="/api/chargepack")
public class ChargePackController {
    @Autowired
    private IChargePackService chargePackService;
}
