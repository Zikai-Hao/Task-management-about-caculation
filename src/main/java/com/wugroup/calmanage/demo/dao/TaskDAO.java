package com.wugroup.calmanage.demo.dao;

import com.wugroup.calmanage.demo.model.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface TaskDAO {
    //mysql语法字符串
    String TABEL_NAME=" task ";
    String INSERT_FIELDS = " task_name, task_type, created_date, user_id, note";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //返回任务编号,上传任务记录
    @Insert({"insert into ", TABEL_NAME," (", INSERT_FIELDS, ") values(#{taskName},#{taskType},#{createdDate},#{userId},#{note})"})
    int addTask(Task task);

    //查询操作，返回Task类,offset-limit 起始到终止数量
    /*List<Task> selectLatestTasks(@Param("userId") int userId, @Param("offset") int offset,
                                 @Param("limit") int limit);*/
    @Select("select" + SELECT_FIELDS +" from" + TABEL_NAME + "order by id desc limit #{offset},#{limit}")
    List<Task> selectLatestTasks(@Param("offset") int offset,@Param("limit")int limit);

    @Select("select" + SELECT_FIELDS +" from" + TABEL_NAME + " where user_id=#{userId} order by id desc limit #{offset},#{limit}")
    List<Task> selectUserLatestTasks(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);

    //查询操作，返回User类
    @Select({"select ", SELECT_FIELDS, " from ", TABEL_NAME, " where id=#{id}"})
    Task selectById(int id);

    //删除任务记录
    @Delete({"delete from ", TABEL_NAME, " where id=#{id}"})
    void deleteById(int id);


}
