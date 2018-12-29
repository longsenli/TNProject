package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.OrderSplit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface OrderSplitMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderSplit record);

    int insertSelective(OrderSplit record);

    OrderSplit selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderSplit record);

    int updateByPrimaryKey(OrderSplit record);

    int insertManyOrder(@Param("orderSplitList") List<OrderSplit> orderSplitList, @Param("orderID") String orderID);
@Select("select * from tb_ordersplit where orderID = #{orderid}")
    List<OrderSplit> selectByOrderID(String orderid);
}