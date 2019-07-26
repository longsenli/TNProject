package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.customize.CustomWorkOrderRecord;
import com.tnpy.mes.model.mysql.Workorder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface WorkorderMapper {
    int deleteByPrimaryKey(String id);

    int insert(Workorder record);

    int insertSelective(Workorder record);

    Workorder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Workorder record);

    int updateByPrimaryKey(Workorder record);

    @Select("select * from tb_workOrder")
    List<Workorder> selectAll();

    @Select("select * from tb_workOrder ${filter} ")
    List<Workorder> selectByFilter(@Param("filter") String filter);

    @Select("select processID from tb_workorder where id = #{orderID}")
    String getProcessIDByOrder(String orderID);
    List<CustomWorkOrderRecord> selectCustomResultByFilter(@Param("filter") String filter);

    @Update("UPDATE tb_workOrder set status = #{status} where scheduledStartTime = #{time} and status < '5'")
    int finishOrder(String time,String status);

    @Update("UPDATE tb_workOrder set status = #{status} where scheduledStartTime = #{time}  and status < '5'")
    int startOrder(String time,String status);

    @Select("select totalProduction,c.name as materialName,d.name as lineName from\n" +
            " (  select totalProduction,materialID,lineID,b.name from (\n" +
            " select totalProduction,materialID,lineID from tb_workorder where plantID = #{plantID} and processID = #{processID}  and status != '5'  and scheduledStartTime >= #{startTime} and scheduledStartTime <= #{endTime}\n" +
            " ) a left join sys_material b on a.materialID = b.id \n" +
            " ) c left join sys_productionline d on c.lineID = d.id  order by lineName ")
   List<Map<Object,Object>> getPlanProductionDashboard(String plantID, String processID, String startTime, String endTime );

    @Select("select ifnull(realProduction,0)  as realProduction, materialName,f.name as lineName from (\n" +
            "select realProduction,materialID,lineID,d.name as materialName from (\n" +
            "select a.id,a.lineID,a.materialID,sum(b.number) as realProduction  from (\n" +
            "select id,orderID,totalProduction,materialID,lineID from tb_workorder where plantID = #{plantID} and processID = #{processID} and status != '5' and scheduledStartTime >= #{startTime} and scheduledStartTime <= #{endTime}\n" +
            ") a left join tb_materialrecord b on a.id = b.orderID  group by a.id \n" +
            ") c  left join sys_material d on c.materialID = d.id \n" +
            ") e left join sys_productionline f on e.lineID = f.id  order by lineName")
    List<Map<Object,Object>> getRealtimeProductionDashboard(String plantID, String processID, String startTime, String endTime );

    @Update("update tb_workorder set status= #{status} where id = #{id}")
    int updateWorkOrderStatus(String id,String status);

    @Select("select count(1) from tb_workorder where lineID = #{lineID}  and scheduledStartTime =  #{startDate}")
    int selectOrderNumber(String lineID,String startDate);

    @Select("select count(1) from tb_workorder where status !='5' and  plantID = #{plantID} and processID = #{processID} and scheduledStartTime =  #{time}")
    int selectOrderInfo(String plantID,String processID,String time);

    @Insert("\n" +
            "insert into tb_workorder (id,orderID,plantID,processID,lineID,status,batchNum,totalProduction,materialID,createTime,scheduledStartTime) \n" +
            "select id ,id ,plantID,processID,lineID,'1',batchNum,totalProduction,materialID,now(),${endOrderTime} from (\n" +
            "select replace(orderID,${timeStartString},${timeEndString}) as id ,plantID,processID,lineID,'1',batchNum,totalProduction,materialID \n" +
            "from tb_workorder where scheduledStartTime = #{startOrderTime}  and status != '5' and plantID = #{plantID} and processID = #{processID} ) a")
    int insertAutoMainOrder(String startOrderTime,@Param("endOrderTime") String endOrderTime,@Param("timeStartString")String timeStartString,@Param("timeEndString")String timeEndString,String plantID,String processID);

    @Insert("insert into tb_ordersplit (id,orderID,orderSplitID,productionNum,status,materialID) \n" +
            "select replace(b.id,${timeStartString},${timeEndString}) as id,replace(b.orderID,${timeStartString},${timeEndString}) as orderID,\n" +
            "replace(b.orderSplitID,${timeStartString},${timeEndString}) as orderSplitID,a.totalProduction/a.batchNum,'1',b.materialID\n" +
            " from  ( select id,totalProduction,batchNum from tb_workorder where scheduledStartTime  = #{startOrderTime}   and status != '5' and plantID = #{plantID} and" +
            " processID = #{processID} ) a left join  tb_ordersplit b on a.id = b.orderID ")
    int insertAutoSubOrder(String startOrderTime,@Param("timeStartString")String timeStartString,@Param("timeEndString")String timeEndString,String plantID,String processID);
}