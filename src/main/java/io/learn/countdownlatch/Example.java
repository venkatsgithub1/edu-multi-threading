package io.learn.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class Example {
  public static void main(String[] args) {
    CountDownLatch countDownLatch = new CountDownLatch(2);
    Worker worker1 = new Worker(countDownLatch, "Worker 1");
    Worker worker2 = new Worker(countDownLatch, "Worker 2");
    worker1.start();
    worker2.start();
    try {
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Master master = new Master("Master");
    master.start();
    System.out.println("Execution complete");
  }
}
