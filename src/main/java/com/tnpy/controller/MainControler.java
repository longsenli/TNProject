package com.tnpy.controller;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.dao.TokenMapper;
import com.tnpy.dao.UserMapper;
import com.tnpy.pojo.Token;
import com.tnpy.pojo.User;
import com.tnpy.token.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
//可以换成@RestController
@RequestMapping(value = "/tokentest/")
//方便拦截API路径下的URL
public class MainControler {
    private Logger logger = Logger.getLogger("rew");
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenMapper tokenmapper;
    @RequestMapping(value = "/index")
    public String showindex() {
        logger.warning("testdf");
        return "index";
    }
    @ResponseBody
    @RequestMapping(value = "/mainpage")
    public Result showmian() {
        logger.warning("testdf");
        System.out.println("调用成功！");
        Result result = new Result();
        //返回Token信息给客户端
        result.setFlag(true);
        result.setMsg("登录成功");
        result.setToken("1");
        return  result;
    }
    @RequestMapping(value = "/logintest")
    public String showlogin() {
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/login")
    public Result login(String name,String psd) {
        System.out.println("用户名 " +name +  " 密码 " + psd);
        User resqUser = new User();
        resqUser.setName(name);
        resqUser.setPassword(psd);
        //创建返回信息对象
        Result result = new Result();
        //判断用户信息为空
        if ("".equals(resqUser.getName()) || "".equals(resqUser.getPassword())) {
            result.setMsg("传入的用户名/密码为空！");
            return result;
        }
        //根据客户用户名查找数据库的usre对象
        User myUser = userMapper.findByUserName(resqUser.getName());

        //判断用户不存在
        if (null == myUser) {
            result.setMsg("用户不存在");
            return result;
        }
        //判断用户不存在
        if (!resqUser.getPassword().equals(myUser.getPassword())) {
            result.setMsg("密码不正确");
            return result;
        }

        //根据数据库的用户信息查询Token
        Token token = tokenmapper.findByUserId(myUser.getUserid());

        long nowtime = System.currentTimeMillis();
        //生成Token

        if (null == token) {
            System.out.println("第一次登陆");
            //第一次登陆
            token = creatToken(myUser);
            System.out.println("第一次登陆");
            tokenmapper.insert2(token);
        }else{
            //登陆就更新Token信息
//            System.out.println("更新登陆");
//            token =  creatToken(myUser);
//            System.out.println("更新登陆");
//            tokenmapper.updateByPrimaryKey(token);
        }
        //返回Token信息给客户端
        result.setFlag(true);
        result.setMsg("登录成功");
        result.setToken( JSONObject.toJSON(token).toString());
        return result;
    }
    //生成Token信息方法（根据有效的用户信息）
    private Token creatToken(User user) {
        Token token = new Token();
        token = new Token();
        token.setTokenid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        token.setToken(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        token.setBuildtime(System.currentTimeMillis());
        token.setUserid(user.getUserid());
        return  token;
    }
}
