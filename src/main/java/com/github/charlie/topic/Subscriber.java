package com.github.charlie.topic;

import java.util.concurrent.CountDownLatch;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Subscriber implements Runnable {

    private String threadName;

    public Subscriber(String name) {
        this.threadName = name;
    }

    public static void main(String[] args) {
        Subscriber s1 = new Subscriber("thread1");
        Subscriber s2 = new Subscriber("thread2");
        Subscriber s3 = new Subscriber("thread3");
        Thread t1 = new Thread(s1);
        Thread t2 = new Thread(s2);
        Thread t3 = new Thread(s3);
        t1.start();
        t2.start();
        t3.start();
    }

    @Override
    public void run() {
        ConnectionFactory factory = null;
        Connection connection = null;
        Session session = null;
        try {
            factory = new ActiveMQConnectionFactory(
                    "tcp://192.168.158.128:61616");
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("hello world topic");
            MessageConsumer consumer = session.createConsumer(destination);
            final CountDownLatch latch = new CountDownLatch(1);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        if (message instanceof TextMessage) {
                            String text = ((TextMessage) message).getText();
                            if ("END".equals(text)) {
                                latch.countDown();
                            }
                            System.out.println(
                                    "线程:" + threadName + "收到消息:" + text);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            latch.await();
            consumer.close();
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
