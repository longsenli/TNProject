package com.tnpy.mes.service.PlasticService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.PlasticUsedRecordMapper;
import com.tnpy.mes.model.mysql.PlasticUsedRecord;
import com.tnpy.mes.service.PlasticService.IPlasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-08-19 10:45
 */
@Service("plasticService")
public class PlasticServiceImpl  implements IPlasticService {
    @Autowired
    private PlasticUsedRecordMapper plasticUsedRecordMapper;
    public TNPYResponse getPlasticUsedRecord(String plantID, String lineID, String locationID, String startTime, String endTime )
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where usedTime > '" + startTime + "' and usedTime < '" + endTime + "' ";
            if(!"-1".equals(plantID))
            {
                filter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }
            if(!"-1".equals(locationID))
            {
                filter += " and workLocation = '" + locationID + "' ";
            }
            List<PlasticUsedRecord> plasticUsedRecordList = plasticUsedRecordMapper.selectByParam(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plasticUsedRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse addPlasticUsedRecord(String listID,String userID,String userName,String plantID,String lineID,String locationID){
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<String, String>> grantResult = new  ArrayList<Map<String, String>>();
            String[] orderArray = listID.split("###");


            for(int i =0;i<orderArray.length;i++)
            {
                Map<String, String> mapResult = new HashMap<String, String>();
                mapResult.put("orderID",orderArray[i]);
                mapResult.put("status","失败");
                mapResult.put("returnMessage","失败!");

                try
                {
                    PlasticUsedRecord plasticUsedRecord = new PlasticUsedRecord();
                    plasticUsedRecord.setId(orderArray[i]);
                    plasticUsedRecord.setLineid(lineID);
                    plasticUsedRecord.setPlantid(plantID);
                    plasticUsedRecord.setWorklocation(locationID);
                    plasticUsedRecord.setStaffid(userID);
                    plasticUsedRecord.setStaffname(userName);
                    plasticUsedRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                    plasticUsedRecord.setUsedtime(new Date());
                    plasticUsedRecordMapper.insert(plasticUsedRecord);
                    mapResult.put("status","成功");
                    mapResult.put("returnMessage","成功!");

                }
                catch (Exception ex)
                {
                    mapResult.put("returnMessage",ex.getMessage());
                }

                grantResult.add(mapResult);
            }
            result.setData(JSONObject.toJSON(grantResult).toString());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("发放失败！" + ex.getMessage());
            return  result;
        }
    }
}
