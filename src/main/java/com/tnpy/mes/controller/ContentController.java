package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.contentService.IContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/21 11:05
 */
@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private IContentService contentService ;

    @ResponseBody
    @RequestMapping(value = "/selectcontent")
    public TNPYResponse selectContent(int type ,String summary,String creator,String startTime,String endTime) {
        return  contentService.selectContent(type,summary,creator,startTime,endTime);
    }


    @RequestMapping(value = "/getcontenttype")
    public TNPYResponse getContentType() {
        return  contentService.getContentType();
    }

    @RequestMapping(value = "/insertcontent")
    public TNPYResponse insertContent(@RequestBody String jsonStr) {
        return  contentService.insertContent(jsonStr);
    }


}
