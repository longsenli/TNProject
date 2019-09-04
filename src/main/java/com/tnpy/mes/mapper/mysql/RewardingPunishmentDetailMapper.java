package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.RewardingPunishmentDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface RewardingPunishmentDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(RewardingPunishmentDetail record);

    int insertSelective(RewardingPunishmentDetail record);

    RewardingPunishmentDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RewardingPunishmentDetail record);

    int updateByPrimaryKey(RewardingPunishmentDetail record);

    @Select(" ( select staffName,round(sum(wage),2) as wage ,'' as reason,'' as closingDate from tb_rewardingpunishmentdetail where plantID = #{plantID} and processID = #{processID} and " +
            "closingDate >= #{startTime} and closingDate <= #{endTime} group by staffName order by staffName limit 1000 ) " +
            "union all" +
            " ( select staffName,wage,reason,CONCAT(closingDate,'') as closingDate  from tb_rewardingpunishmentdetail where plantID = #{plantID} and processID = #{processID} " +
            "and closingDate >= #{startTime} and closingDate <= #{endTime} order by closingDate,staffName limit 3000)")
    List<Map<Object, Object>> selectByFilter(String plantID, String processID, String startTime, String endTime);
}