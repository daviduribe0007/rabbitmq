package com.example.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class productorSimple {

    public static void main(String[] args) throws IOException, TimeoutException {
        String message = "tercer mensaje";
        // abrir conexion AMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (Connection connection = connectionFactory.newConnection();
             //establecer canal
             Channel channel = connection.createChannel()) {
            //crear cola
            String queueName = "primera-cola";
            channel.queueDeclare(queueName, false, false, false, null);
            //Enviar mensaje
            channel.basicPublish("", queueName, null, message.getBytes());
        }


    }
}
