package com.jaggehn.controller;

import com.jaggehn.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/access")
    public String accessResource(@RequestParam(defaultValue = "1") int priority) {
        String requestId = UUID.randomUUID().toString();
        resourceService.addRequest(priority, requestId);
        return "Request added to queue with ID: " + requestId + " and priority: " + priority;
    }
}
