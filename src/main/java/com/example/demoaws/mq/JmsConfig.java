package com.example.demoaws.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;


@Configuration
public class JmsConfig {

    private static final String BROKER_USER = "activemq";
    private static final String BROKER_PASSWORD = "exampleexample";
    private static final String BROKER_URL =
            "ssl://b-82646c3d-9ad4-4a2a-be59-a244daa6e033-1.mq.eu-central-1.amazonaws.com:61617";

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(activeMQConnectionFactory());
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        final ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory(BROKER_USER, BROKER_PASSWORD, BROKER_URL);
        return activeMQConnectionFactory;
    }
}
