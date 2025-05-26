package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class DlxDirectProducer {


  private static final String DEAD_EXCHANGE_NAME = "dlx-direct-exchange";
  private static final String WORK_EXCHANGE_NAME = "direct2-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        // 声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, "direct");

        String queueName1 = "boss_dlx_queue";
        channel.queueDeclare(queueName1, true, false, false, null);
        channel.queueBind(queueName1, DEAD_EXCHANGE_NAME, "boss");

        String queueName2 = "other_dlx_queue";
        channel.queueDeclare(queueName2, true, false, false, null);
        channel.queueBind(queueName2, DEAD_EXCHANGE_NAME, "other");


        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String userInput = scanner.nextLine();
            String[] strings = userInput.split(" ");
            if (strings.length < 1) {
                continue;
            }
            String message = strings[0];
            String routingKey = strings[1];

            channel.basicPublish(WORK_EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + " with routing: " + routingKey + "'");
        }


    }
  }
  //..
}