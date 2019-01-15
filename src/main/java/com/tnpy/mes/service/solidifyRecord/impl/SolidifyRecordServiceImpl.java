package com.tnpy.mes.service.solidifyRecord.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.SolidifyRecordMapper;
import com.tnpy.mes.model.mysql.SolidifyRecord;
import com.tnpy.mes.service.solidifyRecord.ISolidifyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/15 13:19
 */
@Service("solidifyRecordService")
public class SolidifyRecordServiceImpl implements ISolidifyRecordService {
    @Autowired
    private SolidifyRecordMapper solidifyRecordMapper;

    public TNPYResponse getSolidifyRecordByRoom(String plantID,String roomID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<SolidifyRecord> solidifyRecordList = solidifyRecordMapper.selectByRoomID(plantID,roomID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(solidifyRecordList ,SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse addSolidifyRecord(String id,String status,String recorder,String roomID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = sdf.format(date);

            String updateStr = "solidifyRoomID = '" + roomID+"' ";
            int i  = Integer.valueOf(status).intValue();
            if(i-1 >= 1)
            {
                updateStr += " ,endtime" + (i-1) +" = '" + dateStr + "'";
            }
            if(i <4)
            {
                updateStr += " ,starttime" + i +" = '" + dateStr + "' ,recorder" + i +" = '" + recorder + "'";
            }

             solidifyRecordMapper.addSolidifyRecord(id,updateStr);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //result.setData(JSONObject.toJSON(solidifyRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
