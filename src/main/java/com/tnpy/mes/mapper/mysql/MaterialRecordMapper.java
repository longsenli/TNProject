package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component

public interface MaterialRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialRecord record);

    int insertSelective(MaterialRecord record);

    MaterialRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialRecord record);

    int updateByPrimaryKey(MaterialRecord record);

    @Select("select * from tb_materialrecord where expendOrderID = #{expendOrder}")
    List<MaterialRecord> selectByExpendOrder(String expendOrder);

    @Select("select a.* from\n" +
            " (\n" +
            "SELECT * FROM tb_materialrecord where materialID in(\n" +
            "select inMaterialID from sys_materialrelation where outMaterialID = #{materialID}) and inOrOut = '1'\n" +
            ") a left join tb_workorder b on a.orderID = b.id  \n" +
            "where b.plantID = #{plantID}")
    List<MaterialRecord> selectUsableMaterial(String plantID,String materialID );


    int updateGainMaterialRecord(List<String> materialIDList, String expendOrderID, String outputer , Date outputTime);
}