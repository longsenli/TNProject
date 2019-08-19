package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.GrantMaterialRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/24 9:52
 */
@Mapper
@Component
public  interface GrantMaterialRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(GrantMaterialRecord record);

    int insertSelective(GrantMaterialRecord record);

    GrantMaterialRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(GrantMaterialRecord record);

    int updateByPrimaryKey(GrantMaterialRecord record);

    @Select("select * from tb_grantmaterialrecord where orderID = #{orderID} limit 1")
    GrantMaterialRecord selectByOrderID(String orderID);

    @Select(" select a.suborderID as orderID,a.materialNameInfo as status,CONCAT(a.inputer,' ',a.inputTime) as returnMessage from ( select subOrderID,inputer, materialNameInfo,date_format(inputTime, '%Y-%m-%d %H:%i:%s') as inputTime \n" +
            " from tb_materialrecord where inputPlantID = #{plantID} and inputProcessID = #{processID} and  inputTime > #{startTime} and inputTime <  #{endTime} and inOrOut = '1' ) a left join\n" +
            "  (select orderID from tb_grantmaterialrecord where plantID = #{plantID}  and processID =  #{processID} and grantTime > #{startTime}  )  b  on a.subOrderID = b.orderID where b.orderID is  null order by subOrderID  ")
    List<Map<Object,Object>> notGrantMaterialDetail(String plantID, String processID, String startTime, String endTime  );
}