package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ProductionLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface ProductionLineMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProductionLine record);

    int insertSelective(ProductionLine record);

    ProductionLine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProductionLine record);

    int updateByPrimaryKey(ProductionLine record);

    @Select("select * from  sys_productionLine where processID = #{plantID} and plantID = #{processID}")
    List<ProductionLine> selectByPlantProcess(String plantID,String processID);
}