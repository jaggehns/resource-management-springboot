package com.jaggehn.runnable;

import com.jaggehn.model.AccessRequest;

import java.util.concurrent.BlockingQueue;

public class ResourceProcessorRunnable implements Runnable {

    private final BlockingQueue<AccessRequest> requestQueue;
    private long lastAccessTime = 0;
    private int processedCounter = 0;

    public ResourceProcessorRunnable(BlockingQueue<AccessRequest> requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                AccessRequest request = requestQueue.take();
                waitForNextSlot();
                processCurrentRequest(request);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Worker thread interrupted. Stopping request processing.");
                break;
            }
        }
    }

    private void waitForNextSlot() throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastAccess = currentTime - lastAccessTime;

        if (timeSinceLastAccess < 2000) {
            Thread.sleep(2000 - timeSinceLastAccess);
        }
    }

    // So only one thread can execute at a time
    private synchronized void processCurrentRequest(AccessRequest request) {
        lastAccessTime = System.currentTimeMillis();
        processedCounter++;
        System.out.println("Request #" + processedCounter + " processed at " +
                (lastAccessTime / 1000) + " sec, priority: " + request.getPriority());
    }
}
