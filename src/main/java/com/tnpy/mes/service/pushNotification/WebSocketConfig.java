package com.tnpy.mes.service.pushNotification;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/19 14:12
 */
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

//@Configuration
public class WebSocketConfig {
   // @Bean
    public ServerEndpointExporter serverEndpointExporter()  {
        return new ServerEndpointExporter();
    }

}