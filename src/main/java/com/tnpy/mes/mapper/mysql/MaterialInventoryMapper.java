package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialInventory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MaterialInventoryMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialInventory record);

    int insertSelective(MaterialInventory record);

    MaterialInventory selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialInventory record);

    int updateByPrimaryKey(MaterialInventory record);
}