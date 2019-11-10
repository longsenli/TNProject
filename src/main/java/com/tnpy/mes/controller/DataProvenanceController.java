package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.dataProvenanceService.IDataProvenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/10 9:31
 */
@RestController
@RequestMapping("/api/dataprovenance")
public class DataProvenanceController {
    @Autowired
    private  IDataProvenanceService dataProvenanceService;

    @RequestMapping(value = "/getprovenancebyorderid")
    public TNPYResponse getProvenanceByOrderID(String orderID ) {

        return  dataProvenanceService.getProvenanceByOrderID(orderID);
    }

    @RequestMapping(value = "/getProvenanceByDCDK")
    public TNPYResponse getProvenanceByDCDK(String DKQRCode ) {

        return  dataProvenanceService.getProvenanceByDCDK(DKQRCode);
    }
}
