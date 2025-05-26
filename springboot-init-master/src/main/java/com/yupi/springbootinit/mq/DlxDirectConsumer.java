package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class DlxDirectConsumer {

  private static final String WORK_EXCHANGE_NAME = "direct2-exchange";
  private static final String DEAD_EXCHANGE_NAME = "dlx-direct-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();


    // 指定死信队列参数
    Map<String, Object> args = new HashMap<String, Object>();
    // 要绑定到哪个交换机
    args.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
    // 指定死信要转发到哪个死信队列
    args.put("x-dead-letter-routing-key", "other");


    channel.exchangeDeclare(WORK_EXCHANGE_NAME, "direct");
    String queueName1 = "a_queue";
    channel.queueDeclare(queueName1, true, false, false, args);
    channel.queueBind(queueName1, WORK_EXCHANGE_NAME, "aa");

    Map<String, Object> args2 = new HashMap<String, Object>();
    args2.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
    args2.put("x-dead-letter-routing-key", "boss");

    channel.exchangeDeclare(WORK_EXCHANGE_NAME, "direct");
    String queueName2 = "b_queue";
    channel.queueDeclare(queueName2, true, false, false, args2);
    channel.queueBind(queueName2, WORK_EXCHANGE_NAME, "bb");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
        System.out.println(" [aa] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };

    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
        System.out.println(" [bb] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    channel.basicConsume(queueName1, true, deliverCallback1, consumerTag -> { });
    channel.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });
  }
}