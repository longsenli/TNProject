package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.PlasticService.IPlasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-08-19 10:42
 */
@RestController
@RequestMapping("/api/plastic")
public class PlasticController {
    @Autowired
    private IPlasticService plasticService ;

    @RequestMapping(value = "/getPlasticUsedRecord")
    public TNPYResponse getPlasticUsedRecord(String plantID,String lineID,String locationID, String startTime, String endTime ) {
        return  plasticService.getPlasticUsedRecord( plantID, lineID,locationID, startTime, endTime );
    }

    @RequestMapping(value = "/addPlasticUsedRecord")
    public TNPYResponse addPlasticUsedRecord(String listID,String userID,String userName,String plantID,String lineID,String locationID,String orderID)
    {
        return  plasticService.addPlasticUsedRecord( listID,userID,userName,plantID, lineID,locationID ,orderID);
    }

    @RequestMapping(value = "/plasticDataProvenance")
    public TNPYResponse plasticDataProvenance(String id)
    {
        return  plasticService.plasticDataProvenance(id );
    }
}
