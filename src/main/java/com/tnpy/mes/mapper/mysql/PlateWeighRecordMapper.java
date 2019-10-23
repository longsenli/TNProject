package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PlateWeighRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PlateWeighRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(PlateWeighRecord record);

    int insertSelective(PlateWeighRecord record);

    PlateWeighRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PlateWeighRecord record);

    int updateByPrimaryKey(PlateWeighRecord record);

    @Select("select * from ${filter} limit 5000")
    List<PlateWeighRecord> selectByFilter(@Param("filter") String filter);

    @Select(" ${filter}")
    List<Map<Object,Object>> selectBasicData(@Param("filter") String filter);

    @Select(" select dayString,balanceID,materialName,Operator,round(avgWeight,2) as avgWeight,weightNum,qualifiedNum,overNum,lowNum,concat(round(qualifiedNum/weightNum * 100,2),'%') as qualifiedRate from (\n" +
            " select dayString,balanceID,materialName,Operator,count(1) as weightNum,avg(weight) as avgWeight,sum(if(ABS(centerWeight - weight) <= ${weighQualifyRange},1,0)) as qualifiedNum," +
            " sum(if(centerWeight - weight > ${weighQualifyRange},1,0)) as overNum,sum(if(centerWeight - weight < -${weighQualifyRange},1,0)) as lowNum from (\n" +
            " SELECT balanceID,materialName,Operator,weight,centerWeight,case  when hour(time) < 7 then DATE_FORMAT(date_add(time, interval -1 day), '%Y-%m-%d') else DATE_FORMAT(time, '%Y-%m-%d') end  as dayString FROM \n" +
            "  ${filter}  ) a group by dayString,balanceID,materialName,Operator order by dayString,balanceID,materialName ) b")
    List<Map<Object,Object>> QualifiedRateInfo(@Param("filter") String filter,@Param("weighQualifyRange") String weighQualifyRange);
}