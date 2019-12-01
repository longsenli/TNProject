package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.SolidifyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface SolidifyRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(SolidifyRecord record);

    int insertSelective(SolidifyRecord record);

    SolidifyRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SolidifyRecord record);

    int updateByPrimaryKey(SolidifyRecord record);


    @Select("select a.* from (select * from tb_solidifyrecord where solidifyRoomID = #{roomID} or solidifyRoomID is null) a" +
            " left join tb_workorder b on a.orderID = b.id  where b.plantID =  #{plantID} order by starttime1 desc limit 1000")
    List<SolidifyRecord> selectByRoomID(String plantID, String roomID);

    @Update("update tb_solidifyrecord set  ${value} where id = #{id}")
    int addSolidifyRecord(String id,@Param("value") String value);

    @Select("select * from tb_solidifyrecord  ${filter} limit 1000")
    List<SolidifyRecord> getSolidifyRecordByFilter(@Param("filter") String filter);

    @Update(" update tb_solidifyrecord  ${value} where ${filter} ")
    int changeSolidifyStatus(@Param("value") String value,@Param("filter") String filter);

    @Select("select * from tb_solidifyrecord  where orderID = #{orderID} ")
    List<SolidifyRecord>  selectByOrderID(String orderID);


    @Select(" (select '' as id,count(1) as orderID,sum( productionNum) as productionNum,'' as  plantID,'' as  materialID, materialName, solidifyRoomID, '' as recorder1, \n" +
            "    '' as starttime1,'' as  endtime1, '' as recorder2, '' as starttime2,'' as  endtime2,'' as  recorder3, '' as starttime3, '' as endtime3, \n" +
            "     status,'' as  remark, solidifyRoomName,'' as  outOperator from tb_solidifyrecord  ${filter} group by solidifyRoomID,materialName,status order by solidifyRoomID,status limit 1000 )" +
            " union all ( select * from tb_solidifyrecord  ${filter} order by solidifyRoomID,status limit 1000 ) ")
    List<SolidifyRecord>  selectInSolidifyRecord(@Param("filter") String filter);

    @Select("select count(1) from tb_solidifyrecord where solidifyRoomID = #{roomID} and status =#{status} ")
    int selectInNumber(String roomID,String status);

    @Select("select a.subOrderID as orderID ,CONCAT(a.materialNameInfo,'  ',a.number)  as status ,CONCAT(date_format(a.inputTime, '%Y-%m-%d %H:%i:%s'),' ' ,a.inputer ) as returnMessage  from tb_materialrecord  a left join \n" +
            "  tb_solidifyrecord  b on a.subOrderID = b.id where inputTime > #{startTime}  and inputTime <#{endTime} and inputProcessID = #{processID} and inputPlantID = #{plantID} and b.orderID is null ")
    List<Map<Object, Object>>  uninputSolidifyRoom(String plantID ,String processID , String startTime, String endTime );

//    @Select("( select a.materialName,a.solidifyRoomID,a.total1D,b.planDailyProduction as total2D,a.total1D/ifnull(b.planDailyProduction,1) as total3D from ( \n" +
//            "SELECT materialName,'物料周期' as solidifyRoomID,sum( productionNum) as total1D  FROM tb_solidifyrecord  \n" +
//            "where status < '9' and  plantID = #{plantID}  group by materialName  ) a left join  \n" +
//            "(select * from tb_planproductionrecord where plantID =#{plantID} and planMonth = #{monthStr}) b  on a.materialName = b.materialName order by a.materialName limit 100) \n" +
//            "union all\n" +
//            "(SELECT materialName,'分段总计' as solidifyRoomID,sum( if( status = '1' , productionNum, 0)) as total1D,  sum( if( status = '2' , productionNum, 0)) as total2D ,  \n" +
//            "sum( if( status = '3' , productionNum, 0)) as total3D   FROM tb_solidifyrecord  \n" +
//            "where status < '9' and  plantID = #{plantID}  group by materialName limit 100)\n" +
//            "union all\n" +
//            "( select m.materialName,n.name,m.total1D,m.total2D,m.total3D from ( SELECT materialName,solidifyRoomID,sum( if( status = '1' , productionNum, 0)) as total1D,  sum( if( status = '2' , productionNum, 0)) as total2D ,  \n" +
//            "sum( if( status = '3' , productionNum, 0)) as total3D   FROM tb_solidifyrecord  \n" +
//            "where status < '9' and  plantID = #{plantID} group by materialName,solidifyRoomID order by solidifyRoomID  ) m left join sys_productionline n on m.solidifyRoomID = n.id limit 1000)")
@Select(  "(SELECT materialName,'分段总计' as solidifyRoomID,sum( if( status = '1' , productionNum, 0)) as total1D,  sum( if( status = '2' , productionNum, 0)) as total2D ,  \n" +
        "sum( if( status = '3' , productionNum, 0)) as total3D   FROM tb_solidifyrecord  \n" +
        "where status < '9' and  plantID = #{plantID}  group by materialName limit 100)\n" +
        "union all\n" +
        "( select m.materialName,n.name,m.total1D,m.total2D,m.total3D from ( SELECT materialName,solidifyRoomID,sum( if( status = '1' , productionNum, 0)) as total1D,  sum( if( status = '2' , productionNum, 0)) as total2D ,  \n" +
        "sum( if( status = '3' , productionNum, 0)) as total3D   FROM tb_solidifyrecord  \n" +
        "where status < '9' and  plantID = #{plantID} group by materialName,solidifyRoomID order by solidifyRoomID  ) m left join sys_productionline n on m.solidifyRoomID = n.id limit 1000)")
    List<Map<Object, Object>>  getSolidifyRoomDetail(String plantID ,String monthStr );


    @Select(  "select solidifyRoomID,solidifyRoomName , materialName ,sum(totalNumber)  as totalNumber ,sum(totalProduction)  as totalProduction, sum(phaseNumber1)  as phaseNumber1 ,sum(phaseProduction1) as phaseProduction1,\n" +
            "sum(phaseNumber2) as phaseNumber2, sum(phaseProduction2) as phaseProduction2,sum(phaseNumber3) as phaseNumber3, sum(phaseProduction3) as phaseProduction3,  sum(phaseNumber4) as phaseNumber4, sum(phaseProduction4) as phaseProduction4\n" +
            "from (\n" +
            "(select solidifyRoomID,solidifyRoomName , materialName ,count(1)  as totalNumber ,sum(productionNum)  as totalProduction, 0  as phaseNumber1 ,0 as phaseProduction1,\n" +
            " 0 as phaseNumber2, 0 as phaseProduction2, 0 as phaseNumber3, 0 as phaseProduction3, 0 as phaseNumber4, 0 as phaseProduction4\n" +
            "from tb_solidifyrecord where  status != '9' and plantID = #{plantID} ${lineFilter} group by materialName,solidifyRoomID )\n" +
            "union all\n" +
            "(select solidifyRoomID,solidifyRoomName , materialName ,0  as totalNumber ,0 as totalProduction, count(1)  as phaseNumber1 ,sum(productionNum)  as phaseProduction1,\n" +
            " 0 as phaseNumber2, 0 as phaseProduction2, 0 as phaseNumber3, 0 as phaseProduction3, 0 as phaseNumber4, 0 as phaseProduction4\n" +
            "from tb_solidifyrecord where starttime1 > #{startTime} and starttime1 < #{endTime} and plantID = #{plantID} ${lineFilter} group by materialName,solidifyRoomID )\n" +
            "union all\n" +
            "(select solidifyRoomID,solidifyRoomName , materialName ,0  as totalNumber ,0 as totalProduction,0  as phaseNumber1 ,0  as phaseProduction1,\n" +
            " count(1) as phaseNumber2, sum(productionNum) as phaseProduction2, 0 as phaseNumber3, 0 as phaseProduction3, 0 as phaseNumber4, 0 as phaseProduction4\n" +
            "from tb_solidifyrecord where starttime2 > #{startTime} and starttime2 < #{endTime} and plantID = #{plantID} ${lineFilter} group by materialName,solidifyRoomID )\n" +
            "union all\n" +
            "( select solidifyRoomID,solidifyRoomName , materialName ,0  as totalNumber ,0 as totalProduction,0  as phaseNumber1 ,0  as phaseProduction1,\n" +
            "0 as phaseNumber2, 0 as phaseProduction2,  count(1) as phaseNumber3, sum(productionNum) as phaseProduction3, 0 as phaseNumber4, 0 as phaseProduction4\n" +
            "from tb_solidifyrecord where starttime3 > #{startTime} and starttime3 < #{endTime} and plantID = #{plantID} ${lineFilter} group by materialName,solidifyRoomID )\n" +
            "union all\n" +
            "( select solidifyRoomID,solidifyRoomName , materialName ,0  as totalNumber ,0 as totalProduction,0  as phaseNumber1 ,0  as phaseProduction1,\n" +
            "0 as phaseNumber2, 0 as phaseProduction2,  0 as phaseNumber3, 0 as phaseProduction3, count(1) as phaseNumber4, sum(productionNum)  as phaseProduction4\n" +
            "from tb_solidifyrecord where endtime3 > #{startTime} and endtime3 < #{endTime} and plantID = #{plantID} ${lineFilter} group by materialName,solidifyRoomID )\n" +
            ") a group by solidifyRoomID,materialName order by solidifyRoomID,materialName ")
    List<Map<Object, Object>>  getSolidifyWorkRecordDetail(String plantID,@Param("lineFilter") String lineID,String startTime,String endTime);
}