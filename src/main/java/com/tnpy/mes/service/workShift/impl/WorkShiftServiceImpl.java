package com.tnpy.mes.service.workShift.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.ShiftTeamMapper;
import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.mapper.mysql.WorkShiftMapper;
import com.tnpy.mes.mapper.mysql.WorkShiftRecordMapper;
import com.tnpy.mes.model.mysql.ShiftTeam;
import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.mes.model.mysql.WorkShift;
import com.tnpy.mes.model.mysql.WorkShiftRecord;
import com.tnpy.mes.service.workShift.IWorkShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/20 9:05
 */
@Service("workShiftService")
public class WorkShiftServiceImpl implements IWorkShiftService {
    @Autowired
    private ShiftTeamMapper shiftTeamMapper;

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private WorkShiftMapper workShiftMapper;

    @Autowired
    private WorkShiftRecordMapper workShiftRecordMapper;

    public TNPYResponse getShiftTeam(String plantID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<ShiftTeam> shiftTeamList = shiftTeamMapper.selectByPlantID(plantID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(shiftTeamList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeShiftTeam( String jsonStr ) {
        TNPYResponse result = new TNPYResponse();
        try {
            ShiftTeam shiftTeam = (ShiftTeam) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), ShiftTeam.class);
            TbUser user = tbUserMapper.selectByPrimaryKey(shiftTeam.getStaffid());
            if(null == user )
            {
                result.setMessage(shiftTeam.getStaffid() + " ，该工号错误！" );
                return  result;
            }
            shiftTeam.setStaffname(user.getName());
            if (StringUtils.isEmpty(shiftTeam.getId())) {
                shiftTeam.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                shiftTeam.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");

                shiftTeamMapper.insertSelective(shiftTeam);
            } else {
                shiftTeamMapper.updateByPrimaryKey(shiftTeam);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return result;
        }
        catch (Exception ex)
        {
            result.setMessage("修改出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deleteShiftTeam( String id )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
           shiftTeamMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getWorkShiftDetail(String plantID,String shiftTeamID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<WorkShift> workShiftList = workShiftMapper.getWorkShiftDetail(plantID,shiftTeamID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workShiftList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeWorkShift( String jsonStr )
    {
        TNPYResponse result = new TNPYResponse();
        try {
            WorkShift workShift = (WorkShift) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), WorkShift.class);
            TbUser user = tbUserMapper.selectByPrimaryKey(workShift.getStaffid());
            if(null == user )
            {
                result.setMessage(workShift.getStaffid() + " ，该工号错误！" );
                return  result;
            }
            workShift.setStaffname(user.getName());
            if (StringUtils.isEmpty(workShift.getId())) {
                workShift.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                workShift.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");

                workShiftMapper.insertSelective(workShift);
            } else {
                workShiftMapper.updateByPrimaryKey(workShift);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return result;
        }
        catch (Exception ex)
        {
            result.setMessage("修改出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deleteWorkShift( String id )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            workShiftMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getWorkShiftRecord(String plantID,String processID,String workShift,String dayNight )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<WorkShiftRecord> workShiftRecordList = workShiftRecordMapper.getWorkShiftRecord(plantID,processID,workShift, dayNight);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workShiftRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeWorkShiftRecord( String jsonStr )
    {
        TNPYResponse result = new TNPYResponse();
        try {
            WorkShiftRecord workShiftRecord = (WorkShiftRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), WorkShiftRecord.class);
            TbUser user = tbUserMapper.selectByPrimaryKey(workShiftRecord.getStaffid());
            if(null == user )
            {
                result.setMessage(workShiftRecord.getStaffid() + " ，该工号错误！" );
                return  result;
            }
            workShiftRecord.setStaffname(user.getName());
            if (StringUtils.isEmpty(workShiftRecord.getId())) {
                workShiftRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                workShiftRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");

                workShiftRecordMapper.insertSelective(workShiftRecord);
            } else {
                workShiftRecordMapper.updateByPrimaryKey(workShiftRecord);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return result;
        }
        catch (Exception ex)
        {
            result.setMessage("修改出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deleteWorkShiftRecord( String id )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            workShiftRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
