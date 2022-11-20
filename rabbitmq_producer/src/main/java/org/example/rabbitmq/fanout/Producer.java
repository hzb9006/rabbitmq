package org.example.rabbitmq.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.util.ConnectionUtil;

// 发布订阅模式--也叫广播模式
public class Producer {
    // 设置两个常量
    public static final String  QUEUE_NAME1="fanout_queue1";
    public static final String  QUEUE_NAME2="fanout_queue2";
    public static void main(String[] args) throws Exception {
        // 通过封装的工具类获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        /**
         * 声明交换机
         *      参数：
         *         1. exchange：交换机名称
         *         2. type：交换机类型
         *             DIRECT("direct"),：定向
         *             FANOUT("fanout"),：扇形（广播）,发送消息到每一个与之绑定队列。
         *             TOPIC("topic"),通配符的方式
         *             HEADERS("headers");参数匹配
         *         3. durable：是否持久化
         *         4. autoDelete：自动删除
         *         5. internal：内部使用。 一般false
         *         6. arguments：参数
         *
         */
        channel.exchangeDeclare("fanout", BuiltinExchangeType.FANOUT,true,false,null);

        // 声明队列
        channel.queueDeclare(QUEUE_NAME1,true,false,false,null);
        channel.queueDeclare(QUEUE_NAME2,true,false,false,null);

         // 把交换机和队列绑定
        channel.queueBind(QUEUE_NAME1,"fanout","");
        channel.queueBind(QUEUE_NAME2,"fanout","");

        // 设置发送的消息
        String message="该消息会被交换机进行发送到两个队列中";

        // 发送消息
        channel.basicPublish("fanout","",null,message.getBytes());

        // 关闭资源
        ConnectionUtil.closeResource(channel,connection);




    }
}
