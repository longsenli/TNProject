package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.Document;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DocumentMapper {
    int deleteByPrimaryKey(String id);


    int insert(Document record);

    int insertSelective(Document record);

    Document selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Document record);

    int updateByPrimaryKey(Document record);
@Insert("insert into tb_document (id,name,type,summary,location,creator,createTime) values(#{id},#{name},#{type},#{summary},#{location},#{creator},#{createTime})")
    int insertDocument(Document record);
  //  @Select("select * from tb_document  #{filter} ")
    List<Document> selectByFilter(@Param("filter") String filter);
}