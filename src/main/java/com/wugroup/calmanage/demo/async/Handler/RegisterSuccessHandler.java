package com.wugroup.calmanage.demo.async.Handler;

import com.wugroup.calmanage.demo.Util.MailSender;
import com.wugroup.calmanage.demo.async.EventHandler;
import com.wugroup.calmanage.demo.async.EventModel;
import com.wugroup.calmanage.demo.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Haozk on 2019/11/16
 */
@Component
public class RegisterSuccessHandler implements EventHandler {

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "注册成功", "mails/login_exception.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.REGIS);
    }
}
