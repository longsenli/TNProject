package com.tnpy.common.utils.SMSManage;

import com.mascloud.sdkclient.Client;

import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-09-15 9:05
 */
public  class SMSTN {
    public  static   Client client ;
    public static  boolean SMSLogin()
    {
        if(client == null)
        {
            client =  Client.getInstance();
           return client.login("http://mas.ecloud.10086.cn/app/sdk/login", "masadmin", "masadmin","天能集团（河南）能源科技有限公司");
        }
        return  true;
    }
    public  static void sendMessage(String[] phoneNumList,String contentMessage)
    {
//        try {
//            final Client client =  Client.getInstance();
//            // 正式环境IP，登录验证URL，用户名，密码，集团客户名称
//            //client.login("http://mas.ecloud.10086.cn/app/sdk/login", "SDK账号名称（不是页面端账号）", "密码","集团客户名称");
//            client.login("http://mas.ecloud.10086.cn/app/sdk/login", "masadmin", "masadmin","天能集团（河南）能源科技有限公司");
//            // 测试环境IP
//            //client.login("http://112.33.1.13/app/sdk/login", "sdk2", "123","光谷信息");
//            int sendResult = client. sendDSMS (new String[] {"15539392921"},
//                    "sdk短信发送内容测试", "",  1,"LgMVurUH", UUID.randomUUID().toString(),true);
//
//            System.out.println("推送结果: " + sendResult);
//            client.logout();
//
//        }
//        catch (Exception ex)
//        {
//            System.out.println(ex.getMessage() + "============");
//        }

        try {
            SMSLogin();

             client. sendDSMS (phoneNumList,contentMessage, "",  1,"LgMVurUH", UUID.randomUUID().toString(),true);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage() + "============");
        }
    }

}
