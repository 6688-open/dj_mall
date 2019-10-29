package com.dj.mall.service.cmpt.impl;

import com.dj.mall.api.order.cmpt.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;

    /**
     * 发送普通邮件
     *
     * @param to       收件人
     * @param subject  邮件名称
     * @param mailText 邮件内容
     */
    @Override
    public boolean sendMail(String to, String subject, String mailText) {
        SimpleMailMessage mail = new SimpleMailMessage();
        //to 收件人
        mail.setTo(to);
        //发件人
        mail.setFrom(from);
        //邮件名称
        mail.setSubject(subject);
        //邮件主题
        mail.setText(mailText);
        //发送邮件
        mailSender.send(mail);
        return true;
    }

    /**
     * 发送HTML邮件
     *
     * @param to       收件人
     * @param subject  邮件名称
     * @param mailHTML 邮件内容
     */
    @Override
    public boolean sendMailHTML(String to, String subject, String mailHTML) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        //to 收件人
        helper.setTo(to);
        //发件人
        helper.setFrom(from);
        //邮件名称
        helper.setSubject(subject);
        //邮件主体内容
        helper.setText(mailHTML, true);
        //发送邮件<a>123</a>
        mailSender.send(message);
        return true;
    }

    /**
     * 发送带文件的邮件
     *
     * @param to       收件人
     * @param subject  邮件名称
     * @param mailText 邮件内容
     * @param fileName 附件
     * @throws Exception
     */
    @Override
    public boolean sentMailFile(String to, String subject, String mailText, String fileName) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        //to 收件人
        helper.setTo(to);
        //发件人
        helper.setFrom(from);
        //邮件名称
        helper.setSubject(subject);
        //邮件主题
        helper.setText(mailText);
        //获取附件
        FileSystemResource file = new FileSystemResource(fileName);
        //将附件添加至邮件主体
        helper.addAttachment(file.getFilename(), file);
        //发送邮件
        mailSender.send(message);
        return true;
    }

}
