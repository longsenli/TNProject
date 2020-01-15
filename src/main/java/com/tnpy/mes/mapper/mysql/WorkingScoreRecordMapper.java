package com.tnpy.mes.mapper.mysql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2020-01-15 14:23
 */

@Mapper
@Component
public interface WorkingScoreRecordMapper {

    @Select("( SELECT '总计' as userID,'总计' as userName,sum(if(score < 0,score,0) ) as clauseContext,sum(if(score > 0,score,0) ) as score, sum(score)  as operator \n" +
            "FROM workingscoremsdb.tb_behaviorscoredetail where userID = #{staffName} and updateTime > #{startTime} and updateTime < #{endTime} and status = '1'\n" +
            ") union all (\n" +
            "SELECT userID,userName,clauseContext,score,operator FROM workingscoremsdb.tb_behaviorscoredetail \n" +
            "where userID = #{staffName} and updateTime > #{startTime} and updateTime < #{endTime} and status = '1' order by updateTime )")
    List<Map<Object,Object>> getWorkScoreDetailRecord(String staffName,String startTime,String endTime);

    @Select(" SELECT * FROM workingscoremsdb.tb_behaviorscoredetail where userID = #{staffName} and updateTime > #{startTime} and updateTime < #{endTime} and status = '1';")
    List<Map<Object,Object>> getWorkingScoreDailySummaryRecord(String staffName,String startTime,String endTime);

    @Select(" SELECT * FROM workingscoremsdb.tb_behaviorscoredetail where userID = #{staffName} and updateTime > #{startTime} and updateTime < #{endTime} and status = '1';")
    List<Map<Object,Object>> getWorkingScorePeriodSummaryRecord(String staffName,String startTime,String endTime);
}
