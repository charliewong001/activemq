package com.github.charlie.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageProducer producer = null;
        try {
            connectionFactory = new ActiveMQConnectionFactory(
                    "tcp://192.168.158.128:61616");
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic("hello world topic");
            producer = session.createProducer(destination);

            for (int i = 0; i < 100; i++) {
                TextMessage message = session
                        .createTextMessage("Message # " + i);
                System.out.println("sending message #" + i);
                producer.send(message);
                Thread.sleep(100);
            }
            producer.send(session.createTextMessage("END"));
            producer.close();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
