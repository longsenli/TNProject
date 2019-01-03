package com.tnpy.mes.service.commentService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.CommentMapper;
import com.tnpy.mes.model.mysql.Comment;
import com.tnpy.mes.service.commentService.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:01
 */
@Service("commentService")
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public TNPYResponse insertComment( String jsonStr) {

        Comment comment=(Comment) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), Comment.class);
        comment.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        comment.setCreatetime(new Date());

        TNPYResponse result = new TNPYResponse();
        try
        {
            if(commentMapper.insertSelective(comment)> 0)
            {
                result.setMessage("插入成功！");
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

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

    @Override
    public TNPYResponse selectByContentID(String contentID) {

        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Comment> comments = commentMapper.selectByContentID(contentID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(comments).toString());
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }
}
