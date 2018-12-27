package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WorkOrder;
import com.tnpy.mes.model.mysql.WorkOrderKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WorkOrderMapper {
    int deleteByPrimaryKey(WorkOrderKey key);

    int insert(WorkOrder record);

    int insertSelective(WorkOrder record);

    WorkOrder selectByPrimaryKey(WorkOrderKey key);

    int updateByPrimaryKeySelective(WorkOrder record);

    int updateByPrimaryKey(WorkOrder record);

    @Select("select * from tb_workOrder")
    List<WorkOrder> selectAll();
}