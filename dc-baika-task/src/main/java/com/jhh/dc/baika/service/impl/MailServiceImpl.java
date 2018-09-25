package com.jhh.dc.baika.service.impl;

import com.jhh.dc.baika.util.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.jhh.dc.baika.service.MailService;

@Service
public class MailServiceImpl implements MailService {

    @Value("${mail.wzz.host}")
    private String host;
    @Value("${mail.wzz.name}")
    private String name;
    @Value("${mail.wzz.pwd}")
    private String pwd;
    @Value("${mail.wzz.from}")
    private String from;

    @Override
    public void sendMail(String[] to, String[] copyto, String filePath, String fileName, String mainTitle, String mailContent) {
        MailSender cn = new MailSender();
        // 设置发件人地址、收件人地址和邮件标题
        cn.setAddress(from, to, copyto, mainTitle, mailContent);
        // 设置要发送附件的位置和标题
        cn.setAffix(filePath, fileName);
        // 设置smtp服务器以及邮箱的帐号和密码
        cn.send(host, name, pwd);
    }

    @Override
    public void sendMail(String[] to, String[] copyto, String[] filePaths, String fileNames[], String mainTitle, String mailContent) {
        MailSender cn = new MailSender();
        // 设置发件人地址、收件人地址和邮件标题
        cn.setAddress(from, to, copyto, mainTitle, mailContent);
        // 设置要发送附件的位置和标题
        cn.setAffixArray(filePaths, fileNames);
        // 设置smtp服务器以及邮箱的帐号和密码
        cn.send(host, name, pwd);
    }

    @Override
    public void sendMail(String[] to, String[] copyto, String mainTitle, String mailContent) {
        MailSender cn = new MailSender();
        // 设置发件人地址、收件人地址和邮件标题
        cn.setAddress(from, to, copyto, mainTitle, mailContent);
        // 设置要发送附件的位置和标题
        // 设置smtp服务器以及邮箱的帐号和密码
        cn.send(host, name, pwd);
    }

    @Override
    public void sendMail(String[] to, String mainTitle, String mailContent) {
        MailSender cn = new MailSender();
        // 设置发件人地址、收件人地址和邮件标题
        cn.setAddress(from, to, new String[0], mainTitle, mailContent);
        // 设置要发送附件的位置和标题
        // 设置smtp服务器以及邮箱的帐号和密码
        cn.send(host, name, pwd);
    }
}
