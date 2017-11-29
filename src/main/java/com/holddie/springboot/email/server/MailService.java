package com.holddie.springboot.email.server;

/**
 * 邮件发送
 * @author HoldDie
 * @version 1.0.0
 * @email holddie@163.com
 * @date 2017/11/28 20:47
 */
public interface MailService {
    public void sendSimpleMail(String to, String subject, String context);

    public void sendHtmlMail(String to, String subject, String context);

    public void sendAttachmentsMail(String to, String subject, String context, String filePath);

    public void sendInlineResourceMail(String to, String subject, String context, String rscPath, String rscId);
}
