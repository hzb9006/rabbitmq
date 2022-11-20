package org.example.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

/**
 * 在正常情况下测试类是需要@RunWith的
 * 作用是告诉java你这个类通过用什么运行环境运行，例如启动和创建spring的应用上下文。否则你需要为此在启动时写一堆的环境配置代码。
 * 你在IDEA里去掉@RunWith仍然能跑是因为在IDEA里识别为一个JUNIT的运行环境，相当于就是一个自识别的RUNWITH环境配置。但在其他IDE里并没有。
 *
 *
 */
@ContextConfiguration(locations="classpath:spring-rabbitmq.xml")
@RunWith(SpringRunner.class)
public class ProducterTest {
    // 注入RabbitTemplate对象
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //测试简单模式
    @Test
    public void testSimple(){
        // 向简单模式的队列发送消息
        rabbitTemplate.convertAndSend("spring_simple_queue","测试：这是简单模式 ");

    }

    // 测试广播模式
    @Test
    public void testfanout(){
        //广播模式发送消息
        rabbitTemplate.convertAndSend("spring_fanout_exchange","","测试：这是广播模式");
    }

    // 测试确认模式，防止消息丢失
    @Test
    public void testConfirm(){
        // 定义确认的回调函数
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String cause) {
                if (b){
                    // 消息投递成功
                    System.out.println("消息投递成功！");
                }else{
                    System.out.println("消息投递失败！失败的原因是："+cause);
                    // 做一些处理，重新发送消息
                }

            }
        });
        // 通过定向交换机测试确认模式
//        rabbitTemplate.convertAndSend("spring_direct_exchange","confirm","测试：确认模式");

        // 设置一个不存在的交换机名字，看看失败的情况
        rabbitTemplate.convertAndSend("spring_direct_exchange111","confirm","测试：确认模式");

    }

    //测试退回模式
    @Test
    public void testReturn(){
        // 退回模式比确认模式多了一步！！需要设置强制退回！！必须设置
        rabbitTemplate.setMandatory(true);

        // 定义退回的回调函数--只有失败的时候才会调用该函数
        rabbitTemplate.setReturnCallback(
                new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                // 获取消息
                System.out.println("new String(message.getBody()) = " + new String(message.getBody()));
                //错误消息
                System.out.println("replyText = " + replyText);
                // 错误的状态码
                System.out.println("replyCode = " + replyCode);
                // 交换机的名字
                System.out.println("exchange = " + exchange);
                //路由key的名字
                System.out.println("routingKey = " + routingKey);


            }
        }
        );
        // 通过定向交换机测试退回模式
//       rabbitTemplate.convertAndSend("spring_direct_exchange","confirm","测试：退回模式");

        // 设置一个错误的路由key，看看失败的情况
        rabbitTemplate.convertAndSend("spring_direct_exchange","1111","测试：退回模式");

    }

    //测试消息的过期时间，如果队列和消息都设置了过期时间，以短的为准
    @Test
    public void testTTL(){
        // 创建消息的后置处理器--来设置消息的过期时间
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置消息的过期时间为5秒
                message.getMessageProperties().setExpiration("5000");
                return message;
            }
        };
        // 发送消息
        rabbitTemplate.convertAndSend("test_ttl_exchange","test.1","测试：消息的过期时间");

    }





 }
