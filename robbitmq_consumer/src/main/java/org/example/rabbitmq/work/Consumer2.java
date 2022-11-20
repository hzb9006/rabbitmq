package org.example.rabbitmq.work;

import com.rabbitmq.client.*;
import org.example.util.ConnectionUtil;

import java.io.IOException;

public class Consumer2 {
    public static void main(String[] args) throws Exception {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 创建Consumer对象
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 获取消费者的唯一标识
                System.out.println("consumerTag = " + consumerTag);
                // 获取交换机的名字
                System.out.println("envelope.getExchange() = " + envelope.getExchange());
                // 获取路由key
                System.out.println("envelope.getRoutingKey() = " + envelope.getRoutingKey());
                // 获取当前是第几条消息
                System.out.println("envelope.getDeliveryTag() = " + envelope.getDeliveryTag());
                // 获取消息内容
                System.out.println("new String(body) = " + new String(body));



            }
        };
        // 消费消息
        channel.basicConsume("worker_queue",true,defaultConsumer);






    }
}
