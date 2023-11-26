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


    //alttaki ikisi sayesinde artık queyu dinlerken patlayamayacak userdaki rabbitmq
    @Value("${rabbitmq.queue-register}")
    private String queueNameRegister; //kuyruk icin belirtecegim isim


    // bos bir queue olusturuyor diyebiliriz. sıra olusturuyor ama içi boş aslinda istek yoksa.
    @Bean
    public Queue registerQueue(){
        return new Queue(queueNameRegister);
    }


}
