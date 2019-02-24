package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialInventoryRecord;

public interface MaterialInventoryRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialInventoryRecord record);

    int insertSelective(MaterialInventoryRecord record);

    MaterialInventoryRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialInventoryRecord record);

    int updateByPrimaryKey(MaterialInventoryRecord record);
}