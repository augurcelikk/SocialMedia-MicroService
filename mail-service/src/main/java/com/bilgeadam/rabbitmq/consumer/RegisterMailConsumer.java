package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import com.bilgeadam.service.MailSenderService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterMailConsumer {

    private final MailSenderService mailSenderService;


    @RabbitListener(queues = "${rabbitmq.register-mail-queue}")
    public void sendActivationCode(RegisterMailModel model){
        log.info("User {}",model.toString());
        mailSenderService.sendMail(model);
    }
}
