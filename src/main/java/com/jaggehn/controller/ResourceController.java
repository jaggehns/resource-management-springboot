package com.jaggehn.controller;

import com.jaggehn.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/access")
    public String accessResource() {
        return resourceService.accessResource();
    }
}
