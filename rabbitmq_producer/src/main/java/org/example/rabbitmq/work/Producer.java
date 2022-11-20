package org.example.rabbitmq.work;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.util.ConnectionUtil;

/**
 * 工作模型的生产者
 */
public class Producer {
    public static final String QUEUE_MAMQ="worker_queue";
    public static void main(String[] args) throws Exception {
        // 使用工具类获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 声明队列
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(QUEUE_MAMQ,true,false,false,null);
        // 一次性发送十条消息
        for (int i = 0; i < 10; i++) {
             // 设置消息
            String message="消息"+(i+1);
            // 发布消息
            channel.basicPublish("",QUEUE_MAMQ,null,message.getBytes());
        }
        // 关闭资源
        ConnectionUtil.closeResource(channel,connection);


    }
}
