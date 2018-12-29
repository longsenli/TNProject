package com.tnpy.common.utils.token;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private com.tnpy.mes.mapper.mysql.TokenMapper TokenMapper;
    //提供查询
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {}
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {}
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2) throws Exception {
        System.out.println(request.getRequestURI());
        //普通路径放行
        if (true || "/api/login".equals(request.getRequestURI()) || "/api/downloadFile".equals(request.getRequestURI())) {
            return true;}


        //权限路径拦截
        final String headerToken = request.getHeader("token");
        //判断请求信息
        if(null==headerToken||headerToken.trim().equals("")){
//            resultWriter.write("你没有token,需要登录");
//            resultWriter.flush();
//            resultWriter.close();
            return false;
        }

        //解析Token信息
        try {
            String tokenStr = request.getHeader("Token");
            Token token=(Token) JSONObject.toJavaObject(JSONObject.parseObject(tokenStr), Token.class);
            //根据客户Token查找数据库Token
            Token myToken=TokenMapper.findByUserId(token.getUserid() );
            //数据库没有Token记录
            if(null==myToken) {
//               resultWriter.write("我没有你的token？,需要登录");
//                resultWriter.flush();
//                resultWriter.close();
                return false;
            }
            //数据库Token与客户Token比较
            if( !token.getToken().equals(myToken.getToken()) ){
//               resultWriter.write("你的token修改过？,需要登录");
//                resultWriter.flush();
//                resultWriter.close();
                return false;
            }
            //判断Token过期

            if(System.currentTimeMillis() - token.getBuildtime()>60*60*24*3){
//                resultWriter.write("你的token过期了？,需要登录");
//                resultWriter.flush();
//                resultWriter.close();
          //      return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
//            resultWriter.write("反正token不对,需要登录");
//            resultWriter.flush();
//            resultWriter.close();
            return false;
        }
        //最后才放行
        return true;
    }
}