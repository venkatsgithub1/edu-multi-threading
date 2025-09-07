package io.learn.reentrantlock.readwrite.stampedlock.readlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * Stamped lock returns a long value, which can be used to unlock
 */
public class Main {
    public static void main(String args[]) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // create an instance of StampedLock
        StampedLock stampedLock = new StampedLock();

        // main thread attempts to acquire the lock twice, which is granted
        long readStamp1 = stampedLock.readLock();
        long readStamp2 = stampedLock.readLock();

        try {

            // create 3 threads
            for (int i = 0; i < 3; i++) {
                executorService.submit(() -> {

                    try {
                        long readStamp = stampedLock.readLock();
                        System.out.println("Read lock count in spawned thread " + stampedLock.getReadLockCount());

                        // simulate thread performing read of shared state
                        try {
                            System.out.println("sleeping " + Thread.currentThread().getName());
                            Thread.sleep(2000);
                        } catch (InterruptedException ie) {
                            // ignore
                        } finally {
                            stampedLock.unlockRead(readStamp);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });
            }
            // let the main thread simulate work for 5 seconds
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // wait for spawned threads to finish
            executorService.shutdown();
            try {
                executorService.awaitTermination(1, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // remember to unlock
            stampedLock.unlock(readStamp1);
            stampedLock.unlock(readStamp2);
        }

        System.out.println("Read lock count in main thread " + stampedLock.getReadLockCount());
        System.out.println("stampedLock.isReadLocked() " + stampedLock.isReadLocked());
    }

}
