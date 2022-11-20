package com.example.producerspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // 声明交换机的名字
    public static final String EXCHANGE_NAME = "springboot_exchange";
    // 声明队列
    public static final String QUEUE_NAME = "springboot_queue";

    // 创建一个路由模式的交换机
    @Bean
    public Exchange bootExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    // 创建一个队列
    @Bean
    public Queue bootQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    /**
     * 绑定交换机和队列：
     *      需要指定哪个交换机，哪个队列，路由key
     *
      */
    @Bean
    public Binding binding(Exchange exchange,Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("springboot.#").noargs();
    }


}
