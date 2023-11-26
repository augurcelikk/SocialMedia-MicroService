package com.bilgeadam.service;

import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender javaMailSender;


    public void sendMail(RegisterMailModel model){
        //gonderecegim mail uzerinde ozellestirmeler yapmamiza yarayacak simplemailmessage.
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        //mailin nereden gonderileceğini belirtiyoruz.
        mailMessage.setFrom("${java11mailusername}");
        //kime gönderecegimizi söylüyoruz burada
        mailMessage.setTo(model.getEmail());
        mailMessage.setSubject("SocialMediaApp aktivasyon kodunuz");
        mailMessage.setText("Degerli" + model.getUsername() + " Hesap dogrulama kodunuz: " + model.getActivationCode());
        javaMailSender.send(mailMessage);
    }
}
