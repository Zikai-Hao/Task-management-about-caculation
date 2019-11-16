package com.wugroup.calmanage.demo.async;

/**
 * Created by Haozk on 2019/11/14
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5),
    REGIS(6);

    EventType(int value){
        this.value=value;
    }
    private int value;
    public int getValue(){
        return value;
    }


}
