package com.holddie.springboot.email.server.impl;

import com.holddie.springboot.email.server.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.FileSystem;

/**
 * 邮件发送Service实现
 * @author HoldDie
 * @version 1.0.0
 * @email holddie@163.com
 * @date 2017/11/28 20:48
 */
@Component
public class MailServiceImpl implements MailService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSimpleMail(String to, String subject, String context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
//        一般简单抄送人使用
//        message.copyTo(to2);
        message.setSubject(subject);
        message.setText(context);
        try {
            mailSender.send(message);
            logger.info("简单邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送简单邮件失败！", e);
        }
    }

    @Override
    public void sendHtmlMail(String to, String subject, String context) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            //true 表示要创建一个 multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            //抄送使用
//          helper.addCc(to1);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(context, true);
            mailSender.send(message);
            logger.info("Html 邮件发送成功");
        } catch (MessagingException e) {
            logger.error("Html 邮件发送失败", e);
            e.printStackTrace();
        }
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String context, String filePath) {

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(context,true);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            //添加多个附件时，可以使用多个addAttachment
            helper.addAttachment(fileName,file);
            mailSender.send(message);
            logger.info("带附件的邮件发送成功");
        } catch (MessagingException e) {
            logger.error("带附件邮件发送失败",e);
            e.printStackTrace();
        }
    }

    @Override
    public void sendInlineResourceMail(String to, String subject, String context, String rscPath, String rscId) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setText(context,true);
            helper.setSubject(subject);
            FileSystemResource resource = new FileSystemResource(new File(rscPath));
            //添加多个图片可以使用多条 <img src='cid:" + rscId + "' > 和 helper.addInline(rscId, res) 来实现。
            helper.addInline(rscId,resource);
            mailSender.send(message);
            logger.info("嵌入静态资源的邮件已经发送");
        } catch (MessagingException e) {
            logger.error("静态资源邮件发送异常",e);
            e.printStackTrace();
        }
    }
}
