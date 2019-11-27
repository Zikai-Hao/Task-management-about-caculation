package com.wugroup.calmanage.demo.Util;

import com.wugroup.calmanage.demo.Private.PrivateInf;
import freemarker.template.Template;
import freemarker.template.TemplateException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String templateAddr, Map<String, Object> model) {
        try {
            String htmlText="";
            try{

                //获取模板实例
                Template template=freeMarkerConfigurer.getConfiguration().getTemplate(templateAddr);
                //解析模板文件
                htmlText= FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            }catch(IOException e){
                e.printStackTrace();
            }catch(TemplateException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
            String nick = MimeUtility.encodeText("Group问答材料人");
            InternetAddress from = new InternetAddress(nick + "<2283887816@qq.com>");
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true,"UTF-8");
            helper.setFrom(from);//发送人
            helper.setTo(to);//邮件接收人
            helper.setSubject(subject);//标题
            helper.setText(htmlText,true);//设置HTML格式邮件内容
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(PrivateInf.MailNAME);
        mailSender.setPassword(PrivateInf.MailPSW);
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        //javaMailProperties.put("mail.smtp.auth", true);
        //javaMailProperties.put("mail.smtp.starttls.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
