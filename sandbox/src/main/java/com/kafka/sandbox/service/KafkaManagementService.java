package com.kafka.sandbox.service;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.Uuid;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaManagementService {

    private final AdminClient adminClient;

    public KafkaManagementService(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @PostConstruct
    public void checkTopics() throws ExecutionException, InterruptedException {
        ListTopicsResult topics = adminClient.listTopics();
        Set<String> names = topics.names().get();

        DescribeClusterResult clusterResult = adminClient.describeCluster();
        Collection<Node> nodes = clusterResult.nodes().get();
        for ( Node node: nodes
             ) {
            System.out.println(node.id() + "; " + node.host() + "; " + node.port());
        }
        System.out.println("Size of the cluster: " + nodes.size());
        Node controller  = clusterResult.controller().get();
        if(controller.isEmpty()){
            System.out.println("Cluster unhealthy");
        }
        else{
            System.out.println("Controller is elected");
            System.out.println("Controler id: " + controller.id());
        }
        DescribeTopicsResult topicsResult = adminClient.describeTopics(names);
        Map<String, TopicDescription> topicsMap = topicsResult.allTopicNames().get();

        topicsMap.forEach((topicName, td) -> {
            System.out.println(td.toString());
            for (TopicPartitionInfo p : td.partitions()) {
                System.out.println(p.toString());
                if(p.isr().size() < p.replicas().size()){
                    System.out.println("Problem with topic" + td.name());
                }
                else{
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
}