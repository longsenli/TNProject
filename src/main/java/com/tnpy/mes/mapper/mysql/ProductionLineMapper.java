package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ProductionLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ProductionLineMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProductionLine record);

    int insertSelective(ProductionLine record);

    ProductionLine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProductionLine record);

    int updateByPrimaryKey(ProductionLine record);
    @Select("select * from  sys_productionLine where plantID = #{plantID} and processID = #{processID} order by ordinal asc ")
    List<ProductionLine> selectByPlantProcess(String plantID, String processID);

    @Select("select id,name from  sys_productionLine where status != '-1' ")
    List<Map<Object,Object>> getAllProductionLine();

    @Select("SELECT a.id as lineID,'-1' as workLocationID,a.name,a.processID,a.plantID,b.id as contentID,b.name as contentName FROM sys_productionline a left join tb_workcontent b \n" +
            "on a.plantID = b.plantID and a.processID = b.processID  where a.id = #{qrCode} ")
    List<Map<Object,Object>> selectByQRCode(String qrCode);

}