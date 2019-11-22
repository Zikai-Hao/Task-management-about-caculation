package com.wugroup.calmanage.demo.dao;

import com.wugroup.calmanage.demo.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by HaoZK on 2019/11/5
 * DAO
 */
@Mapper
public interface UserDAO {
    //mysql语法字符串
    String TABEL_NAME=" user ";
    String INSERT_FIELDS = " name, password, salt, head_url";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //返回主键值,插入数据操作
    @Insert({"insert into ", TABEL_NAME," (", INSERT_FIELDS, ")values(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    //查询操作，返回User类
    @Select({"select ", SELECT_FIELDS, " from ", TABEL_NAME, " where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABEL_NAME, " where name=#{name}"})
    User selectByName(String name);

    //修改密码
    @Update({"update ", TABEL_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(int id);

    //删除用户
    @Delete({"delete from ", TABEL_NAME, " where id=#{id}"})
    void deleteById(int id);

    //修改头像
    @Update({"update ", TABEL_NAME, " set head_url=#{0} where id=#{1}"})
    void updateHeadUrl(String headUrl,int id);


}
