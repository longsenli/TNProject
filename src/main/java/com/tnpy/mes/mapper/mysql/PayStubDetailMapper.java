package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PayStubDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface PayStubDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayStubDetail record);

    int insertSelective(PayStubDetail record);

    PayStubDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayStubDetail record);

    int updateByPrimaryKey(PayStubDetail record);
}