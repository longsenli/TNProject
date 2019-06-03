package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.safetyAndEPService.ISafetyAndPEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-06-03 16:25
 */
@RestController
@RequestMapping("/api/safetyandep")
public class SafetyAndEPController {
    @Autowired
    private ISafetyAndPEService safetyAndPEService ;


    @RequestMapping(value = "/getsalesorderdetail")
    public TNPYResponse getIchnographyDetailInfo(String mainRegionID)
    {
        return  safetyAndPEService.getIchnographyDetailInfo(mainRegionID);
    }


}
