package com.example.demoaws.secrets;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Secrets {

    private String brokerUrl;

    private String brokerUser;

    private String brokerPassword;

    private final Environment environment;

    @Autowired
    public Secrets(final Environment environment) throws JsonProcessingException {
        this.environment = environment;

        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(environment.getProperty("cloud.aws.region.static"))
                .build();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(environment.getProperty("spring.aws.secretsmanager.secretName"));

        String secret = client.getSecretValue(getSecretValueRequest).getSecretString();

        final HashMap<String, String> secretMap = new ObjectMapper().readValue(secret, HashMap.class);

        brokerUrl = secretMap.get("broker-url");
        brokerUser = secretMap.get("user");
        brokerPassword = secretMap.get("password");
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(final String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getBrokerUser() {
        return brokerUser;
    }

    public void setBrokerUser(final String brokerUser) {
        this.brokerUser = brokerUser;
    }

    public String getBrokerPassword() {
        return brokerPassword;
    }

    public void setBrokerPassword(final String brokerPassword) {
        this.brokerPassword = brokerPassword;
    }
}
