package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.Document;

public interface DocumentMapper {
    int deleteByPrimaryKey(String id);

    int insert(Document record);

    int insertSelective(Document record);

    Document selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Document record);

    int updateByPrimaryKey(Document record);
}