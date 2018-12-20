package com.tnpy.common.utils.token;

import com.tnpy.mes.mapper.mysql.TokenMapper;

import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/1914:31
 */
public class TokenUtil {
    private TokenMapper tokenmapper;
    //生成Token信息方法（根据有效的用户信息）
    public  Token creatToken(String userID) {
        Token token = new Token();
        token = new Token();
        token.setTokenid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        token.setToken(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        token.setBuildtime(System.currentTimeMillis());
        token.setUserid(userID);
        return  token;
    }

    public   boolean InsertToken(Token token)
    {
        try
        {
            tokenmapper.insertToken(token);
        }
        catch (Exception ex)
        {
            return  false;
        }
        return  true;
    }

    public   boolean UpdateToken(Token token)
    {
        try
        {
            tokenmapper.updateByPrimaryKey(token);
        }
        catch (Exception ex)
        {
            return  false;
        }
        return  true;
    }
}
