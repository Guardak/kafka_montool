package com.kafka.sandbox.rest;

import com.kafka.sandbox.dto.ConsumerGroupDto;
import com.kafka.sandbox.service.KafkaManagementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
@RestController
public class ConsumerController {
    private final KafkaManagementService adminService;

    public ConsumerController(KafkaManagementService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/lag/{groupId}")
    public List<ConsumerGroupDto> clusterHealth(@PathVariable String groupId) throws ExecutionException, InterruptedException {
        return adminService.checkConsumerGroups(groupId);
    }
}
