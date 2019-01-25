package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.SalesOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface SalesOrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(SalesOrder record);

    int insertSelective(SalesOrder record);

    SalesOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SalesOrder record);

    int updateByPrimaryKey(SalesOrder record);
    @Select("select * from tb_salesOrder ${filter} order by salecreatetime limit 1000 ")
    List<SalesOrder> selectByFilter(@Param("filter") String filter);

    @Select(" ${filter} ")
    List<Map<String, Object>> selectBySQL(@Param("filter") String filter);
}