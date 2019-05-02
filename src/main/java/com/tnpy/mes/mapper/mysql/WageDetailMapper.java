package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WageDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

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
            "select uuid(),inputPlantID,inputProcessID,'','', inputerID,inputer,materialNameInfo, materialID, productionNum,wage,productionNum*wage,now(),'system','system',now(), '1','' \n" +
            "from (select a.*,ifnull(b.wage,0) as wage from (select inputer,materialID,inputerID,materialNameInfo,sum(number) as productionNum,inputPlantID,inputProcessID \n" +
            "from tb_materialrecord where inputTime >= #{startTime} and inputTime <  #{endTime} and inputPlantID = #{plantID} \n" +
            "and inputProcessID = #{processID} and ifnull(status,0) < 5 group by inputerID,materialID ) a left join sys_material b on a.materialID = b.id ) c")
    int insertRecord(String plantID,String processID,String startTime,String endTime);
}