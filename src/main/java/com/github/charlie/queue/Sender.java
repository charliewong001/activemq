package com.github.charlie.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
    // 默认连接用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    // 默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    // 默认连接地址
    private static final String BROKEURL = "tcp://192.168.158.128:61616";
    // 发送的消息数量
    private static final int SENDNUM = 10;

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageProducer messageProducer = null;

        connectionFactory = new ActiveMQConnectionFactory(BROKEURL);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue("hello world");

        messageProducer = session.createProducer(destination);

        sendMessage(session, messageProducer);

        session.commit();

    }

    public static void sendMessage(Session session,
            MessageProducer messageProducer) throws Exception {
        for (int i = 0; i < SENDNUM; i++) {
            TextMessage textMessage = session
                    .createTextMessage("ActiveMQ 发送消息" + i);

            System.out.println("发送消息：ActiveMQ发送消息" + i);
            messageProducer.send(textMessage);
            Thread.sleep(1000);
        }
    }
}