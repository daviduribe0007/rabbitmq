package com.example.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ConsumidorEventosDeportivos {

    public static final String EXCHANGE = "eventos-deportivos";

    public static void main(String[] args) throws IOException, TimeoutException {
        //Abrir conexion
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        //Establecer canal
        Channel channel = connection.createChannel();
        // Declarar exchange"eventos"
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
        //Crear cola y asociarla al exchange "eventos"
        String queueName = channel.queueDeclare().getQueue();
        //Patron routing-key -> country.sport.eventType
        //*-> identifica una palabra
        //#-> identifica multiples palabras delimitadas por .
        // eventos tenis -> *.tenis.*
        //eventos españa -> es.# / es.*.*
        //Todos loseventos -> #
        System.out.println("Ingrese routing-key");
        Scanner scanner = new Scanner(System.in);
        String routingKey = scanner.nextLine();
        channel.queueBind(queueName, EXCHANGE,routingKey);
        //Crear subscripcion a una cola asociada al exchange "eventos"
        channel.basicConsume(queueName,
                false,
                ((consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensaje recibido " + messageBody);
                    System.out.println("Routing Key " + message.getEnvelope().getRoutingKey());
                }),
                consumerTag -> {
                    System.out.println("Consumidor" + consumerTag + "Cancelado");
                });
    }
}
