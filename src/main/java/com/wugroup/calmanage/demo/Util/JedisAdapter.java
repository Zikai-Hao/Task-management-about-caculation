package com.wugroup.calmanage.demo.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Haozk on 2019/11/13
 */

@Service
public class JedisAdapter implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool;



    @Override
    public void afterPropertiesSet() throws Exception{
        pool=new JedisPool("redis://localhost:6379/1");
    }

    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);

        }catch (Exception e ){
            logger.error("Jedis异常"+e.getMessage());

        }finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key,value);

        }catch (Exception e ){
            logger.error("Jedis异常"+e.getMessage());

        }finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("Jedis异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public String get(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            String value = null;
            value = jedis.get(key);
            return value;
        }catch (Exception e) {
            logger.error("Jedis异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("Jedis异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("Jedis异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("Jedis异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public List<String> lrange(String key,int start,int end){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lrange(key,start,end);
        }catch (Exception e){
            logger.error("Jedis异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    //sortset 添加记录
    public long zadd(String key,double score,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zadd(key,score,value);
        }catch (Exception e){
            logger.error("Jedis异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //sortset 删除记录
    public long zrem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zrem(key,value);
        }catch (Exception e){
            logger.error("Jedis异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //获取jedis资源
    public Jedis getJedis(){
        return pool.getResource();
    }

    //开启事务
    public Transaction multi(Jedis jedis){
        try{
            return jedis.multi();
        }catch (Exception e){
            logger.error("Jedis异常" + e.getMessage());
        }
        return null;
    }



    //执行事务块内命令
    public List<Object> exec(Transaction tx,Jedis jedis){
        try{
            return tx.exec();
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
            tx.discard();//放弃事务
        }finally {//先关事务，后释放jedispool资源
            if (tx != null) {
                try {
                    tx.close();
                } catch (IOException ioe) {
                    // ..
                }
            }

            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    //获取对应key下的给定范围内的value值，
    public Set<String> zrange(String key,int start,int end){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zrange(key,start,end);
        }catch(Exception e) {
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    //获取对应key下的给定范围内（倒序）的value值，
    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    //获取key下的value的数量
    public long zcard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //获取key,value对应的score值
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }




}
