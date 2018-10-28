package com.wangwei.spring.rabbit;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by wangwei on 2018/10/28 0028.
 */
@ComponentScan
public class App2 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App2.class);

        context.getBean(ConnectionFactory.class).createConnection().close();
        System.out.println("------------执行完成-------------");
        context.close();
    }
}
