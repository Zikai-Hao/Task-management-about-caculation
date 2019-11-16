package com.wugroup.calmanage.demo.dao;

import com.wugroup.calmanage.demo.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by Haozk on 2019/11/9
 */


@Mapper
public interface CommentDAO {
    //mysql语法字符串
      String TABEL_NAME=" comment ";
      String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type,status";
      String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //返回任务编号,上传任务记录
    @Insert({"insert into ", TABEL_NAME," (", INSERT_FIELDS, ") values(#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    //查询操作，返回Comment类,offset-limit 起始到终止数量
    /*List<Comment> selectLatestComments(@Param("userId") int userId, @Param("offset") int offset,
                                 @Param("limit") int limit);*/

    @Select({"select" + SELECT_FIELDS +" from" + TABEL_NAME + " where entity_id=#{entityId} and entity_type=#{entityType}  order by id desc "})
    List<Comment> selectCommentsByEntityId(@Param("entityId") int userId,@Param("entityType") int entityType);

    @Select({"select ", SELECT_FIELDS, " from ", TABEL_NAME, " where id=#{id}"})
    Comment getCommentById(int id);

    //查询操作，返回评论数
    @Select({"select count(id) from ", TABEL_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} "})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);


    //删除评论(隐藏)
    @Update({"Update comment set status=#{status} where id = #{id}"})
    int updateStatus(@Param("id") int id,@Param("status")int status);

    //查询用户评论
    @Select({"select count(id) from ", TABEL_NAME, " where user_id=#{userId}"})
    int getUserCommentCount(int userId);
}
