package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.RewardingPunishmentDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RewardingPunishmentDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(RewardingPunishmentDetail record);

    int insertSelective(RewardingPunishmentDetail record);

    RewardingPunishmentDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RewardingPunishmentDetail record);

    int updateByPrimaryKey(RewardingPunishmentDetail record);
}