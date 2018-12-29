package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ProductionProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ProductionProcessMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProductionProcess record);

    int insertSelective(ProductionProcess record);

    ProductionProcess selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProductionProcess record);

    int updateByPrimaryKey(ProductionProcess record);


    @Select("select * from sys_productionProcess")
    List<ProductionProcess> selectAll();

}