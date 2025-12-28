package com.kafka.sandbox.service;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerService {

    private final KafkaConsumer<String, String> consumer;

    public ConsumerService(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    public long getEndOffset(TopicPartition tp) {
        consumer.assign(List.of(tp));
        consumer.seekToEnd(List.of(tp));
        return consumer.position(tp);
    }
}
