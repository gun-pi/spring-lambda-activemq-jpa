package com.example.demoaws;

import com.example.demoaws.db.DocumentEntity;
import com.example.demoaws.db.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;

import java.util.function.UnaryOperator;

@SpringBootApplication
public class DemoAwsApplication {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    private DocumentRepository documentRepository;

    @Bean
    public UnaryOperator<String> handle() {
        return input -> {
            jmsTemplate.convertAndSend(environment.getProperty("destination"), input);
            String string = (String) jmsTemplate.receiveAndConvert(environment.getProperty("destination"));
            final DocumentEntity documentEntity = new DocumentEntity(string);
            return documentRepository.save(documentEntity).getId().toString();
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoAwsApplication.class, args);
    }

}
