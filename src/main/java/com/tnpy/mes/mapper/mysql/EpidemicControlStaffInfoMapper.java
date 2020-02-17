package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.EpidemicControlStaffInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface EpidemicControlStaffInfoMapper {
    int deleteByPrimaryKey(String identityno);

    int insert(EpidemicControlStaffInfo record);

    int insertSelective(EpidemicControlStaffInfo record);

    EpidemicControlStaffInfo selectByPrimaryKey(String identityno);

    int updateByPrimaryKeySelective(EpidemicControlStaffInfo record);

    int updateByPrimaryKey(EpidemicControlStaffInfo record);

    @Select(" select * " +
            " from tb_epidemiccontrolstaffinfo where name like  #{name}  ")
    List<Map<Object, Object>> getStaffEpidemicBasicInfoByName( String name);
}