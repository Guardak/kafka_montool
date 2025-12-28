package com.kafka.sandbox.dto;

import lombok.Data;

@Data
public class ConsumerGroupDto {
    String groupId;
    String topic;
    int partition;
    long committedOffset;
    long endOffset;
    long lag = endOffset - committedOffset;
}
