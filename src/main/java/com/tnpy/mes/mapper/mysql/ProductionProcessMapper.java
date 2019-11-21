package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ProductionProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ProductionProcessMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProductionProcess record);

    int insertSelective(ProductionProcess record);

    ProductionProcess selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProductionProcess record);

    int updateByPrimaryKey(ProductionProcess record);


    @Select("select * from sys_productionProcess where status != '-1' order by ordinal")
    List<ProductionProcess> selectAll();


    @Select("select id,name from tb_workContent ${filter}")
    List<Map<Object,Object>> getWorkContentDetail(@Param("filter") String filter);

}