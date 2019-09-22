package com.tnpy.mes.service.wageManageService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.PayStubDetailMapper;
import com.tnpy.mes.mapper.mysql.RewardingPunishmentDetailMapper;
import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.mapper.mysql.WageDetailMapper;
import com.tnpy.mes.model.mysql.PayStubDetail;
import com.tnpy.mes.model.mysql.RewardingPunishmentDetail;
import com.tnpy.mes.service.wageManageService.IWageManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-09-04 9:46
 */
@Service("wageManageService")
public class WageManageServiceImpl implements IWageManageService {
    @Autowired
    private WageDetailMapper wageDetailMapper;

    @Autowired
    private RewardingPunishmentDetailMapper rewardingPunishmentDetailMapper;

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private PayStubDetailMapper payStubDetailMapper;

    public TNPYResponse getProductionWageDetail(String plantID, String processID, String staffName, String startTime, String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String staffFilter = "";
            if(!"-1".equals(staffName))
            {
                staffFilter = " and staffID = '" + staffName + "' ";
            }

            if(!"-1".equals(plantID))
            {
                staffFilter = " and plantID = '" + plantID + "' ";
            }

            if(!"-1".equals(processID))
            {
                staffFilter = " and processID = '" + processID + "' ";
            }

            List<Map<Object, Object>> mapList = wageDetailMapper.selectByFilterWithName(plantID,processID,startTime,endTime,staffFilter);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getRewardingPunishmentDetail(String plantID, String processID, String staffName, String startTime, String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String staffFilter = "";
            if(!"-1".equals(staffName))
            {
                staffFilter = " and staffID = '" + staffName + "' ";
            }

            if(!"-1".equals(plantID))
            {
                staffFilter = " and plantID = '" + plantID + "' ";
            }

            if(!"-1".equals(processID))
            {
                staffFilter = " and processID = '" + processID + "' ";
            }
            List<Map<Object, Object>> mapList = rewardingPunishmentDetailMapper.selectByFilter(plantID,processID,startTime,endTime,staffFilter);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeRewardingPunishmentDetail( String jsonStr )
    {
        RewardingPunishmentDetail rewardingPunishmentDetail=(RewardingPunishmentDetail) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), RewardingPunishmentDetail.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(rewardingPunishmentDetail.getId()))
            {
                rewardingPunishmentDetail.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                rewardingPunishmentDetail.setClosingdate(new Date());
                rewardingPunishmentDetail.setUpdatetime(new Date());
                rewardingPunishmentDetail.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                 List<Map<Object,Object>> userInfoList = userMapper.selecUserInfoByfilter(" industrialplant_id,productionprocess_id "," where userID = '" + rewardingPunishmentDetail.getStaffid() + "'");
                if(userInfoList.size() > 0)
                {
                    rewardingPunishmentDetail.setPlantid(userInfoList.get(0).get("industrialplant_id").toString());
                    rewardingPunishmentDetail.setProcessid(userInfoList.get(0).get("productionprocess_id").toString());
                }
                 rewardingPunishmentDetailMapper.insertSelective(rewardingPunishmentDetail);
            }
            else
            {
                rewardingPunishmentDetailMapper.updateByPrimaryKey(rewardingPunishmentDetail);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }
    public TNPYResponse deleteRewardingPunishmentDetail( String recordID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            rewardingPunishmentDetailMapper.deleteByPrimaryKey(recordID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }


    public TNPYResponse getPaystubDetail(String plantID, String processID, String staffName, String startTime, String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where closingDate >= '" + startTime + "' and closingDate <= '" + endTime + "' ";
            if(!"-1".equals(staffName))
            {
                filter += " and staffID = '" + staffName + "' ";
            }
            if(!"-1".equals(plantID))
            {
                filter  +=  " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                filter  +=  " and processID = '" + processID + "' ";
            }
            filter  +=  " order by staffName ";
            List<Map<Object, Object>> mapList = payStubDetailMapper.selectByFilter(filter);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changePaystubDetail( String jsonStr )
    {
        PayStubDetail payStubDetail=(PayStubDetail) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), PayStubDetail.class);

        if(payStubDetail.getPunishmentwage() > 0)
        {
            payStubDetail.setPunishmentwage(payStubDetail.getPunishmentwage() * (-1));
        }
        payStubDetail.setFinalwage(payStubDetail.getProductionwage() + payStubDetail.getPunishmentwage() + payStubDetail.getRewardingwage() + payStubDetail.getExtdwage1() + payStubDetail.getExtdwage2() + payStubDetail.getExtdwage3());
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(payStubDetail.getId()))
            {
                payStubDetail.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                payStubDetail.setClosingdate(new Date());
                payStubDetail.setUpdatetime(new Date());
                payStubDetail.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                List<Map<Object,Object>> userInfoList = userMapper.selecUserInfoByfilter(" industrialplant_id,productionprocess_id "," where userID = '" + payStubDetail.getStaffid() + "'");
                if(userInfoList.size() > 0)
                {
                    payStubDetail.setPlantid(userInfoList.get(0).get("industrialplant_id").toString());
                    payStubDetail.setProcessid(userInfoList.get(0).get("productionprocess_id").toString());
                }
                payStubDetailMapper.insertSelective(payStubDetail);
            }
            else
            {
                payStubDetail.setUpdatetime(new Date());
                payStubDetailMapper.updateByPrimaryKeySelective(payStubDetail);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("修改失败！" + ex.getMessage());
        }
        return  result;
    }
    public TNPYResponse deletePaystubDetail( String recordID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            payStubDetailMapper.deleteByPrimaryKey(recordID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
}
