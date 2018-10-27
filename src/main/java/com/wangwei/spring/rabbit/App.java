package com.wangwei.spring.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangwei on 2018/10/27 0027.
 */
@ComponentScan
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        System.out.println(rabbitAdmin);


        //以下代码均可重复执行
        //1、定义Exchange
        rabbitAdmin.declareExchange(new DirectExchange("spring.direct.exchange",true,false,null));
        rabbitAdmin.declareExchange(new TopicExchange("spring.topic.exchange",true,false,null));
        rabbitAdmin.declareExchange(new FanoutExchange("spring.fanout.exchange",true,false,null));
        rabbitAdmin.declareExchange(new HeadersExchange("spring.headers.exchange",true,false,null));

        // 删除Exchange
        rabbitAdmin.deleteExchange("spring.headers+.exchange");

        //2、定义Queue
        rabbitAdmin.declareQueue(new Queue("spring.direct.queue",true,false,false,new HashMap<String,Object>()));
        rabbitAdmin.declareQueue(new Queue("spring.topic.queue",true));
        rabbitAdmin.declareQueue(new Queue("spring.fanout.queue",true));
        rabbitAdmin.declareQueue(new Queue("spring.headers.queue",true));

        //删除Queue
//        rabbitAdmin.deleteQueue("spring.headers.queue");

        //消费消息：purge操作
//        rabbitAdmin.purgeQueue("spring.direct.queue",false);


        //3、绑定
        //3.1 Exchange和Queue绑定
        rabbitAdmin.declareBinding(new Binding("spring.direct.queue",Binding.DestinationType.QUEUE,"spring.direct.exchange","spring.direct",null));
        rabbitAdmin.declareBinding(new Binding("spring.topic.queue",Binding.DestinationType.QUEUE,"spring.direct.exchange","spring.topic",null));
        rabbitAdmin.declareBinding(new Binding("spring.fanout.queue",Binding.DestinationType.QUEUE,"spring.direct.exchange","spring.fanout",null));
        rabbitAdmin.declareBinding(new Binding("spring.headers.queue",Binding.DestinationType.QUEUE,"spring.direct.exchange","spring.headers",null));

        //解绑
//        rabbitAdmin.removeBinding(new Binding("spring.topic.queue",Binding.DestinationType.QUEUE,"spring.direct.exchange","spring.direct",null));
//        rabbitAdmin.removeBinding(new Binding("spring.fanout.queue",Binding.DestinationType.QUEUE,"spring.direct.exchange","spring.direct",null));
//        rabbitAdmin.removeBinding(new Binding("spring.headers.queue",Binding.DestinationType.QUEUE,"spring.direct.exchange","spring.direct",null));


        //绑定第二种方式：通过BindingBuilder对象
        //绑定TopicExchange，需要通过with方法指定routingKey
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.direct.queue")).to(new TopicExchange("spring.topic.exchange")).with("direct.#"));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.topic.queue")).to(new TopicExchange("spring.topic.exchange")).with("topic.#"));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.fanout.queue")).to(new TopicExchange("spring.topic.exchange")).with("fanout.#"));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.headers.queue")).to(new TopicExchange("spring.topic.exchange")).with("headers.#"));

        //绑定FanoutExchange，不需要指定routingKey
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.direct.queue")).to(new FanoutExchange("spring.fanout.exchange")));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.topic.queue")).to(new FanoutExchange("spring.fanout.exchange")));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.fanout.queue")).to(new FanoutExchange("spring.fanout.exchange")));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.headers.queue")).to(new FanoutExchange("spring.fanout.exchange")));

        //绑定HeadersExchange,不需要routingKey，需要制定header参数，whereAll-参数全部匹配；whereAny-参数部分匹配即可。
        Map<String,Object> headerValues = new HashMap<String,Object>();
        headerValues.put("name","direct");
        headerValues.put("age",18);
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.direct.queue")).to(new HeadersExchange("spring.headers.exchange")).whereAll(headerValues).match());
        headerValues.put("name","topic");
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.topic.queue")).to(new HeadersExchange("spring.headers.exchange")).whereAll(headerValues).match());
        headerValues.put("name","fanout");
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.fanout.queue")).to(new HeadersExchange("spring.headers.exchange")).whereAny(headerValues).match());
        headerValues.put("name","headers");
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.headers.queue")).to(new HeadersExchange("spring.headers.exchange")).whereAny(headerValues).match());

        Map<String,Object> headerValues2 = new HashMap<String,Object>();
        headerValues2.put("name","wangwei");
        headerValues2.put("age",18);
        rabbitAdmin.removeBinding(BindingBuilder.bind(new Queue("spring.direct.queue")).to(new HeadersExchange("spring.headers.exchange")).whereAll(headerValues2).match());

        //3.2 Exchange和Exchange绑定
        rabbitAdmin.declareExchange(new DirectExchange("wangwei.from.exchange"));
        rabbitAdmin.declareExchange(new DirectExchange("wangwei.to.exchange"));
        //From:wangwei.from.exchange To:wangwei.to.exchange

        rabbitAdmin.declareBinding(new Binding("wangwei.to.exchange",Binding.DestinationType.EXCHANGE,"wangwei.from.exchange","wangwei",null));
//        rabbitAdmin.declareBinding(BindingBuilder.bind(new DirectExchange("wangwei.to.exchange")).to(new DirectExchange("wangwei.from.exchange")).with("wangwei"));

        System.out.println("=====over======");
        context.close();
    }
}
