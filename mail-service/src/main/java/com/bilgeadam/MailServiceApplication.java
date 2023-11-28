package com.bilgeadam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class MailServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(MailServiceApplication.class);
    }






    //Deneme
//    private final JavaMailSender javaMailSender;

//    public MailServiceApplication(JavaMailSender javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }
//
//
//    @EventListener(ApplicationReadyEvent.class)//uygulamayı dinliyor. ayaga kalkar kalkmaz burayı calıstıracagını biliyor.runlamamıza veya cagirmamiza gerek kalmadan bu metodu calistiracagını biliyor.
//    public void sendMail(){
//        //gonderecegim mail uzerinde ozellestirmeler yapmamiza yarayacak simplemailmessage.
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        //mailin nereden gonderileceğini belirtiyoruz.
//        mailMessage.setFrom("${java11mailusername}");
//        //kime gönderecegimizi söylüyoruz burada
//        mailMessage.setTo("ayhan.ugur.celik@gmail.com");
//        mailMessage.setSubject("SocialMediaApp aktivasyon kodunuz");
//        mailMessage.setText("AR!Q4");
//        javaMailSender.send(mailMessage);
//    }
}