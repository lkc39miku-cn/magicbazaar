package com.a243.magicbazaar.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Component
public class EmailUtil {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailUtil(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void emailCode(HttpServletResponse response, String email) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("3304079457@qq.com");
        message.setTo(email);
        message.setSubject("验证码验证注册");

        String code = RandomUtil.code();

        saveCode(response, code);

        message.setText("你本次注册的验证码为：" + code);
        javaMailSender.send(message);
    }

    private void saveCode(HttpServletResponse response, String code) {
        Cookie cookie = new Cookie("emailCode", code);
        cookie.setMaxAge(60 * 30);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
