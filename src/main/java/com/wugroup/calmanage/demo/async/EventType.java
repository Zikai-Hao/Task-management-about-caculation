package com.wugroup.calmanage.demo.async;

/**
 * Created by Haozk on 2019/11/14
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    TASKOVER(4);

    EventType(int value){
        this.value=value;
    }
    private int value;
    public int getValue(){
        return value;
    }


}
