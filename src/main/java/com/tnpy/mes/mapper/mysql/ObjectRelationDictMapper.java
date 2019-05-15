package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ObjectRelationDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ObjectRelationDictMapper {
    int deleteByPrimaryKey(String id);

    int insert(ObjectRelationDict record);

    int insertSelective(ObjectRelationDict record);

    ObjectRelationDict selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ObjectRelationDict record);

    int updateByPrimaryKey(ObjectRelationDict record);

    @Select("select previousObjectID from sys_objectrelationdict where nextObjectID = #{objectID} and objectType = #{typeID} and status != '-1'")
    List<String> selectPreviousObjectID(String objectID,String typeID);

    @Select("select nextObjectID from sys_objectrelationdict where previousObjectID  = #{objectID} and objectType = #{typeID} and status != '-1'")
    List<String> selectNextObjectID(String objectID,String typeID);
}