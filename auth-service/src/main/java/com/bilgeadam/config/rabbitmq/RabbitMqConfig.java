package com.bilgeadam.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange-auth}")
    private String exchange; //5672ye gidecek. queue namein icinde duracak key ile birlikte.

    @Value("${rabbitmq.register-key}")
    private String registerBindingKey; //baglayici bir anahtar, exchange e baglanacak

    @Value("${rabbitmq.queue-register}")
    private String queueNameRegister; //kuyruk icin belirtecegim isim


    @Value("${rabbitmq.register-mail-key}")
    private String registerMailBindingKey;


    @Value("${rabbitmq.register-mail-queue}")
    private String registerMailQueue;


    @Bean //spring tarafından yonetilebilmesi icin gerekli. oteki türlü yalnızca sınıf tarafından yonetilebilirler
    public DirectExchange exchangeAuth(){
        return new DirectExchange(exchange);
    }


    @Bean
    public Queue registerQueue(){
        return new Queue(queueNameRegister);
    }


    @Bean
    public Binding bindingRegister(final Queue registerQueue, final DirectExchange exchangeAuth){
        return BindingBuilder.bind(registerQueue).to(exchangeAuth).with(registerBindingKey); //regsiterqueue sırasıyla exchangeauthu bir binding key(token gibi) ile bağlıyor
    }


    @Bean
    public Queue registerMailQueue(){
        return new Queue(registerMailQueue);
    }


    @Bean
    public Binding bindingMailRegister(final Queue registerMailQueue, final DirectExchange exchangeAuth){
        return BindingBuilder.bind(registerMailQueue).to(exchangeAuth).with(registerMailBindingKey); //regsiterqueue sırasıyla exchangeauthu bir binding key(token gibi) ile bağlıyor
    }

}
