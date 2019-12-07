package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyProductionAndWageDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface DailyProductionAndWageDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyProductionAndWageDetail record);

    int insertSelective(DailyProductionAndWageDetail record);

    DailyProductionAndWageDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyProductionAndWageDetail record);

    int updateByPrimaryKey(DailyProductionAndWageDetail record);


//    @Select(" select c.*,uuid() as id,ifnull(d.price,0) as univalence,round(c.shelfProduction * ifnull(d.price,0),2) as wage from (\n" +
//            "select a.plantID,a.processID,a.lineID,a.worklocationID,a.materialName,a.materialID,a.totalProduction,b.staffID,b.staffName,b.extd1,b.classType1,b.classType2,DATE_FORMAT( b.dayTime,'%Y-%m-%d') as dayTime,a.totalProduction as shelfProduction from (\n" +
//            "select inputPlantID as plantID,inputProcessID as processID,inputLineID as lineID,null as worklocationID,materialNameInfo as materialName ,materialID,sum(number) as totalProduction from tb_materialrecord where inputPlantID = #{plantID}\n" +
//            " and inputProcessID = #{processID} and status ='1' and orderID like ${orderInfo}  ${lineFilter}  group by inputLineID,materialNameInfo ) a left join tb_staffattendancedetail b \n" +
//            " on a.lineID = b.lineID and b.verifierID is not null and b.classType1 = #{classType} and b.dayTime = #{dayTime} ) c left join tb_workcontentunivalence d on c.materialID= d.materialID and c.plantID = d.plantID and c.processID = d.processID" +
//            " and c.extd1 = d.workContentID order by lineID\n" )
@Select( "select c.plantID,c.processID,c.lineID,c.worklocationID,c.materialName,c.materialID,round(c.totalProduction /ifnull(d.extd1,1),0) as totalProduction,c.staffID,c.staffName,c.extd1,c.classType1,c.classType2,c.dayTime,round(c.totalProduction /(ifnull(d.extd1,1) * staffNumber),0) as shelfProduction ,uuid() as id,\n" +
            "ifnull(d.price,0) as univalence,round(c.totalProduction /(ifnull(d.extd1,1) * staffNumber)  * ifnull(d.price,0),2) as wage ,c.staffNumber from (\n" +
            "select m.*,1 as staffNumber  from (\n" +
            "  select a.plantID,a.processID,a.lineID,a.worklocationID,a.materialName,a.materialID,a.totalProduction,b.staffID,b.staffName,b.extd1,b.classType1,b.classType2,DATE_FORMAT( b.dayTime,'%Y-%m-%d') as dayTime,a.totalProduction as shelfProduction from (\n" +
            " select inputPlantID as plantID,inputProcessID as processID,inputLineID as lineID,null as worklocationID,materialNameInfo as materialName ,materialID,sum(number) as totalProduction from tb_materialrecord where inputPlantID = #{plantID}\n" +
            "   and inputProcessID = #{processID} and status ='1' and orderID like  ${orderInfo}  ${lineFilter}   group by inputLineID,materialNameInfo ) a left join tb_staffattendancedetail b  \n" +
            " on a.lineID = b.lineID and b.verifierID is not null and b.classType1 = #{classType} and b.plantID = #{plantID}  and b.dayTime = #{dayTime} \n" +
            " ) m left join (select lineID,extd1,count(1) as staffNumber from tb_staffattendancedetail where  verifierID is not null and processID = #{processID}  and plantID = #{plantID} and classType1 = #{classType} and dayTime = #{dayTime} group by lineID,extd1) n\n" +
            " on m.lineID = n.lineID and m.extd1 = n.extd1 \n" +
            " ) c left join tb_workcontentunivalence d on c.materialID= d.materialID and c.plantID = d.plantID and c.processID = d.processID\n" +
            " and c.extd1 = d.workContentID order by lineID\n" )
    List<Map<Object,Object>>  orderProductionWageInfoByLine(String plantID, String processID,  @Param("lineFilter") String lineFilter, @Param("orderInfo") String orderInfo, String dayTime, String classType);

    @Select( "select c.plantID,c.processID,c.lineID,c.worklocationID,c.materialName,c.materialID,round(c.totalProduction /ifnull(d.extd1,1),0) as totalProduction,c.staffID,c.staffName,c.extd1,c.classType1,c.classType2,c.dayTime,round(c.totalProduction /(ifnull(d.extd1,1) * staffNumber),0) as shelfProduction ,uuid() as id,\n" +
            "ifnull(d.price,0) as univalence,round(c.totalProduction /(ifnull(d.extd1,1) * staffNumber)  * ifnull(d.price,0),2) as wage ,c.staffNumber from (\n" +
            "select m.*,ifnull(n.staffNumber,1) as staffNumber  from (\n" +
            "  select a.plantID,a.processID,a.lineID,a.worklocationID,a.materialName,a.materialID,a.totalProduction,b.staffID,b.staffName,b.extd1,b.classType1,b.classType2,DATE_FORMAT( b.dayTime,'%Y-%m-%d') as dayTime,a.totalProduction as shelfProduction from (\n" +
            " select inputPlantID as plantID,inputProcessID as processID,inputLineID as lineID,null as worklocationID,materialNameInfo as materialName ,materialID,sum(number) as totalProduction from tb_materialrecord where inputPlantID = #{plantID}\n" +
            "   and inputProcessID = #{processID} and status ='1' and orderID like  ${orderInfo}  ${lineFilter}   group by inputLineID,materialNameInfo ) a left join tb_staffattendancedetail b  \n" +
            " on a.lineID = b.lineID and b.verifierID is not null and b.classType1 = #{classType} and b.plantID = #{plantID}  and b.dayTime = #{dayTime} \n" +
            " ) m left join (select lineID,extd1,count(1) as staffNumber from tb_staffattendancedetail where  verifierID is not null and processID = #{processID}  and plantID = #{plantID} and classType1 = #{classType} and dayTime = #{dayTime} group by lineID,extd1) n\n" +
            " on m.lineID = n.lineID and m.extd1 = n.extd1 \n" +
            " ) c left join tb_workcontentunivalence d on c.materialID= d.materialID and c.plantID = d.plantID and c.processID = d.processID\n" +
            " and c.extd1 = d.workContentID order by lineID\n" )
     List<Map<Object,Object>>  orderProductionWageInfoByLineAVGProduction(String plantID, String processID,  @Param("lineFilter") String lineFilter, @Param("orderInfo") String orderInfo, String dayTime, String classType);


    @Select(" select c.*,uuid() as id,ifnull(d.price,0) as univalence,round(c.shelfProduction * ifnull(d.price,0),2) as wage from (\n" +
            "select a.plantID,a.processID,a.lineID,a.worklocationID,a.materialName,a.materialID,a.totalProduction,b.staffID,b.staffName,b.extd1,b.classType1,b.classType2,DATE_FORMAT( b.dayTime,'%Y-%m-%d') as dayTime,a.totalProduction as shelfProduction from (\n" +
            "select inputPlantID as plantID,inputProcessID as processID,inputLineID as lineID,inputWorkLocationID as worklocationID,materialNameInfo as materialName ,materialID,sum(number) as totalProduction from tb_materialrecord where inputPlantID = #{plantID}\n" +
            " and inputProcessID = #{processID} and status ='1' and   orderID like ${orderInfo}  ${lineFilter} group by inputWorkLocationID,materialNameInfo ) a left join tb_staffattendancedetail b \n" +
            " on a.worklocationID = b.worklocationID and b.verifierID is not null and b.classType1 = #{classType} and b.dayTime = #{dayTime} ) c left join tb_workcontentunivalence d on c.materialID= d.materialID and c.plantID = d.plantID and c.processID = d.processID" +
            " and c.extd1 = d.workContentID order by lineID,worklocationID\n" )
    List<Map<Object,Object>> orderProductionWageInfoByWorklocation(String plantID, String processID,   @Param("lineFilter") String lineFilter,@Param("orderInfo") String orderInfo, String dayTime, String classType);

    @Select("  select c.*,uuid() as id,ifnull(d.price,0) as univalence,round(c.shelfProduction * ifnull(d.price,0),2) as wage from (\n" +
            "select a.plantID,a.processID,a.lineID,a.worklocationID,a.materialName,a.materialID,a.totalProduction,b.staffID,b.staffName,b.extd1,b.classType1,b.classType2,DATE_FORMAT( b.dayTime,'%Y-%m-%d') as dayTime,a.totalProduction as shelfProduction from (\n" +
            "select plantID,#{processID} as processID,lineID,workLocation as worklocationID,materialID,materialName,count(1) as totalProduction  from tb_plasticusedrecord " +
            " where plantID = #{plantID} and  usedOrderID like  ${orderInfo} ${lineFilter} and status != '9'   group by workLocation,materialName\n" +
            " ) a left join  tb_staffattendancedetail b \n" +
            " on a.worklocationID = b.worklocationID and b.verifierID is not null and b.classType1 = #{classType} and b.dayTime = #{dayTime} ) c left join tb_workcontentunivalence d on  c.materialID= d.materialID and c.plantID = d.plantID and c.processID = d.processID" +
            " and c.extd1 = d.workContentID order by worklocationID\n" )
    List<Map<Object,Object>>  ZHQDProductionWageInfoByWorklocation(String plantID,String processID,  @Param("lineFilter") String lineFilter,@Param("orderInfo") String orderInfo,String dayTime,String classType);

    @Select(" select e.*, 0 as totalProduction,ifnull(f.price,0) as univalence, 0 as shelfProduction ,uuid() as id,0 as wage, 1 as staffNumber from  (\n" +
            "select  plantID,processID,c.lineID , worklocationID, staffName,staffID, classType1,classType2,dayTime ,extd1,d.materialName,d.materialID from (\n" +
            "select  plantID,processID,lineID , worklocationID, staffName,staffID, classType1,classType2,dayTime ,extd1,b.nextObjectID from (\n" +
            " select plantID,processID,lineID , worklocationID, staffName,staffID, classType1,classType2,DATE_FORMAT( dayTime,'%Y-%m-%d') as dayTime ,extd1 from tb_staffattendancedetail\n" +
            " where plantID = #{plantID} and processID = #{processID} and classType1 = #{classType}  and dayTime = #{dayTime}  ${lineFilter} ) a left join  \n" +
            " sys_objectrelationdict b on a.lineID = b.previousObjectID and b.status = '1'  ) c left join\n" +
            "( select lineID,materialID,materialName from  tb_chargingrackrecord  where putonDate =  #{dayTime} and plantID = #{plantID} group by lineID,materialID )\n" +
            "d on c.nextObjectID = d.lineID\n" +
            ") e left join tb_workcontentunivalence f on   e.materialID= f.materialID and e.plantID = f.plantID and e.processID = f.processID and e.extd1= f.workContentID" )
    List<Map<Object,Object>>  JSProductionWageInfoByWorklocation(String plantID,String processID,  @Param("lineFilter") String lineFilter,String dayTime,String classType);


    @Select("select plantID,processID,lineID,worklocationID,totalProduction,staffName,shelfProduction,univalence,wage,extd1,classType1,classType2,DATE_FORMAT( dayTime,'%Y-%m-%d') as dayTime,verifierName," +
            " DATE_FORMAT( verifyTime,'%Y-%m-%d %H:%i:%s') as verifyTime,materialID,materialName  from tb_dailyproductionandwagedetail" +
            " where  plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} and classType1 =#{classType}  order by lineID,worklocationID")
    List<Map<Object,Object>>  getFinalProductionWageInfo(String plantID,String processID,String dayTime,String classType);

    @Select("select count(1) from tb_dailyproductionandwagedetail  where  plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} and classType1 =#{classType}")
    int checkConfirmAlready(String plantID,String processID,String dayTime,String classType);

    @Delete("delete from tb_dailyproductionandwagedetail  where  plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} and classType1 =#{classType}")
    int deleteConfirmProductionWageRecord(String plantID,String processID,String dayTime,String classType);

    @Select("select * from tb_dailyproductionandwagedetail  where  plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} and classType1 =#{classType}")
    List<DailyProductionAndWageDetail> getConfirmRecord(String plantID,String processID,String dayTime,String classType);

    @Select("select a.*,b.name from (\n" +
            "select staffName,staffID,shelfProduction,materialName,DATE_FORMAT( dayTime,'%Y-%m-%d') as dayTime,DATE_FORMAT( verifyTime,'%Y-%m-%d %H:%i:%s') as verifyTime ,classType1,classType2,verifierName,extd1\n" +
            " from tb_dailyproductionandwagedetail where dayTime >= #{startTime} and dayTime <= #{endTime} and staffID = #{staffID}  ) a left join tb_workcontent b on a.extd1 = b.id order by dayTime,materialName")
    List<Map<Object,Object>>   getShelfWageDetail(String staffID,String startTime,String endTime);
}