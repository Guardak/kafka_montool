package com.kafka.sandbox.rest;

import com.kafka.sandbox.dto.TopicDto;
import com.kafka.sandbox.service.KafkaManagementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/topics")
public class TopicsController {

    private final KafkaManagementService adminService;

    public TopicsController(KafkaManagementService adminService) {
        this.adminService = adminService;
    }
    @GetMapping("/")
    public List<TopicDto> checkTopics() throws ExecutionException, InterruptedException {
        return adminService.checkTopics();
    }

    @GetMapping("/{name}")
    public TopicDto checkTopic(@PathVariable String name) throws ExecutionException, InterruptedException {
        return adminService.checkTopic(name);
    }
}
