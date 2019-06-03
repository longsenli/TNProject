package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.IchnographyDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

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
}