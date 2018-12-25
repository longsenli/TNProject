package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.ContentMapper;
import com.tnpy.mes.mapper.mysql.ContentTypeMapper;
import com.tnpy.mes.model.mysql.Content;
import com.tnpy.mes.model.mysql.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/21 11:05
 */
@RestController
@RequestMapping("/api/content")
public class ContentContoller {
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private ContentTypeMapper contentTypeMapper;
    @ResponseBody
    @RequestMapping(value = "/selectcontent")
    public TNPYResponse selectContent(int type ,String summary,String creator,String startTime,String endTime) {
        TNPYResponse response = new TNPYResponse();
        StringBuffer filter = new StringBuffer();
        try {
            if(type > 0 )
            {
                filter.append(" and type =" + type + " ");
            }
            if(!StringUtils.isEmpty(summary))
            {
                filter.append(" and title like '%" + summary + "%' ");
            }
            if(!StringUtils.isEmpty(creator))
            {
                filter.append(" and creator like '%" + creator + "%' ");
            }
            if(!StringUtils.isEmpty(startTime))
            {
                filter.append(" and createTime >= '" + startTime + "' ");
            }

            if(!StringUtils.isEmpty(endTime))
            {
                filter.append(" and createTime <= '" + endTime + "' ");
            }
            String str  = filter.toString();
            if(!StringUtils.isEmpty(str))
            {
                str = " where " + str.substring(4);
            }
            str += " " + "order by createTime desc";
            List<Content> contentList = contentMapper.selectByFilter(str);
            response.setStatus(1);
            response.setData(JSONObject.toJSON(contentList).toString());
            return response;
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
            response.setMessage("查询失败！" + e.getMessage());
            return response;
        }
    }


    @RequestMapping(value = "/getcontenttype")
    public TNPYResponse getContentType() {

        TNPYResponse result = new TNPYResponse();
        try
        {
            List<ContentType> contentType = contentTypeMapper.selectAllDict();
            result.setStatus(1);
            result.setData(JSONObject.toJSON(contentType).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }

    }

    @RequestMapping(value = "/insertcontent")
    public TNPYResponse insertContent(@RequestBody String jsonStr) {

        Content content=(Content) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), Content.class);
        content.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        content.setCreatetime(new Date());

        TNPYResponse result = new TNPYResponse();
        try
        {
            if(contentMapper.insertSelective(content)> 0)
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
