package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ChargingRackRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    @Select("select a.onRackNumber,a.workLocation,b.name,b.describeInfo from ( select sum(productionNumber) as onRackNumber,workLocation FROM tb_chargingrackrecord where workLocation = #{workLocationID} and status = '1' and pulloffStaffName is null ) a \n" +
            "left join tb_worklocation b on a.workLocation = b.id ")
    List<Map<Object,Object>> selectOnRackNumber(String workLocationID);

    @Select(" ( select '' as  id, plantID, processID, lineID,'总计' as workLocation, staffID,'' as  staffName, materialID, materialName, \n" +
            "     sum(productionNumber) as productionNumber,'' as  putonDate,sum(repairNumber) as repairNumber, repairID,'' as  repairName,'' as  repairTime,'' as  reason, \n" +
            "     sum(realNumber) as realNumber,'' as  materialType,'' as  pulloffStaffID,'' as  pulloffStaffName,'' as  pulloffDate,'' as  repairCombine, \n" +
            "    '' as  status,'' as  remark from tb_chargingrackrecord ${filter2}  group by materialID ) union all (select * from tb_chargingrackrecord ${filter}  limit 1000) ")
    List<ChargingRackRecord> selectByFilterWithSummary(@Param("filter") String filter,@Param("filter2") String filter2);


    @Select("( select  '' as  id, plantID, processID, lineID,'总计' as workLocation, staffID,'' as  staffName, materialID, materialName, " +
            "  sum(productionNumber) as productionNumber,  putonDate,sum(repairNumber) as repairNumber, repairID,'' as  repairName,'' as  repairTime,'' as  reason," +
            " sum(realNumber) as realNumber,  materialType,'' as  pulloffStaffID,'' as  pulloffStaffName,'' as  pulloffDate,'' as  repairCombine," +
            "  '' as  status,'' as  remark from tb_chargingrackrecord ${filter}  group by putonDate,materialName,materialType order by putonDate,materialName  limit 1000 ) " +
            " union all ( select * from tb_chargingrackrecord ${filter}  order by workLocation, putonDate asc  limit 1000) ")
    List<ChargingRackRecord> selectByFilterOnRack(@Param("filter") String filter);
    
    @Select(" SELECT id, plantID, processID, lineID, workLocation, staffID, staffName, materialID, materialName, productionNumber, "
    		+ "putonDate, repairNumber, repairID, repairName, repairTime, reason, realNumber, materialType, pulloffStaffID, pulloffStaffName, "
    		+ "IFNULL(DATE_FORMAT(pulloffDate,'%Y-%m-%d'),'') pulloffDate, repairCombine, STATUS, remark FROM tb_chargingrackrecord t WHERE t.id = #{filter}")
    ChargingRackRecord selectByFilterForCancel(@Param("filter") String filter);
    
}