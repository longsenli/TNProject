package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.UnqualifiedMaterialReturn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
@Component
public interface UnqualifiedMaterialReturnMapper {
    int deleteByPrimaryKey(String id);

    int insert(UnqualifiedMaterialReturn record);

    int insertSelective(UnqualifiedMaterialReturn record);

    UnqualifiedMaterialReturn selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UnqualifiedMaterialReturn record);

    int updateByPrimaryKey(UnqualifiedMaterialReturn record);

    @Select("select * from tb_unqualifiedmaterialreturn where returnTime >= #{startTime} and returnTime < #{endTime} and inputPlantID = #{plantID} and inputProcessID = #{processID} ${lineFilter} ")
    List<Map<String, String>> getByFilter(String startTime, String endTime, String plantID, String processID, @Param("lineFilter") String lineFilter);

    @Update("update tb_unqualifiedmaterialreturn set ${updateSet} where id = #{id}")
   int updateRepairNum(String id,@Param("updateSet") String updateSet);
}