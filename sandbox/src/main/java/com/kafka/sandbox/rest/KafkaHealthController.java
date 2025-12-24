package com.kafka.sandbox.rest;

import com.kafka.sandbox.dto.ClusterHealth;
import com.kafka.sandbox.service.KafkaManagementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/health")
public class KafkaHealthController {

    private final KafkaManagementService adminService;

    public KafkaHealthController(KafkaManagementService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/cluster")
    public ClusterHealth clusterHealth() throws ExecutionException, InterruptedException {
        return adminService.checkCluster();
    }
}
