package org.example.rabbitmq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单模式的消费者
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        //todo： 后续学习一下springboot怎么整合RabbitMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置主机
        connectionFactory.setHost("192.168.6.100");
        // 设置端口号
        connectionFactory.setPort(5672);
        // 设置虚拟主机
        connectionFactory.setVirtualHost("/");
        // 设置用户名
        connectionFactory.setUsername("admin");
        // 设置密码
        connectionFactory.setPassword("123456");

        // 获取连接
        Connection connection = connectionFactory.newConnection();

        // 创建信道
        Channel channel = connection.createChannel();


        /**
         * 声明队列 作为一个消费者来说，可以不声明队列，如果声明了队列，消费消息
         * 如果队列不存在，则自动创建一个队列，但是如果声明的队列时指定的参数值与生产者指定的不一致，则会抛出异常
         *
          */
        channel.queueDeclare("simple_queue",true,false,false,null);

        // 创建Consumer对象
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){

             /*
               回调方法,当收到消息后,会自动执行该方法
               1. consumerTag：标识
               2. envelope：获取一些信息,交换机,路由key...
               3. properties：配置信息
               4. body：数据
            */

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 获取消费者标识
                System.out.println("consumerTag = " + consumerTag);
                // 获取交换机的名字
                System.out.println("envelope.getExchange() = " + envelope.getExchange());
                // 获取路由key
                System.out.println("envelope.getRoutingKey() = " + envelope.getRoutingKey());
                // 获取消息标识以及当前是第几条消息
                System.out.println("envelope.getDeliveryTag() = " + envelope.getDeliveryTag());

                // 获取其他属性
                System.out.println("properties = " + properties);

                // 获取消息内容
                System.out.println("new String(body) = " + new String(body));



                super.handleDelivery(consumerTag, envelope, properties, body);
            }
        };


          /*
        basicConsume(String queue, boolean autoAck, Consumer1 callback)
        参数：
            1. queue：队列名称
            2. autoAck：是否自动确认 ,类似咱们发短信,发送成功会收到一个确认消息
            3. callback：回调对象
         */
        // 消费消息
        channel.basicConsume("simple_queue",true,defaultConsumer);

        // 由于消费者没有关闭资源，所以生产者生产一条，消费者就消费一条数据
        // 如果消费者关闭的时候，生产者生产了多条消息，会被取出



    }
}
