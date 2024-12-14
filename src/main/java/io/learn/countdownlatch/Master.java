package io.learn.countdownlatch;

public class Master extends Thread {
  public Master(String name) {
    super(name);
  }

  @Override
  public void run() {
    System.out.println("Executing master");
  }
}
