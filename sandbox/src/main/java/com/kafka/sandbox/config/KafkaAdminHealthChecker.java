package com.kafka.sandbox.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAdminHealthChecker {
    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServers;
    @Bean
    public AdminClient adminClient(){
        Map<String,Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        return AdminClient.create(configs);
    }
}
