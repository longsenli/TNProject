package com.tnpy.mes.service.dataProvenanceService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.MaterialRecordMapper;
import com.tnpy.mes.model.customize.CustomMaterialRecord;
import com.tnpy.mes.service.dataProvenanceService.IDataProvenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/10 9:34
 */
@Service("dataProvenanceService")
public class dataProvenanceServiceImpl implements IDataProvenanceService {

    @Autowired
    private MaterialRecordMapper materialRecordMapper;

    public TNPYResponse getProvenanceByOrderID(String orderID )
    {
        TNPYResponse result = new TNPYResponse();
        List<String> expendOrderList = new ArrayList<>();
        expendOrderList.add(orderID);
        try
        {
            List<String> tmpOrderList = new ArrayList<>();
            for(int i =0 ;i< expendOrderList.size();i++)
            {
                tmpOrderList = materialRecordMapper.getOrderIDByExpendID(expendOrderList.get(i));

                if(tmpOrderList.isEmpty())
                    continue;
                for(int j =0 ;j<tmpOrderList.size();j++)
                {
                    expendOrderList.add(tmpOrderList.get(j));
                }
            }
            if(expendOrderList.size() == 1)
            {
                expendOrderList.add("-1");
            }
            System.out.println("==============" +expendOrderList.toString());
            List<CustomMaterialRecord> materialRecordList = materialRecordMapper.selectByExpendIDList(expendOrderList);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(materialRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
