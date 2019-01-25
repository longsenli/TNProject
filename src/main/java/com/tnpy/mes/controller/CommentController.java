package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.commentService.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/22 17:27
 */
@RestController
@RequestMapping(value ="/api/comment")
public class CommentController {
    @Autowired
    ICommentService commentService;

    @RequestMapping(value = "/insertcomment")
    public TNPYResponse insertComment(@RequestBody String jsonStr) {

        return  commentService.insertComment(jsonStr);
    }

    @RequestMapping(value = "/selectbycontentid")
    public TNPYResponse selectByContentID( String contentID) {
        return  commentService.selectByContentID(contentID);
    }
}
