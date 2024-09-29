package com.jaggehn.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class ResourceService {

    private final ReentrantLock lock = new ReentrantLock();
    private long lastAccessTime = 0;

    public String accessResource() {
        lock.lock();
        try {
            long currentTime = System.currentTimeMillis();
            long timeSinceLastAccess = currentTime - lastAccessTime;

            if (timeSinceLastAccess < 2000) {
                try {
                    Thread.sleep(2000 - timeSinceLastAccess);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "Thread interrupted while waiting.";
                }
            }

            lastAccessTime = System.currentTimeMillis();
            return "Resource accessed successfully at " + lastAccessTime;
        } finally {
            lock.unlock();
        }
    }
}
