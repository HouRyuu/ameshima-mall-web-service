package com.ameshima.user.utils;

import com.ameshima.common.redis.RedisClient;
import com.ameshima.common.utils.CommonUtil;
import com.ameshima.user.keys.UserKey;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
@ConfigurationProperties("mail.captcha")
@EnableAsync
public class CaptchaSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaSender.class);

    @Resource
    private RedisClient redisClient;
    @Resource
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;
    private String subject;
    private String text;

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Async
    public void sendCaptchaMail(@NotBlank String to) {
        String expiry = LocalDateTime.now().plusMinutes(UserKey.CAPTCHA_REGISTER.timeout()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss"));
        String captcha = CommonUtil.createCaptcha();
        LOGGER.info("{}にキャプチャ=>{}を送る", to, captcha);
        redisClient.set(UserKey.CAPTCHA_REGISTER, to, captcha);
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(from);
        mail.setTo(to);
        mail.setSentDate(new Date());
        mail.setSubject(subject);
        mail.setText(text.replace("{captcha}", captcha).replace("{time}", expiry));
        mailSender.send(mail);
        LOGGER.info("{}にキャプチャ=>{}を送るのが完了", to, captcha);
    }
}
