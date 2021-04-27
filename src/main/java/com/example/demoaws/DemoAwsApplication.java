package com.example.demoaws;

import com.example.demoaws.db.DocumentEntity;
import com.example.demoaws.db.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;

import java.util.function.UnaryOperator;

@SpringBootApplication
public class DemoAwsApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DemoAwsApplication.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    private DocumentRepository documentRepository;

    @Bean
    public UnaryOperator<String> handle() {
        return input -> {
            LOG.info("Handler is starting with input string: {}", input);

            String message;
            try {
                jmsTemplate.convertAndSend(environment.getProperty("destination"), input);
                message = (String) jmsTemplate.receiveAndConvert(environment.getProperty("destination"));
            } catch (Exception e) {
                LOG.info("Exception occurred during interaction with JMS. ", e);
                throw new RuntimeException(e);
            }

            String id;
            try {
                final DocumentEntity documentEntity = new DocumentEntity(message);
                id = documentRepository.save(documentEntity).getId().toString();
            } catch (Exception e) {
                LOG.info("Exception occurred during interaction with database. ", e);
                throw new RuntimeException(e);
            }

            LOG.info("Handler is finishing with message {} and id {}", message, id);
            return id;
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoAwsApplication.class, args);
    }

}
