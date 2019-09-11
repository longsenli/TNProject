package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.IchnographyDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface IchnographyDetailInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(IchnographyDetailInfo record);

    int insertSelective(IchnographyDetailInfo record);

    IchnographyDetailInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(IchnographyDetailInfo record);

    int updateByPrimaryKey(IchnographyDetailInfo record);

    @Select("select * from tb_ichnographydetailinfo where mainRegionID = #{mainRegionID} order by drawNumber")
    List<IchnographyDetailInfo> selectByMinRegionID(String mainRegionID);

    @Select("SELECT a.*,ROUND(ifnull(b.dangerScore,0) / 20 ) as dangerScore FROM tb_ichnographydetailinfo a left join (select plantID,sum(score) as dangerScore from " +
            " ( select  plantID,case dangerLevel when '红' then 5 when '橙' then 4 when '黄' then 3 when '蓝' then 2 else 1 end as score FROM tb_hiddendangermanagerecord " +
            " where status != '-1' and reportTime >= #{startTime} and reportTime < #{endTime} and hiddenDangerType ='隐患上报' group by plantID,dangerLevel ) a group by plantID) b\n" +
            " on a.regionID = b.plantID  where mainRegionID = #{mainRegionID} order by drawNumber")
    List<Map<Object,Object>> selectSSTByMinRegionID(String mainRegionID,String startTime,String endTime);
}