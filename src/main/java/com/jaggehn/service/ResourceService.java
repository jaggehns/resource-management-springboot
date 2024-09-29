package com.jaggehn.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class ResourceService {

    private final BlockingQueue<AccessRequest> requestQueue = new PriorityBlockingQueue<>(
            10, Comparator.comparingInt(AccessRequest::getPriority).reversed());

    private long lastAccessTime = 0;
    private int processedCounter = 0;

    @PostConstruct
    public void startProcessing() {
        Thread workerThread = new Thread(this::processRequests);
        workerThread.setDaemon(true);
        workerThread.start();
    }

    public void addRequest(int priority, String requestId) {
        requestQueue.offer(new AccessRequest(priority, requestId));
        System.out.println("Request sent with ID: " + requestId + " and priority: " + priority);
    }

    private void processRequests() {
        while (true) {
            try {
                AccessRequest request = requestQueue.take();
                long currentTime = System.currentTimeMillis();
                long timeSinceLastAccess = currentTime - lastAccessTime;

                if (timeSinceLastAccess < 2000) {
                    Thread.sleep(2000 - timeSinceLastAccess);
                }

                lastAccessTime = System.currentTimeMillis();
                processedCounter++;
                System.out.println("Request " + processedCounter + " (ID: " + request.getRequestId() + ") processed at " +
                        (lastAccessTime / 1000) + " seconds with priority " + request.getPriority());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Worker thread interrupted.");
            }
        }
    }

    private static class AccessRequest {
        private final int priority;
        private final String requestId;

        public AccessRequest(int priority, String requestId) {
            this.priority = priority;
            this.requestId = requestId;
        }

        public int getPriority() {
            return priority;
        }

        public String getRequestId() {
            return requestId;
        }
    }
}
