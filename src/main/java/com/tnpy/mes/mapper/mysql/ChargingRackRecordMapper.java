package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ChargingRackRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ChargingRackRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChargingRackRecord record);

    int insertSelective(ChargingRackRecord record);

    ChargingRackRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChargingRackRecord record);

    int updateByPrimaryKey(ChargingRackRecord record);

    @Select("select * from tb_chargingrackrecord ${filter} ")
    List<ChargingRackRecord> selectByFilter(@Param("filter") String filter);

    @Select(" ( select '' as  id, plantID, processID, lineID,'总计' as workLocation, staffID,'' as  staffName, materialID, materialName, \n" +
            "     sum(productionNumber) as productionNumber,'' as  putonDate,sum(repairNumber) as repairNumber, repairID,'' as  repairName,'' as  repairTime,'' as  reason, \n" +
            "     sum(realNumber) as realNumber,'' as  materialType,'' as  pulloffStaffID,'' as  pulloffStaffName,'' as  pulloffDate,'' as  repairCombine, \n" +
            "    '' as  status,'' as  remark from tb_chargingrackrecord ${filter2}  group by materialID ) union all (select * from tb_chargingrackrecord ${filter}  limit 1000) ")
    List<ChargingRackRecord> selectByFilterWithSummary(@Param("filter") String filter,@Param("filter2") String filter2);
}