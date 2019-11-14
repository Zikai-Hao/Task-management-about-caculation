package com.wugroup.calmanage.demo.async;

import java.util.List;

/**
 * Created by Haozk on 2019/11/14
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
