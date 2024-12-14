package io.learn.completablefuture.allof;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        ExecutorService fixedTp = Executors.newFixedThreadPool(3);
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        }, fixedTp);

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
                System.out.println(1 / 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 30;
        }, fixedTp);

        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 50;
        }, fixedTp);

        future2 = future2.exceptionallyAsync(ex -> {
            System.out.println("got exception: " + ex.getMessage());
            return null;
        });

        CompletableFuture.allOf(future1, future2, future3);
        long start = System.currentTimeMillis();
        try {
            System.out.println(future1.get());
            System.out.println(future2.get());
            System.out.println(future3.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(System.currentTimeMillis() - start);
    }
}
