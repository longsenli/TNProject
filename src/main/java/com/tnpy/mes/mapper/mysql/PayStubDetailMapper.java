package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PayStubDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PayStubDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayStubDetail record);

    int insertSelective(PayStubDetail record);

    PayStubDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayStubDetail record);

    int updateByPrimaryKey(PayStubDetail record);

    @Select(" select id,staffName,finalWage,productionWage,rewardingWage,punishmentWage,extdWage1,extdWage2,extdWage3,closingDate,remark from tb_paystubdetail ${filter}")
    List<Map<Object, Object>> selectByFilter(@Param("filter") String filter);
}