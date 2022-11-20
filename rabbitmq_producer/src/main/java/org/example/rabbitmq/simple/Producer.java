package org.example.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单模式的生产者
 */
public class Producer  {
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
         * queue      参数1：队列名称
         * durable    参数2：是否定义持久化队列,当mq重启之后,还在
         * exclusive  参数3：是否独占本次连接
         *            ① 是否独占,只能有一个消费者监听这个队列
         *            ② 当connection关闭时,是否删除队列
         * autoDelete 参数4：是否在不使用的时候自动删除队列,当没有consumer时,自动删除
         * arguments  参数5：队列其它参数
         */

        // 声明队列
        channel.queueDeclare("simple_queue",true,false,false,null);

        // 创建要发送的信息
         String message="你好,阿菲3!";


        /**
         * 参数1：交换机名称,如果没有指定则使用默认Default Exchage
         * 参数2：路由key,简单模式可以传递队列名称,就是指定这个消息要放到哪个队列里面
         * 参数3：配置信息
         * 参数4：消息内容
         */

        //发布消息
        channel.basicPublish("","simple_queue",null,message.getBytes());

        // 关闭资源
        channel.close();
        connection.close();



    }

}
