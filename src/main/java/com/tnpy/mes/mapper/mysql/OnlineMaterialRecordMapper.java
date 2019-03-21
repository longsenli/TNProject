package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.OnlineMaterialRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface OnlineMaterialRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(OnlineMaterialRecord record);

    int insertSelective(OnlineMaterialRecord record);

    OnlineMaterialRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OnlineMaterialRecord record);

    int updateByPrimaryKey(OnlineMaterialRecord record);

    @Select("select * from tb_onlineMaterialRecord ${filter} ")
    List<OnlineMaterialRecord> getOnlineMaterialRecordByFilter(@Param("filter") String filter);
}