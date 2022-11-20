package org.example.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

@ContextConfiguration(locations = "classpath:spring-rabbitmq.xml")
@RunWith(SpringRunner.class)
public class ConsumerTest {

    /**
     * 测试消费：
     * 保证测试类，消费一直在运行状态
     */
    @Test
    public void testConsumer(){
        while (true){

        }

    }
}
