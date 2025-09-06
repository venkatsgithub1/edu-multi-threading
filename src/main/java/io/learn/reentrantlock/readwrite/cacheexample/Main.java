package io.learn.reentrantlock.readwrite.cacheexample;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    private static ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static Map<Integer, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        Runnable readTask = () -> {
            readThread(rwLock, map);
        };
        Runnable writeTask = () -> {
            writeThread(rwLock, map);
        };

        List<Future<?>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        futures.add(executorService.submit(writeTask));
        for (int i = 0; i < 2; i++) {
            futures.add(executorService.submit(readTask));
        }
        futures.add(executorService.submit(writeTask));
        for (int i = 0; i < 2; i++) {
            futures.add(executorService.submit(readTask));
        }
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("Shutting down");
        executorService.shutdown();
    }

    private static void writeThread(ReadWriteLock rwLock, Map<Integer, Integer> map) {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Trying to acquire write lock");
        try {
            rwLock.writeLock().lock();
            System.out.println("acquired write lock");
            map.put(123, new Random().nextInt(1, 100));
        } finally {
            System.out.println("unlocking");
            rwLock.writeLock().unlock();
        }
    }

    private static void readThread(ReadWriteLock rwLock, Map<Integer, Integer> map) {
        try {
            Thread.sleep(30);
            rwLock.readLock().lock();
            System.out.println("reading from cache: " + map.get(123) + " " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
