package org.example.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class SimpleListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        // 获取消费者的唯一标识
        System.out.println("message.getMessageProperties().getConsumerTag() = " + new String(message.getMessageProperties().getConsumerTag()));
        // 获取消息的内容
        System.out.println("new String(message.getBody()) = " + new String(message.getBody()));

        // 获取这是第几条消息
        System.out.println("message.getMessageProperties().getDeliveryTag() = " + message.getMessageProperties().getDeliveryTag());

        // 获取交换机的名字
        System.out.println("交换机的名字是："+message.getMessageProperties().getReceivedExchange());

    }
}
