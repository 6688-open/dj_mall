package com.dj.mall.api.order.cmpt;

public interface EmailService {

    /**
     * 发送普通邮件
     * @param to
     * @param subject
     * @param mailText
     * @return
     * @throws Exception
     */
    boolean sendMail(String to, String subject, String mailText) throws Exception;

    /**
     * 发送HTML邮件
     * @param to
     * @param subject
     * @param mailHTML
     * @return
     * @throws Exception
     */
    boolean sendMailHTML(String to, String subject, String mailHTML) throws Exception;

    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param mailText
     * @param fileName
     * @return
     * @throws Exception
     */
    boolean sentMailFile(String to, String subject, String mailText, String fileName) throws Exception;
}
