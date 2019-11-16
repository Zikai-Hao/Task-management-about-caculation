package com.wugroup.calmanage.demo;

import com.wugroup.calmanage.demo.async.EventModel;
import com.wugroup.calmanage.demo.async.EventProducer;
import com.wugroup.calmanage.demo.async.EventType;
import com.wugroup.calmanage.demo.async.Handler.RegisterSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Haozk on 2019/11/16
 */
public class MailTest {


    public static void main(String[] args) {
        EventProducer eventProducer = new EventProducer();
        RegisterSuccessHandler reg = new RegisterSuccessHandler();
        try{
            reg.doHandle(new EventModel().setExt("username","测试邮件").setExt("email","1125037115@qq.com"));

        } catch(Exception e){
            System.out.println("发送失败"+e.getMessage());


        }
    }
}
