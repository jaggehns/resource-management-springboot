package com.jaggehn.service;

import com.jaggehn.model.AccessRequest;
import com.jaggehn.runnable.ResourceProcessorRunnable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ResourceService {

    private final BlockingQueue<AccessRequest> requestQueue = new PriorityBlockingQueue<>(
            10, Comparator.comparingInt(AccessRequest::getPriority).reversed());

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void startProcessing() {
        executorService.submit(new ResourceProcessorRunnable(requestQueue));
    }

    public void addRequest(int priority) {
        requestQueue.offer(new AccessRequest(priority));
        System.out.println("Request received with priority: " + priority);
    }

    public void stopProcessing() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
