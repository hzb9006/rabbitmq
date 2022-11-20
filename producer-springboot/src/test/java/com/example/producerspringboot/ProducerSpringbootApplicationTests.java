package com.example.producerspringboot;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProducerSpringbootApplicationTests {

	@Autowired
	RabbitTemplate rabbitTemplate;

	// 测试发送消息
	@Test
	void sendMessage() {
		// 发送消息
		rabbitTemplate.convertAndSend("springboot_exchange","springboot.haha","测试：springboot整合rabbitMq");
	}

}
