package org.example.rabbitmq.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.util.ConnectionUtil;

/**
 * 这是路由模式的生产者
 */
public class Producer {
    public static final String EXCHANGE_NAME="direct_exchange";
    public static final String QUEUE_NAME1="direct_queue1";
    public static final String QUEUE_NAME2="direct_queue2";

    public static void main(String[] args) throws Exception {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,true,false,false,null);

        // 声明两个队列
        channel.queueDeclare(QUEUE_NAME1,true,false,false,null);
        channel.queueDeclare(QUEUE_NAME2,true,false,false,null);

        // 绑定队列和交换机
        channel.queueBind(QUEUE_NAME1,EXCHANGE_NAME,"error");// 发送消息的时候路由key是error，即当路由key是error的时候，交换机才会把该消息发给第一个队列

        // 发送消息的时候，路由key是info或者waring或者error的时候，消息会被转发到第二个队列
        channel.queueBind(QUEUE_NAME2,EXCHANGE_NAME,"info");
        channel.queueBind(QUEUE_NAME2,EXCHANGE_NAME,"waring");
        channel.queueBind(QUEUE_NAME2,EXCHANGE_NAME,"error");

        // 设置一个同时发送到队列一和队列二的消息
        String message="该消息会同时进入队列一和队列二";
        // 设置一个只发送到队列二的消息
        String message2="该消息只会发送到队列二";

        // 发送消息
        channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());
        channel.basicPublish(EXCHANGE_NAME,"waring",null,message2.getBytes());

        // 关闭资源
        ConnectionUtil.closeResource(channel,connection);



    }


}
