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


    @Value("${rabbitmq.exchange-user}")
    private String exchange;

    @Value("${rabbitmq.elastic-register-key}")
    private String elasticRegisterBindingKey;

    @Value("${rabbitmq.queue-register-elastic}")
    private String elasticRegisterQueue;


    //alttaki ikisi sayesinde artık queyu dinlerken patlayamayacak userdaki rabbitmq
    @Value("${rabbitmq.queue-register}")
    private String queueNameRegister; //kuyruk icin belirtecegim isim


    @Bean
    public DirectExchange exchangeUser(){
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding bindingRegisterElastic(final Queue registerQueueElastic, final DirectExchange exchangeUser){
        return BindingBuilder.bind(registerQueueElastic).to(exchangeUser).with(elasticRegisterBindingKey);
    }


    // bos bir queue olusturuyor diyebiliriz. sıra olusturuyor ama içi boş aslinda istek yoksa.
    @Bean
    public Queue registerQueue(){
        return new Queue(queueNameRegister);
    }

    @Bean
    public Queue registerQueueElastic(){
        return new Queue(elasticRegisterQueue);
    }




}
