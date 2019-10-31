package com.dj.mall.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.dj.mall.api.order.cmpt.EmailService;
import com.dj.mall.constant.SystemConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqImpl implements MessageListener {

    @Autowired
    private EmailService emailService;

    @Override
    public void onMessage(Message message) {
        try {
            //获取 rabbitTemplate.convertAndSend("sendEmail", jsonObject.toString()); json字符串
            String json =  new String(message.getBody());
            //将字符串josn 转成josn对象
            JSONObject jsonObject = JSONObject.parseObject(json);
            Integer userId = jsonObject.getInteger("userId");
            String userEmail = jsonObject.getString("userEmail");
            String url = SystemConstant.HTTP_SEND_EMAIL+userId; //激活链接路径拼上id
            String to = userEmail;
            String subject = "DJ商城";
            String text = "您的激活链接是"+"<p><a href="+url+">点我激活</a></p>";
            emailService.sendMailHTML(to, subject, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
