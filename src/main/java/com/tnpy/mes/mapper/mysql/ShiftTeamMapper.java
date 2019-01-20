package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ShiftTeam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ShiftTeamMapper {
    int deleteByPrimaryKey(String id);

    int insert(ShiftTeam record);

    int insertSelective(ShiftTeam record);

    ShiftTeam selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ShiftTeam record);

    int updateByPrimaryKey(ShiftTeam record);

    @Select(" select * from tb_shiftTeam where plantID = #{plantID}")
    List<ShiftTeam> selectByPlantID(String plantID);
}