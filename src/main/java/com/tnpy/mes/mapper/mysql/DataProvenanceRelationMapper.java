package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DataProvenanceRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DataProvenanceRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(DataProvenanceRelation record);

    int insertSelective(DataProvenanceRelation record);

    DataProvenanceRelation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DataProvenanceRelation record);

    int updateByPrimaryKey(DataProvenanceRelation record);

    @Select(" select * from tb_dataprovenancerelation ${filter} ")
    List<DataProvenanceRelation> selectByFilter(@Param("filter") String filter);

    @Select(" select inSubOrderID from tb_dataprovenancerelation where outSubOrderID  in ( ${outOrderIDList} ) ")
    List<String> selectInOrderIDByOutOrderID(@Param("outOrderIDList") String outOrderIDList);

    @Select(" select outSubOrderID from tb_dataprovenancerelation where inSubOrderID  in ( ${inOrderIDList} ) ")
    List<String> selectOutOrderIDByInOrderID(@Param("inOrderIDList") String inOrderIDList);
}