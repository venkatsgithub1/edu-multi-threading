package io.learn.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3);
        Runnable r = () -> {
            System.out.println("Thread " + Thread.currentThread().getName() + " is waiting on barrier");
            try {
                barrier.await();
                System.out.println("Thread " + Thread.currentThread().getName() + " has crossed barrier");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        Thread t3 = new Thread(r);

        t1.start();
        t2.start();
        t3.start();
    }
}
