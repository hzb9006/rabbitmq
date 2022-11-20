package com.example.consumerspringboot.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SpringbootConsumer {

    @RabbitListener(queues = "springboot_queue")
    public void getmessages(Message message){
        System.out.println("new String(message.getBody()) = " + new String(message.getBody()));

    }
}
