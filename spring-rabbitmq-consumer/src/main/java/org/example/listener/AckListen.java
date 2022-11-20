package org.example.listener;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

public class AckListen implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // 获取消息的标识
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 获取消息
            System.out.println("new String(message.getBody()) = " + new String(message.getBody()));

            // 处理业务逻辑
            /**
             *  如果没有出现异常，手动确认:
             *      basicAck中参数的意义：
             *          第一个参数：消息的标识
             *          第二个参数:设置是否批量确认，如果为true，第一条消息确认后，后面的自动确认
             *
             */

            channel.basicAck(deliveryTag,true);
        } catch (Exception e) {

            e.printStackTrace();
            /**
             * 出现异常,不确认消息：参数意义：
             *      1. 消息的标识
             *      2. 设置是否批量确认，如果为true，第一条消息确认后，后面的自动确认
             *      3， 设置当消息不确认的时候是否放回原消息队列，设置为true，以免消息丢失
             *
              */

            channel.basicNack(deliveryTag,true,true);
        }

    }
}
