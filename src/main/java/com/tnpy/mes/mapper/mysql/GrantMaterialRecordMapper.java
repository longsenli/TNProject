package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.GrantMaterialRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

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

    @Select("select * from tb_grantmaterialrecord where orderID = #{orderID}")
    GrantMaterialRecord selectByOrderID(String orderID);
}