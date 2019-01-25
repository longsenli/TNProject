package com.tnpy.mes.service.emailSendService.impl;

import com.tnpy.mes.service.emailSendService.IEmailSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/25 8:20
 */
@Service("emailSendService")
public class EmailSendServiceImpl implements IEmailSendService {
    @Autowired
    private JavaMailSender jms;

    public String send()
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        //谁发的
        mailMessage.setFrom("llsbenign@163.com");
        //发给谁
        mailMessage.setTo("631620498@qq.com");
        //标题
        mailMessage.setSubject("我");
        //内容
        mailMessage.setText("逗你玩");
        jms.send(mailMessage);
        return "1";
    }

}
