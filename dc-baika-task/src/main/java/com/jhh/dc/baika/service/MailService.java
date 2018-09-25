package com.jhh.dc.baika.service;

public interface MailService {

    void sendMail(String[] to, String[] copyto, String filePath, String fileName, String mainTitle, String mailContent);

    void sendMail(String[] to, String[] copyto, String filePath[], String fileName[], String mainTitle, String mailContent);

    void sendMail(String[] to, String[] copyto, String mainTitle, String mailContent);

    void sendMail(String[] to, String mainTitle, String mailContent);
}
