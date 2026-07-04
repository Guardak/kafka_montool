package com.kafka.sandbox.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class KafkaLagMetricsPublisher {

    private final AdminClient adminClient;
    private final MeterRegistry meterRegistry;

    private final KafkaManagementService adminService;

    private final Map<String, AtomicLong> lagStorage = new ConcurrentHashMap<>();

    public KafkaLagMetricsPublisher(AdminClient adminClient,MeterRegistry meterRegistry, KafkaManagementService adminService) {
        this.adminClient = adminClient;
        this.meterRegistry = meterRegistry;
        this.adminService = adminService;
    }
    private void registerLagGauge() {
        String key = "test-topic" + "-" + 1;
        lagStorage.putIfAbsent(key, new AtomicLong(0));

        Gauge.builder("kafka_consumer_group_lag", lagStorage.get(key), AtomicLong::get)
                .tag("group", "lag-monitor")
                .tag("topic", "test-topic")
                .tag("partition", String.valueOf(1))
                .description("Current lag for Kafka consumer group")
                .register(meterRegistry);
    }

    @Scheduled(fixedRate = 15000)
    public void updateLags() throws ExecutionException, InterruptedException {

        long currentLag = adminService.checkConsumerGroups("test-group").get(1).getLag();

        registerLagGauge();

        lagStorage.get("orders-0").set(currentLag);
    }
}
