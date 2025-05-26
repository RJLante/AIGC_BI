package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class TopicConsumer {

  private static final String EXCHANGE_NAME = "topic-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "topic");

    String queueName1 = "frontend_queue";
    channel.queueDeclare(queueName1, true, false, false, null);
    channel.queueBind(queueName1, EXCHANGE_NAME, "#.frontend.#");

    String queueName2 = "product_queue";
    channel.queueDeclare(queueName2, true, false, false, null);
    channel.queueBind(queueName2, EXCHANGE_NAME, "#.backend.#");

    String queueName3 = "backend_queue";
    channel.queueDeclare(queueName3, true, false, false, null);
    channel.queueBind(queueName3, EXCHANGE_NAME, "#.product.#");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [a] Received '" +
                  delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };

    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [b] Received '" +
                delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };

    DeliverCallback deliverCallback3 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [c] Received '" +
                delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };

    channel.basicConsume(queueName1, true, deliverCallback1, consumerTag -> { });
    channel.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });
    channel.basicConsume(queueName3, true, deliverCallback3, consumerTag -> { });
  }
}