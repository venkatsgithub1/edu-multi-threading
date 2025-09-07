package io.learn.reentrantlock.readwrite.stampedlock.writelock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

public class Main {
    public static void main(String[] args) {
        StampedLock stampedLock = new StampedLock();
        long writeStamp = stampedLock.writeLock();
        System.out.println("acquired write lock");
        ExecutorService es = Executors.newFixedThreadPool(1);
        es.submit(() -> {
            long readStamp = 0;
            try {
                Thread.sleep(20);
                readStamp = stampedLock.readLock();
                System.out.println("acquired read lock");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println("releasing read lock");
                stampedLock.unlock(readStamp);
            }
        });
        try {
            es.shutdown();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Releasing write lock");
        stampedLock.unlock(writeStamp);
    }
}
