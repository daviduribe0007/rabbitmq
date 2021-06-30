package com.example.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProductorEventos {

    public static final String EVENTOS = "Eventos";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        // abrir conexion AMQ y establecer canal

        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            //Crear fanout exchange "eventos"
            channel.exchangeDeclare(EVENTOS, BuiltinExchangeType.FANOUT);

            int count= 0;
            //Enviar mensaje al fanoutexchange"eventos"
            while (true) {
                String messaje = "evento" + count;
                System.out.println("Produciendo mensaje; "+ messaje);
                channel.basicPublish(EVENTOS, "", null, messaje.getBytes());
                Thread.sleep(1);
                count ++;
            }
        }
    }
}
