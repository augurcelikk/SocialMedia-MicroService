package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterElasticProducer {
    @Value("${rabbitmq.exchange-user}")
    private String directExchange; //takas faktorumuz

    @Value("${rabbitmq.elastic-register-key}")
    private String registerBindingKey; //baglayicimiz

    private final RabbitTemplate rabbitTemplate;


    //modelin kendisini serilestirip, künyesine de bilgilerini ekliyor.
    public void sendNewUser(RegisterElasticModel model){
        rabbitTemplate.convertAndSend(directExchange,registerBindingKey,model);
    }
}
