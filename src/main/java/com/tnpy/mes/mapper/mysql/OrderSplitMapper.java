package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.customize.CustomOrderSplitRecord;
import com.tnpy.mes.model.mysql.OrderSplit;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface OrderSplitMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderSplit record);

    int insertSelective(OrderSplit record);

    OrderSplit selectByPrimaryKey(String id);

    @Select("select * from tb_ordersplit where orderSplitID= #{orderSplitName}")
    List<OrderSplit> selectByOrderSplitName(String orderSplitName);


    @Select("select * from tb_ordersplit ${filter}")
    List<OrderSplit> selectByFilter(@Param("filter") String filter);

    int updateByPrimaryKeySelective(OrderSplit record);

    int updateByPrimaryKey(OrderSplit record);

    int insertManyOrder(@Param("orderSplitList") List<OrderSplit> orderSplitList, @Param("orderID") String orderID);
@Select("select * from tb_ordersplit where orderID = #{orderid} order by status,orderSplitID ")
    List<OrderSplit> selectByOrderID(String orderid);

  /*  @Select("SELECT a.id,orderSplitID,orderID,productionNum,case a.status when '1' then '已下单' when '2' then '已打印'\n" +
            " when '3' then '已开工' when '4' then '已完成' when '5' then '已关闭' else '状态不详' end as status,\n" +
            " b.name as materialID FROM tb_ordersplit a left join sys_material b on a.materialID = b.id where orderID = #{orderid} order by orderSplitID asc") */
    List<OrderSplit> selectAfterMapByOrderID(String orderid);

    @Select(" select c.*,d.plantID,d.processID,d.lineID from ( \n" +
            " SELECT a.id,ordersplitid,a.orderid,productionnum,a.materialid ,a.status,case a.status when '1' then '已下单' when '2' then '已打印'\n" +
            "     when '3' then '已开工' when '4' then '已完成' when '5' then '已关闭' else '状态不详' end as statusName,\n" +
            "      b.name as materialName FROM tb_ordersplit a left join sys_material b on a.materialID = b.id where orderID = #{orderid} \n" +
            " ) c left join tb_workorder d on c.orderid = d.id  order by status ,orderSplitID")
    List<Map<String, String>> selectToMapByOrderID(String orderid);

    @Select(" select c.*,d.plantID,d.processID,d.lineID from ( \n" +
            "    SELECT a.id,ordersplitid,a.orderID,productionnum,a.materialid ,a.status,case a.status when '1' then '已下单' when '2' then '已打印'\n" +
            "     when '3' then '已开工' when '4' then '已完成' when '5' then '已关闭' else '状态不详' end as statusName,\n" +
            "      b.name as materialName FROM tb_ordersplit a left join sys_material b on a.materialID = b.id where a.id = #{id} " +
            " ) c left join tb_workorder d on c.orderid = d.id   where d.plantID = #{plantID} and d.processID = #{processID} order by status ,orderSplitID")
    List<Map<String, String>> selectToMapBySubOrderID(String id,String plantID,String processID);
    @Select(" select c.*,d.plantID,d.processID,d.lineID from ( \n" +
            "  SELECT a.id,ordersplitid,a.orderid,productionnum,a.materialid ,a.status,case a.status when '1' then '已下单' when '2' then '已打印'\n" +
            "     when '3' then '已开工' when '4' then '已完成' when '5' then '已关闭' else '状态不详' end as statusName,\n" +
            "      b.name as materialName FROM tb_ordersplit a left join sys_material b on a.materialID = b.id where a.orderSplitID like '%${orderName}%' \n" +
            " ) c left join tb_workorder d on c.orderid = d.id  where d.plantID = #{plantID} and d.processID = #{processID} order by status ,orderSplitID limit 100")
    List<Map<String, String>> selectToMapBySubOrderName(@Param("orderName") String orderName,String plantID,String processID);

    List<CustomOrderSplitRecord> selectAfterMapBySubOrderID(String id);

    List<CustomOrderSplitRecord> selectAfterMapBySubOrderName(String orderName);

    @Delete("delete from tb_ordersplit where orderID = #{orderID}")
    int deleteByOrderID(String orderID);

    int cancelFinishStatus(String suborderID);
 
    /**
   	 * 批量出窑使用
     * @param dryingkilnid
     * @return
     */
    @Update("UPDATE tb_ordersplit t1 SET t1. STATUS = #{status} WHERE t1.id IN ( SELECT a.id FROM ( SELECT id FROM tb_ordersplit t WHERE t.orderSplitID IN "
    		+ "( SELECT d.suborderID FROM tb_dryingkilnjzrecord d WHERE d.dryingKilnID = #{dryingkilnid} ) ) AS a )")
    int updateByDryingkilnid(@Param("dryingkilnid")  String dryingkilnid, String status);}