package com.yupi.springbootinit.mq;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;

import java.io.IOException;

public class TtlProducer {
    public static void main(String[] args) throws IOException {
        Environment environment = Environment.builder().build();
        String stream = "hello-java-stream";
        environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(5)).create();

        Producer producer = environment.producerBuilder().stream(stream).build();
        producer.send(producer.messageBuilder().addData("Hello, World!".getBytes()).build(), null);
        System.out.println(" [x] 'Hello, World!' message sent");

        System.out.println(" [x] Press Enter to close the producer...");
        System.in.read();
        producer.close();
        environment.close();
    }
}
