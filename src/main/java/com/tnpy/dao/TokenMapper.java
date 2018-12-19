package com.tnpy.dao;

import com.tnpy.pojo.Token;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TokenMapper {
    int deleteByPrimaryKey(String tokenid);

    int insert(Token record);

    int insertSelective(Token record);

    Token selectByPrimaryKey(String tokenid);

    int updateByPrimaryKeySelective(Token record);

    int updateByPrimaryKey(Token record);



    @Insert(" insert into tb_token (tokenid, userid, token, buildtime) values (#{tokenid}, #{userid}, #{token}, #{buildtime})")
    int insert2(Token record);
    @Select("select * from tb_token where userid = #{userid}")
    Token findByUserId(int userid);

}