package com.kafka.sandbox.dto;

import lombok.Data;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;

import java.util.List;

@Data
public class TopicDto {
    private String name;
    private int partitions;
    private int replicationFactor;
    private int underReplicatedPartitions;
    private boolean healthy;
}
