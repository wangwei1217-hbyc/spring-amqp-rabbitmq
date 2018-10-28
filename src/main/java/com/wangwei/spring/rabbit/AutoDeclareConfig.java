package com.wangwei.spring.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 1、自动声明（创建）的步骤：
 *      直接把要自动声明的组件Bean纳入到spring容器中管理即可。
        自动声明发生在rabbitmq第一次连接创建的时候。
        如果系统从启动到停止没有创建任何连接，则不会自动创建

        自动声明支持单个和批量。
 *
 * 2、自动声明（创建）的条件：
 *      1)：要有连接Connection产生

        2)：spring容器中要有RabbitAdmin的Bean，且autoStartup属性必须为true（默认）

        3)：如果ConnectionFactory使用的是CachingConnectionFactory，则CacheMode必须要是CacheMode.CHANNEL（默认）,
            CacheMode.CONNECTION不会自动声明.

        4)：所要声明的组件的shouldDeclare必须要是true（默认）

        5)：Exchange、Queue的名字不能以amq.开头.
            --Queue的名字以amq.开头,不能成功声明，不会报异常。
            --Exchange的名字以amq.开头,不能成功声明，会报异常。
            com.rabbitmq.client.ShutdownSignalException:
              channel error; protocol method:
               #method<channel.close>(reply-code=403, reply-text=ACCESS_REFUSED - exchange name 'amq.fanout.exchange' contains reserved prefix 'amq.*', class-id=40, method-id=10)


 */
@Configuration
public class AutoDeclareConfig {

    @Bean
    public DirectExchange directExchange(){
        DirectExchange directExchange = new DirectExchange("spring.direct.exchange", true, false, null);
        return directExchange;
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("spring.topic.exchange",true,false,null);
    }
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("spring.fanout.exchange",true,false,null);
    }
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange("spring.headers.exchange",true,false,null);
    }

    @Bean
    public FanoutExchange fExchange1(){
        return new FanoutExchange("amq.fanout.exchange",true,false,null);
    }

    @Bean
    public List<Exchange> listExchange(){
        List<Exchange> listExchange = new ArrayList<>();
        listExchange.add(new DirectExchange("aa",true,false));
        listExchange.add(new DirectExchange("bb",true,false));
        return listExchange;
    }

    @Bean
    public Queue q1(){
        Queue q1 = new Queue("spring.direct.queue",true,false,false,new HashMap<String,Object>());
        return q1;
    }
    @Bean
    public Queue q2(){
        Queue q2 = new Queue("spring.topic.queue", true);
        q2.setShouldDeclare(true);//true-默认。可以自动声明；设为false，则不会自动声明
        return q2;
    }
    @Bean
    public Queue q3(){
        return new Queue("spring.fanout.queue",true);
    }
    @Bean
    public Queue q4(){
        return new Queue("spring.headers.queue",true);
    }

    @Bean
    public Queue q5(){
        return new Queue("amq.wangwei",true);
    }
    @Bean
    public Binding b1(){
        return new Binding("spring.direct.queue",Binding.DestinationType.QUEUE,
                "spring.direct.exchange","spring.direct",null);
    }
    @Bean
    public List<Binding> listB1(){
        ArrayList<Binding> b1List = new ArrayList<>();
        b1List.add(new Binding("spring.topic.queue",Binding.DestinationType.QUEUE,
                "spring.direct.exchange","spring.topic",null));
        b1List.add(new Binding("spring.fanout.queue",Binding.DestinationType.QUEUE,
                "spring.direct.exchange","spring.fanout",null));
        b1List.add(new Binding("spring.headers.queue",Binding.DestinationType.QUEUE,
                "spring.direct.exchange","spring.headers",null));

        return b1List;
    }

}
