package com.kafka.sandbox.service;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.Collection;
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

        System.out.println("Podłączono do klastra! Znalezione tematy:");
        names.forEach(System.out::println);
        DescribeClusterResult clusterResult = adminClient.describeCluster();
        Collection<Node> nodes = clusterResult.nodes().get();
        for ( Node node: nodes
             ) {
            System.out.println(node.id());
            System.out.println(node.host());
            System.out.println(node.port());
        }
        System.out.println("Size of the cluster: " + nodes.size());
        Node controller  = clusterResult.controller().get();
        System.out.println("Controler id: " + controller.id());
        DescribeTopicsResult topicsResult = adminClient.describeTopics(names);
        System.out.println(topicsResult);
    }
}