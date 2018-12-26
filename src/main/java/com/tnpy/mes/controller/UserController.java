package com.tnpy.mes.controller;

import com.tnpy.common.utils.encryption.Encryption;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.model.mysql.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/26 8:22
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    TbUserMapper tbUserMapper;
    @PostMapping(value = "/changepsw")
    public TNPYResponse changePsw(String oldPsw,String newPsw,String userID) {
        TNPYResponse tnpyResponse = new TNPYResponse();
        Encryption encryption = new Encryption();
        try{
            TbUser myUser = tbUserMapper.selectByPrimaryKey(userID);
            if (!oldPsw.trim().equals(myUser.getPassword()) && !encryption.encrypt(oldPsw,null).equals(myUser.getPassword())) {
                tnpyResponse.setMessage("密码不正确");
                return tnpyResponse;
            }
            myUser.setPassword(encryption.encrypt(newPsw,null));
            if(tbUserMapper.updateByPrimaryKeySelective(myUser)> 0)
            {
                tnpyResponse.setStatus(1);
                tnpyResponse.setMessage("修改成功！");
                return tnpyResponse;
            }
            tnpyResponse.setMessage("修改失败！");
            return tnpyResponse;
        }
        catch (Exception ex)
        {
            tnpyResponse.setMessage(ex.getMessage());
            return  tnpyResponse;
        }

    }

}
