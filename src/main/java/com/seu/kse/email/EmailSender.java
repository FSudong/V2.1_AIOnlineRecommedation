package com.seu.kse.email;

import com.seu.kse.util.Constant;
import com.sun.mail.util.MailSSLSocketFactory;


import javax.mail.*;
import javax.mail.internet.InternetAddress;

import javax.mail.internet.MimeMessage;

import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Created by yaosheng on 2017/6/2.
 */
public class EmailSender {

    private String userName = Constant.sender;
    private String psw= "wyvclnbvapjabbfa";
    private String host;
    private String from;
    private Properties proes;
    private Session session;
    public EmailSender(String from,String host){
        this.from=from;
        this.host=host;
    }

    //初始化邮箱配置
    public void init() throws GeneralSecurityException {
        proes = System.getProperties();
        // 设置邮件服务器
        proes.setProperty("mail.smtp.host", host);
        proes.put("mail.smtp.auth", "true");
        //设置SSL加密
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        proes.put("mail.smtp.ssl.enable","true");
        proes.put("mail.smtp.ssl.socketFactory",sf);
        // 获取默认session对象
        session = Session.getDefaultInstance(proes,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(userName, psw); //发件人邮件用户名、密码
            }
        });
    }

    public void send(String to , String content){
        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);
            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));
            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            // Set Subject: 头部头字段
            message.setSubject("recommend paper");
            // 设置消息体
            message.setContent(content,"text/html;charset=UTF-8");

            // 发送消息
            Transport.send(message);
        }catch (Exception mex) {
            mex.printStackTrace();
        }
    }
}
