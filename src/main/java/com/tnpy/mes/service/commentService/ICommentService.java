package com.tnpy.mes.service.commentService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:00
 */
public interface ICommentService {
    public TNPYResponse insertComment( String jsonStr);
    public TNPYResponse selectByContentID(String contentID);
}
