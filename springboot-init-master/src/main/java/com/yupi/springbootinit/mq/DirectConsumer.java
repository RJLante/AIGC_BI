package com.yupi.springbootinit.mq;

import com.rabbitmq.client.*;

public class DirectConsumer {

  private static final String EXCHANGE_NAME = "direct-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    String queueName1 = "小江的工作队列";
    channel.queueDeclare(queueName1, true, false, false, null);
    channel.queueBind(queueName1, EXCHANGE_NAME, "jiang");

    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    String queueName2 = "小张的工作队列";
    channel.queueDeclare(queueName2, true, false, false, null);
    channel.queueBind(queueName2, EXCHANGE_NAME, "zhang");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [小江] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };

    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [小张] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    channel.basicConsume(queueName1, true, deliverCallback1, consumerTag -> { });
    channel.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });
  }
}