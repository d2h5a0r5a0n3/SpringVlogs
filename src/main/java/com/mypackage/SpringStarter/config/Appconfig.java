package com.mypackage.SpringStarter.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class Appconfig {

    @Value("${mail.transport.protocol}")
    private String mail_transport_protocol;

    @Value("${spring.mail.smtp.ssl.trust}")
    private String spring_mail_smtp_ssl_trust;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String spring_mail_properties_mail_smtp_starttls_enable;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String spring_mail_properties_mail_smtp_auth;

    @Value("${spring.mail.port}")
    private String spring_mail_port;

    @Value("${spring.mail.host}")
    private String spring_mail_host;

    @Value("${spring.mail.username}")
    private String spring_mail_username;

    @Value("${spring.mail.password}")
    private String spring_mail_password;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailsender = new JavaMailSenderImpl();
        mailsender.setHost(spring_mail_host);
        mailsender.setPort(Integer.parseInt(spring_mail_port));
        mailsender.setUsername(spring_mail_username);
        mailsender.setPassword(spring_mail_password);
        Properties properties = mailsender.getJavaMailProperties();
        properties.put("mail.transport.protocol", mail_transport_protocol);
        properties.put("mail.smtp.auth", spring_mail_properties_mail_smtp_auth);
        properties.put("mail.smtp.starttls.enable", spring_mail_properties_mail_smtp_starttls_enable);
        properties.put("mail.smtp.ssl.trust", spring_mail_smtp_ssl_trust);
        properties.put("mail.debug", "true");
        return mailsender;
    }

}
