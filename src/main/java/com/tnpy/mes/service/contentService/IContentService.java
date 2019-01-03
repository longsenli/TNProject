package com.tnpy.mes.service.contentService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:08
 */
public interface IContentService {
    public TNPYResponse selectContent(int type , String summary, String creator, String startTime, String endTime);
    public TNPYResponse getContentType();
    public TNPYResponse insertContent( String jsonStr);
}
