package com.jaggehn.service;

import com.jaggehn.model.AccessRequest;
import com.jaggehn.runnable.ResourceProcessorRunnable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Service
public class ResourceService {

    private final BlockingQueue<AccessRequest> requestQueue = new PriorityBlockingQueue<>(
            10, Comparator.comparingInt(AccessRequest::getPriority).reversed());

    private Thread workerThread;

    @PostConstruct
    public void startProcessing() {
        // Manually managing the thread to keep it simple
        // Better to use ExecutorService for production
        workerThread = new Thread(new ResourceProcessorRunnable(requestQueue));
        workerThread.setDaemon(true);
        workerThread.start();
    }

    // So only one thread can execute at a time
    public synchronized void addRequest(int priority) {
        requestQueue.offer(new AccessRequest(priority));
        System.out.println("Request received with priority: " + priority);
    }

    public void stopProcessing() {
        if (workerThread != null && workerThread.isAlive()) {
            workerThread.interrupt();
        }
    }
}
