package com.kafka.sandbox.mapper;

import com.kafka.sandbox.dto.TopicDto;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.TopicPartitionInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TopicMapper {

    public TopicDto mapFromTopicDescription(TopicDescription td){
        TopicDto dto = new TopicDto();
        dto.setName(td.name());
        dto.setPartitions(td.partitions().size());

        int replicationFactor = td.partitions()
                .get(0)
                .replicas()
                .size();

        dto.setReplicationFactor(replicationFactor);

        long underReplicated = td.partitions().stream()
                .filter(p -> p.isr().size() < p.replicas().size())
                .count();

        dto.setUnderReplicatedPartitions((int) underReplicated);
        dto.setHealthy(underReplicated == 0);

        return dto;
    }

    public List<TopicDto> mapFromTopicDescriptions(Map<String, TopicDescription> topicsMap) {
        List<TopicDto> topics = new ArrayList<>();
        topicsMap.forEach((topicName, td) -> {
            topics.add(mapFromTopicDescription(td));
        });
        return topics;
    }
}