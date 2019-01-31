package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.Batchrelationcontrol;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BatchrelationcontrolMapper {
    int deleteByPrimaryKey(String id);

    int insert(Batchrelationcontrol record);

    int insertSelective(Batchrelationcontrol record);

    Batchrelationcontrol selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Batchrelationcontrol record);

    int updateByPrimaryKey(Batchrelationcontrol record);

    @Select("select tbBatch from tb_batchrelationcontrol where relationOrderID = #{orderID} limit 1")
    String selectTBBatchByOrderID(String orderID);
}