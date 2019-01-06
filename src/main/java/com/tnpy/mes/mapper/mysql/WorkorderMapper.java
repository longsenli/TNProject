package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.customize.CustomWorkOrderRecord;
import com.tnpy.mes.model.mysql.Workorder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WorkorderMapper {
    int deleteByPrimaryKey(String id);

    int insert(Workorder record);

    int insertSelective(Workorder record);

    Workorder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Workorder record);

    int updateByPrimaryKey(Workorder record);

    @Select("select * from tb_workOrder")
    List<Workorder> selectAll();

    @Select("select * from tb_workOrder ${filter}")
    List<Workorder> selectByFilter(@Param("filter") String filter);

    List<CustomWorkOrderRecord> selectCustomResultByFilter(@Param("filter") String filter);
}