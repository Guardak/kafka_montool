package com.kafka.sandbox.service;

import com.kafka.sandbox.dto.ClusterHealth;
import com.kafka.sandbox.dto.TopicDto;
import com.kafka.sandbox.mapper.TopicMapper;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KafkaManagementService {

    private final AdminClient adminClient;
    ListTopicsResult topics;
    DescribeClusterResult clusterResult;
    Set<String> names;
    Collection<Node> nodes;

    DescribeTopicsResult topicsResult;

    TopicMapper topicMapper;
    public KafkaManagementService(AdminClient adminClient,TopicMapper topicMapper) throws ExecutionException, InterruptedException {
        this.adminClient = adminClient;
        this.topics = adminClient.listTopics();
        this.clusterResult = adminClient.describeCluster();
        this.names = topics.names().get();
        this.nodes = clusterResult.nodes().get();
        this.topicsResult = adminClient.describeTopics(names);
        this.topicMapper = topicMapper;
    }

    @PostConstruct
    public void initialCheck() throws ExecutionException, InterruptedException {

        for (Node node : nodes
        ) {
            System.out.println(node.id() + "; " + node.host() + "; " + node.port());
        }
        System.out.println("Size of the cluster: " + nodes.size());
        Node controller = clusterResult.controller().get();
        if (controller.isEmpty()) {
            System.out.println("Cluster unhealthy");
        } else {
            System.out.println("Controller is elected");
            System.out.println("Controler id: " + controller.id());
        }
        DescribeTopicsResult topicsResult = adminClient.describeTopics(names);
        Map<String, TopicDescription> topicsMap = topicsResult.allTopicNames().get();

        topicsMap.forEach((topicName, td) -> {
            System.out.println(td.toString());
            for (TopicPartitionInfo p : td.partitions()) {
                System.out.println(p.toString());
                if (p.isr().size() < p.replicas().size()) {
                    System.out.println("Problem with topic" + td.name());
                } else {
                    System.out.println("Topic " + td.name() + " healthy!");
                }
            }
        });


//        }
//        for (TopicDescription td: topicsMap
//             ) {
//            TopicDescription td = topicsMap.get("test-topic");
//        }

    }

    public ClusterHealth checkCluster() throws ExecutionException, InterruptedException {
        Node controller = clusterResult.controller().get();
        Map<String, TopicDescription> topicsMap = topicsResult.allTopicNames().get();
        AtomicInteger i = new AtomicInteger();
        topicsMap.forEach((topicName, td) -> {
            for (TopicPartitionInfo p : td.partitions()) {
                if (p.isr().size() < p.replicas().size()) {
                    i.getAndIncrement();
                }
            }
        });
        return new ClusterHealth(nodes.size(), i.get(), !controller.isEmpty());
    }

    public TopicDto checkTopic(String name) throws ExecutionException, InterruptedException {

        Map<String, TopicDescription> topicsMap = topicsResult.allTopicNames().get();
        return topicMapper.mapFromTopicDescription(topicsMap.get(name));
    }

    public List<TopicDto> checkTopics() throws ExecutionException, InterruptedException {
        Map<String, TopicDescription> topicsMap = topicsResult.allTopicNames().get();
        return topicMapper.mapFromTopicDescriptions(topicsMap);
    }
}