package com.wugroup.calmanage.demo.async;

import com.alibaba.fastjson.JSON;
import com.wugroup.calmanage.demo.Util.JedisAdapter;
import com.wugroup.calmanage.demo.Util.JedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Haozk on 2019/11/14
 */

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private final static Logger logger= LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans != null){
            for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for(EventType type:eventTypes){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread((Runnable) () ->{
            while(true){
                String key = JedisKeyUtil.getEventQueueKey();
                List<String> events = jedisAdapter.brpop(0,key);

                for(String message :events){
                    if (message.equals(key)) {
                        continue;
                    }

                    EventModel eventModel = JSON.parseObject(message,EventModel.class);
                    if(!config.containsKey(eventModel.getType())){
                        logger.error("事件无法识别");
                        continue;
                    }

                    for(EventHandler handler :config.get(eventModel.getType())){
                        handler.doHandle(eventModel);
                    }

                }



            }
        });
        thread.start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
