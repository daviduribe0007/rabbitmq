package com.example.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class ConsumidorSimple {

    public static void main(String[] args) throws IOException, TimeoutException {
        //Abrir conexion AMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        //establecer canal
        Channel channel = connection.createChannel();
        //declarar la primer cola
        String queueName = "primera-cola";
        channel.queueDeclare(queueName, false, false, false, null);
        //crear subscripcion a la cola "primer-cola" usando el comando basic.consume
        channel.basicConsume(queueName, true,
                ((consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensaje " + messageBody);
                    System.out.println("Exchange " + message.getEnvelope().getExchange());
                    System.out.println("Routing Key " + message.getEnvelope().getRoutingKey());
                    System.out.println("Delivery tag " + message.getEnvelope().getDeliveryTag());
                }),
                consumerTag -> {
                    System.out.println("Consumidor" + consumerTag + "Cancelado");
                });
    }

}

