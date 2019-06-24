package com.tnpy.mes.service.pushNotification;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/19 14:12
 */

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configurable
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

    }
    /*  正式使用时需要放开，与tomcat自身'serverEndpointExporter' 冲突
    @Bean
    public ServerEndpointExporter serverEndpointExporter()  {

        return new ServerEndpointExporter();
    }
*/
}