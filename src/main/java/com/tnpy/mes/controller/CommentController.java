package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.CommentMapper;
import com.tnpy.mes.model.mysql.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/22 17:27
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;

    @RequestMapping(value = "/insertcomment")
    public TNPYResponse insertComment(@RequestBody String jsonStr) {

        Comment comment=(Comment) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), Comment.class);
        comment.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        comment.setCreatetime(new Date());

        TNPYResponse result = new TNPYResponse();
        try
        {
            if(commentMapper.insertSelective(comment)> 0)
            {
                result.setStatus(1);
                result.setMessage("插入成功！");
            }
            else
                result.setMessage("插入失败！");
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }
}
