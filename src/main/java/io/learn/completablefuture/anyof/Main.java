package io.learn.completablefuture.anyof;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
  public static void main(String[] args) {
    CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return 10;
    });

    CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(500);
        System.out.println(1 / 0);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return 10;
    });

    CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return 50;
    });

    // any future that completes first, result of that future comes back.
    CompletableFuture<Object> anyOfRes = CompletableFuture.anyOf(future1, future2, future3);
    try {
      System.out.println(anyOfRes.get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }
}
