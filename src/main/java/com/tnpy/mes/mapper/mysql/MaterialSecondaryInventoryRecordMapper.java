package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialSecondaryInventoryRecord;

public interface MaterialSecondaryInventoryRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialSecondaryInventoryRecord record);

    int insertSelective(MaterialSecondaryInventoryRecord record);

    MaterialSecondaryInventoryRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialSecondaryInventoryRecord record);

    int updateByPrimaryKey(MaterialSecondaryInventoryRecord record);
}