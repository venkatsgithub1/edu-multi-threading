package io.practice.cyclicbarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TestScenario {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
            System.out.println("all the threads reached the barrier");
        });
        Runnable runnable = () -> {
            var sleepTime = new Random().nextInt(2000);
            System.out.println(Thread.currentThread().getName() + " is going to sleep for " + sleepTime);
            try {
                cyclicBarrier.await();
                Thread.sleep(sleepTime);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
