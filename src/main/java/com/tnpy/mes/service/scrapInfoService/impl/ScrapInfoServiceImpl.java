package com.tnpy.mes.service.scrapInfoService.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.WorkOrderScrapInfoMapper;
import com.tnpy.mes.model.mysql.Material;
import com.tnpy.mes.model.mysql.WorkOrderScrapInfo;
import com.tnpy.mes.service.scrapInfoService.IScrapInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/29 9:10
 */
@Service("scrapinfoservice")
public class ScrapInfoServiceImpl implements IScrapInfoService {
    @Autowired
    private WorkOrderScrapInfoMapper workOrderScrapInfoMapper;
    public TNPYResponse getScrapInfo(String plantID , String processID,String lineID, String startTime, String endTime )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<WorkOrderScrapInfo> workOrderScrapInfoList = workOrderScrapInfoMapper.selectByParams(plantID,processID,lineID.trim(),startTime,endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderScrapInfoList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse saveScrapInfo(String strJson )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            //System.out.println(strJson);
            JSONObject jso = JSONObject.parseObject(strJson);
            Map<String, String> jsonMap =  JSONObject.toJavaObject(jso, Map.class);
            List<WorkOrderScrapInfo> workOrderScrapInfoList = new ArrayList<>();

            for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
                if(!entry.getKey().contains("###"))
                    continue;
                if(StringUtils.isEmpty(entry.getValue()))
                    continue;
                WorkOrderScrapInfo workOrderScrapInfo = new WorkOrderScrapInfo();
                workOrderScrapInfo.setOrderid(jsonMap.get("orderid"));
                workOrderScrapInfo.setMaterialid(entry.getKey().split("###")[0]);
                workOrderScrapInfo.setMaterialname(entry.getKey().split("###")[1]);
                workOrderScrapInfo.setOrdername(jsonMap.get("ordername"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                workOrderScrapInfo.setOrdertime( formatter.parse(jsonMap.get("ordertime")));
                workOrderScrapInfo.setValue(Float.parseFloat(entry.getValue()));
                workOrderScrapInfo.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                workOrderScrapInfoList.add(workOrderScrapInfo);
            }
            workOrderScrapInfoMapper.insertManyOrderScrap(workOrderScrapInfoList,jsonMap.get("orderid"));
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("报废出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getMaterialScrapInfo( String materialID, String orderID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Material> materialList = workOrderScrapInfoMapper.getMaterialScrapInfo(materialID,orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(materialList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
