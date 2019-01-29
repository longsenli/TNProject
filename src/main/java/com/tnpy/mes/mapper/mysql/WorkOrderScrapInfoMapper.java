package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.Material;
import com.tnpy.mes.model.mysql.WorkOrderScrapInfo;
import com.tnpy.mes.model.mysql.WorkOrderScrapInfoKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WorkOrderScrapInfoMapper {
    int deleteByPrimaryKey(WorkOrderScrapInfoKey key);

    int insert(WorkOrderScrapInfo record);

    int insertSelective(WorkOrderScrapInfo record);

    WorkOrderScrapInfo selectByPrimaryKey(WorkOrderScrapInfoKey key);

    int updateByPrimaryKeySelective(WorkOrderScrapInfo record);

    int updateByPrimaryKey(WorkOrderScrapInfo record);

//    @Select("select * from tb_workOrderScrapInfo where orderID in (select id from tb_workorder where  <if test=\" '-1' != lineID \"> lineID = #{lineID} and </if>    scheduledStartTime >= #{startTime} and" +
//            " scheduledStartTime <= #{endTime}  )")
    List<WorkOrderScrapInfo> selectByParams(String plantID , String processID,String lineID, String startTime, String endTime );

    int insertManyOrderScrap(@Param("workOrderScrapInfoList") List<WorkOrderScrapInfo> workOrderScrapInfoList, @Param("orderID") String orderID);

    @Select("select a.id,a.name,ifnull(b.value,\"\") as description from \n" +
            " ( select * from sys_material where id in (select distinct inMaterialID from sys_materialrelation where outMaterialID = #{materialID}) ) a \n" +
            " left join (select materialID,value from tb_workorderscrapinfo where orderID = #{orderID}) b on a.id = b.materialID\n")
   List<Material>  getMaterialScrapInfo(String materialID, String orderID );
 }