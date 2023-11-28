package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j//consol'a log ciktisi vermek icin kullanilan bir kutuphane
public class RegisterConsumer {

    private final UserProfileService userProfileService;


    // serilestirilmis veriyi alıyor deSerialize ediyor.
    //dinleyecek birşey bulamadıgı icin beancreation hatasi veriyor cunku kuyrukta hiç istek yok baslangicta. ama bekliyor. patlamıyor hemen.
    @RabbitListener(queues = "${rabbitmq.queue-register-elastic}")  //bu kuyruktaki bize gelecek verileri dinleyerek tüketmeye basliyor.
    public void createNewUser(RegisterElasticModel model){
        log.info("User {}",model.toString());
        userProfileService.createUserWithRabbitMq(model);
//        userProfileService.createUser()
    }

    //serilestiriyor 7070'de -> sonra rabbitmq 5672ye yoollanıyor. onu tekrar deserialize etmeye calisiyorum 7071'de.
    //guvenlik duvari disaridan gelen herhangi bir seyi deserialize edilmesine izin vermiyor. o yuzden env ekliyoruz;
    //SPRING_AMQP_DESERIALIZATION_TRUST_ALL = true yapiyoruz

}
