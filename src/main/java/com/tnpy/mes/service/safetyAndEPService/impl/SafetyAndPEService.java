package com.tnpy.mes.service.safetyAndEPService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.IchnographyDetailInfoMapper;
import com.tnpy.mes.model.mysql.IchnographyDetailInfo;
import com.tnpy.mes.service.safetyAndEPService.ISafetyAndPEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-06-03 16:31
 */
@Service("safetyAndPEService")
public class SafetyAndPEService implements ISafetyAndPEService {

    @Autowired
    private IchnographyDetailInfoMapper ichnographyDetailInfoMapper;
    public TNPYResponse getIchnographyDetailInfo(String mainRegionID){
        TNPYResponse result = new TNPYResponse();
        try
        {
           List<IchnographyDetailInfo> ichnographyDetailInfoList = ichnographyDetailInfoMapper.selectByMinRegionID(mainRegionID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(ichnographyDetailInfoList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
