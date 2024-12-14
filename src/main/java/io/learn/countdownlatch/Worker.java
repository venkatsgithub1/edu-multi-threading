package io.learn.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class Worker extends Thread {
  private final CountDownLatch countDownLatch;

  public Worker(CountDownLatch countDownLatch, String name) {
    super(name);
    this.countDownLatch = countDownLatch;
  }

  @Override
  public void run() {
    System.out.println("Executing worker");
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    countDownLatch.countDown();
  }
}
