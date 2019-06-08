package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.materialService.IMaterialService;
import com.tnpy.mes.service.notificationService.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-06-08 10:18
 */
@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private INotificationService notificationService ;

    @RequestMapping(value = "/getnotificationgroup")
    public TNPYResponse getNotificationGroup( ) {
        return notificationService.getNotificationGroup();
    }
}
