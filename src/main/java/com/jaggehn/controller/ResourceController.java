package com.jaggehn.controller;

import com.jaggehn.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/resource")
@Validated
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/access")
    public ResponseEntity<String> accessResource(@RequestParam(defaultValue = "1") @Min(1) int priority) {
        resourceService.addRequest(priority);
        return ResponseEntity.status(HttpStatus.OK).body("Request added to queue with priority: " + priority);
    }
}
