package com.holddie.springboot.email.service;

import com.holddie.springboot.email.server.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * 邮件发送简单测试
 * @author HoldDie
 * @version 1.0.0
 * @email holddie@163.com
 * @date 2017/11/28 21:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testSimpleMail() {
        mailService.sendSimpleMail("holddie@163.com","简单邮件","简单邮件，简单内容");
    }

    @Test
    public void testHtmlMail(){
        String content="<html><body><h3>hello world! 这是一封html邮件！</h3></body></html>";
        mailService.sendHtmlMail("holddie@163.com","一封HTML邮件",content);
    }

    @Test
    public void testAttachmentMail(){
        String filepath = "C:\\Users\\HoldDie\\Pictures\\1.png";
        mailService.sendAttachmentsMail("holddie@163.com","带附件邮件","<html><body><h1>有附件，请查收！</h1></body></html>",filepath);
    }

    @Test
    public void testInlineResourceMail(){
        String rscId = "holddie";
        String filepath = "C:\\Users\\HoldDie\\Pictures\\1.png";
        mailService.sendInlineResourceMail("holddie@163.com","带附件邮件","<html><body><img src='cid:" + rscId + "' ><h1>有附件，请查收！</h1></body></html>",filepath,rscId);
    }

    @Test
    public void sendTemplateMail(){
        //创建邮件正文
        Context context = new Context();
        context.setVariable("name123123","holddie");
        String emailContext = templateEngine.process("emailTemplate",context);
        mailService.sendHtmlMail("holddie@163.com","主题：这是邮件模板",emailContext);
    }
}
