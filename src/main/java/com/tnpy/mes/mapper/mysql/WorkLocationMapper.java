package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WorkLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface WorkLocationMapper {
    int deleteByPrimaryKey(String id);

    int insert(WorkLocation record);

    int insertSelective(WorkLocation record);

    WorkLocation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WorkLocation record);

    int updateByPrimaryKey(WorkLocation record);

    @Select("select * from tb_worklocation ${filter}")
    List<WorkLocation> selectByFilter(@Param("filter") String filter);

    @Select("SELECT a.id as lineID,'-1' as workLocationID,a.name,a.processID,a.plantID,b.id as contentID,b.name as contentName FROM tb_worklocation a left join tb_workcontent b \n" +
            "on a.plantID = b.plantID and a.processID = b.processID where a.id = #{qrCode}")
    List<Map<Object,Object>> selectByQRCode( String qrCode);
}