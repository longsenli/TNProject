package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WageDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface WageDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(WageDetail record);

    int insertSelective(WageDetail record);

    WageDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WageDetail record);

    int updateByPrimaryKey(WageDetail record);

    @Insert("insert into tb_wagedetail\n" +
            "select uuid(),inputPlantID,inputProcessID,'','', inputerID,inputer, materialID,materialNameInfo, productionNum,wage,productionNum*wage,date_add(now(), interval -1 day),'system','system',now(), '1','' \n" +
            "from (select a.*,ifnull(b.wage,0) as wage from (select inputer,materialID,inputerID,materialNameInfo,sum(number) as productionNum,inputPlantID,inputProcessID \n" +
            "from tb_materialrecord where inputTime >= #{startTime} and inputTime <  #{endTime} and inputPlantID = #{plantID} \n" +
            "and inputProcessID = #{processID} and ifnull(status,0) < 5 group by inputerID,materialID ) a left join sys_material b on a.materialID = b.id ) c")
    int insertRecord(String plantID,String processID,String startTime,String endTime);

    @Select(" ( select staffName,sum(wage) as wage ,'' as materialName,'' as productionNumber ,'' as unitPrice,'总计' as closingDate from tb_wagedetail where plantID = #{plantID} and processID = #{processID} and " +
            "closingDate >= #{startTime} and closingDate <= #{endTime} group by staffName order by staffName limit 1000 ) " +
            "union all" +
            " ( select staffName,wage,materialName,productionNumber,CONCAT(unitPrice,'') as unitPrice,CONCAT(closingDate,'') as closingDate  from tb_wagedetail where plantID = #{plantID} and processID = #{processID} " +
            "and closingDate >= #{startTime} and closingDate <= #{endTime} order by closingDate,staffName limit 3000)")
    List<Map<Object, Object>> selectByFilter(String plantID,String processID,String startTime,String endTime);
}