package com.example.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ProductorEventosDeportivos {

    public static final String EXCHANGE = "eventos-deportivos";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        // abrir conexion AMQ y establecer canal

        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            //Crear fanout exchange "eventos-deportivos"
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
            //pais: es,fr,usa
            List<String> countries = Arrays.asList("es", "fr", "usa");
            //deporte: futbol,tenis,voleibol
            List<String> sports = Arrays.asList("soccer", "tennis", "baseball");
            //evento: envivo, noticia
            List<String> eventTypes = Arrays.asList("envivo", "noticia");


            int count = 1;
            //Enviar mensaje al fanoutexchange"eventos"
            while (true) {
                shuffle(countries, sports, eventTypes);
                String country = countries.get(0);
                String sport = sports.get(0);
                String eventType = eventTypes.get(0);
               //routingKey -> country.sport.eventType
                String routingKey = country + "."+sport + "."+ eventType;

                String messaje = "evento" + count;
                System.out.println("Produciendo mensaje:  (" + country + "."+sport + "."+ eventType+ "):"+messaje);
                channel.basicPublish(EXCHANGE, routingKey, null, messaje.getBytes());
                Thread.sleep(1000);
                count++;
            }
        }
    }

    private static void shuffle(List<String> countries, List<String> sports, List<String> eventTypes) {
        Collections.shuffle(countries);
        Collections.shuffle(sports);
        Collections.shuffle(eventTypes);
    }
}
