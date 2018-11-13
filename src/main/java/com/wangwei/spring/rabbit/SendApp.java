package com.wangwei.spring.rabbit;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;

/**
 * 消息发送:
 *  ----前提:需要把RabbitTemplate对象纳入到Spring容器当中管理
 */
@ComponentScan
public class SendApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SendApp.class);
//        Map<String, RabbitTemplate> templateMap = context.getBeansOfType(RabbitTemplate.class);
//        System.out.println(templateMap);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);
        System.out.println(rabbitTemplate);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("name","张三");
        messageProperties.getHeaders().put("age",16);
//        Message message = new Message("Java Core".getBytes(),messageProperties);
//        rabbitTemplate.send(message);//使用默认exchange(""),默认routingKey("")发送消息
//        rabbitTemplate.send("info",message);//使用默认exchange(""),自己指定routingKey
//        rabbitTemplate.send("spring.direct.exchange","spring.direct",message);//指定exchange，指定routingKey发送消息

//        rabbitTemplate.convertAndSend("First In Java");//使用默认exchange(""),默认routingKey("")发送消息
//        rabbitTemplate.convertAndSend("debug","愿你走出半生，归来仍是少年");//使用默认exchange(""),自己指定routingKey
//        rabbitTemplate.convertAndSend("spring.direct.exchange","spring.direct","海棠未眠");

        rabbitTemplate.convertAndSend("","info","Never give up - 逆时针向",new CorrelationData("001"));
        /**
         * MessagePostProcessor:消息后置处理接口.真正发送消息前会调用该接口实现类的postProcessMessage方法。
         */
//        rabbitTemplate.convertAndSend("spring.direct.exchange", "spring.direct", "四轮承载肉体，二轮承载灵魂", new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                System.out.println("------消息处理前--------");
//                System.out.println(message);
//                message.getMessageProperties().getHeaders().put("dubai","灵魂摆渡");
//                return message;
//            }
//        });

//        rabbitTemplate.convertAndSend("spring.direct.exchange", "spring.direct","千古悠悠，有多少冤魂嗟叹。",message ->{
//            System.out.println("处理前--------");
//            System.out.println(message);
//            MessageProperties properties = new MessageProperties();
//            properties.getHeaders().put("name","张三");
//            properties.getHeaders().put("age",16);
//            message = new Message("空怅惘，人寰无限，丛生哀怨".getBytes(),properties);
//            System.out.println("--------处理后");
//            System.out.println(message);
//            return message;
//        });
        System.out.println("***********handle successful*****************");
        context.close();
    }
}
