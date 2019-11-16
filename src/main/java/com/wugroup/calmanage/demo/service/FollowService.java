package com.wugroup.calmanage.demo.service;

import com.wugroup.calmanage.demo.Util.JedisAdapter;
import com.wugroup.calmanage.demo.Util.JedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Haozk on 2019/11/15
 */

@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 用户关注
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId,int entityType,int entityId){
        //用于获取粉丝
        String followerKey = JedisKeyUtil.getFolloerKey(entityType,entityId);
        //用于获取关注对象
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        //添加粉丝
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        //添加关注对象
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx,jedis);
        return ret.size()==2 && (Long)ret.get(0)>0 && (Long) ret.get(1)>0;
    }

    /**
     * 用户取消关注
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unFollow(int userId,int entityType,int entityId){
        //用于获取粉丝
        String followerKey = JedisKeyUtil.getFolloerKey(entityType,entityId);
        //用于获取关注对象
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId,entityType);
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        //添加粉丝
        tx.zrem(followerKey,String.valueOf(userId));
        //添加关注对象
        tx.zrem(followeeKey,String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx,jedis);
        return ret.size()==2 && (Long)ret.get(0)>0 && (Long) ret.get(1)>0;
    }

    /**
     * 获取所有粉丝
     *  @param count
     *  @param entityType
     *  @param entityId
     * @return
     */

    public List<Integer> getFollowers(int entityType,int entityId,int count){
        String followerKey = JedisKeyUtil.getFolloerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,0,count));
    }

    /**
     * 获取所有粉丝,翻页
     *  @param count
     *  @param offset
     *  @param entityType
     *  @param entityId
     * @return
     */
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = JedisKeyUtil.getFolloerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, offset+count));
    }
    /**
     * 获取所有关注对象
     *  @param count
     *  @param entityType
     *  @param userId
     * @return
     */
    public List<Integer> getFollowees(int userId, int entityType, int count) {
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }
    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, offset+count));
    }

    /**
     * 获取粉丝数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = JedisKeyUtil.getFolloerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    /**
     * 获取关注数量
     * @param entityType
     * @param userId
     * @return
     */
    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    private List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    /**
     *  判断用户是否关注了某个实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = JedisKeyUtil.getFolloerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }
}
