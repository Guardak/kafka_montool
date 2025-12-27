package com.kafka.sandbox.mapper;

import com.kafka.sandbox.dto.ConsumerGroupDto;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class ConsumerGroupMapper {

    public ConsumerGroupDto mapFromConsumerGroup(ListConsumerGroupOffsetsResult cons, String groupId) throws ExecutionException, InterruptedException {
        ConsumerGroupDto consDto = new ConsumerGroupDto();
        Map<TopicPartition, OffsetAndMetadata> offsets = cons.partitionsToOffsetAndMetadata(groupId).get();
        System.out.println(cons.partitionsToOffsetAndMetadata(groupId).get().size());
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
            TopicPartition tp = entry.getKey();
            OffsetAndMetadata oam = entry.getValue();
            consDto.setGroupId("puste");
            consDto.setTopic(tp.topic());
            consDto.setPartition(tp.partition());
            consDto.setCommittedOffset(oam.offset());
            consDto.setLag(1L);
        }
        return consDto;
    }
}
