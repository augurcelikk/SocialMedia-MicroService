package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProducer {

    @Value("${rabbitmq.exchange-auth}")
    private String directExchange; //takas faktorumuz

    @Value("${rabbitmq.register-key}")
    private String registerBindingKey; //baglayicimiz

    private final RabbitTemplate rabbitTemplate;


    //modelin kendisini serilestirip, künyesine de bilgilerini ekliyor.
    public void sendNewUser(RegisterModel model){
        rabbitTemplate.convertAndSend(directExchange,registerBindingKey,model);
    }

}
