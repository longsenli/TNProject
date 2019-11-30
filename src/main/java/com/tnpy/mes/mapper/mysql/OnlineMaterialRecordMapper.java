package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.OnlineMaterialRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface OnlineMaterialRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(OnlineMaterialRecord record);

    int insertSelective(OnlineMaterialRecord record);

    OnlineMaterialRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OnlineMaterialRecord record);

    int updateByPrimaryKey(OnlineMaterialRecord record);

    @Select("select * from tb_onlineMaterialRecord ${filter} ")
    List<OnlineMaterialRecord> getOnlineMaterialRecordByFilter(@Param("filter") String filter);

    @Select("select m.*,n.name as operator from ( " +
            "select e.materialID,e.plantID,e.processID,CONCAT(f.id , \"###\" , f.shortname) as lineID,e.materialNum from (\n" +
            "select c.materialID,c.plantID,CONCAT(d.id , \"###\" , d.shortname) as processID,c.lineID,c.materialNum  from (\n" +
            "select a.materialID,CONCAT(b.id , \"###\" ,b.shortname ) as plantID,a.processID,a.lineID,a.materialNum from (\n" +
            "SELECT materialID,plantID,processID,lineID,sum(materialNum) as materialNum FROM tb_onlinematerialrecord  where id in (${mergeID}) group by materialID,lineID) a \n" +
            "left join sys_industrialplant b on a.plantID = b.id ) c left join sys_productionprocess d on c.processID = d.id ) e left join sys_productionline f on e.lineID = f.id " +
            "    ) m left join sys_material n on m.materialID  = n.id ")
    List<OnlineMaterialRecord> getMergeNum(@Param("mergeID") String mergeID);

    @Update("update tb_onlinematerialrecord set status = #{status} where id in ( ${idList} )")
    int updateStatus(@Param("idList") String idList,String status);
}