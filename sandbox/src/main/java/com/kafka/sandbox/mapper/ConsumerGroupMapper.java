package com.kafka.sandbox.mapper;

import com.kafka.sandbox.dto.ConsumerGroupDto;
import com.kafka.sandbox.service.ConsumerService;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class ConsumerGroupMapper {

    ConsumerService consumerService;
    public ConsumerGroupMapper(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }
    public List<ConsumerGroupDto> mapFromConsumerGroup(ListConsumerGroupOffsetsResult cons, String groupId) throws ExecutionException, InterruptedException {
        List<ConsumerGroupDto> consumerGroupDtoList = new ArrayList<>();
        Map<TopicPartition, OffsetAndMetadata> offsets = cons.partitionsToOffsetAndMetadata(groupId).get();
        System.out.println("Partitions: " + cons.partitionsToOffsetAndMetadata(groupId).get().size());

        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
            ConsumerGroupDto consDto = new ConsumerGroupDto();
            consDto.setGroupId(groupId);
            TopicPartition tp = entry.getKey();
            OffsetAndMetadata oam = entry.getValue();
            consDto.setTopic(tp.topic());
            consDto.setPartition(tp.partition());
            consDto.setCommittedOffset(oam.offset());
            consDto.setEndOffset(consumerService.getEndOffset(tp));
            consumerGroupDtoList.add(consDto);
        }
        return consumerGroupDtoList;
    }
}
