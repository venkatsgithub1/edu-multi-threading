package io.learn.reentrantlock.readwrite.stampedlock.optimisticlocking.multiplication;

import java.util.concurrent.locks.StampedLock;

public class Main {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3};
        arr[0] = 2;
        arr[1] = 5;
        arr[2] = 6;
        System.out.println(getProduct(arr, new StampedLock()));
    }

    private static int getProduct(int[] arr, StampedLock stampedLock) {
        long stamp = stampedLock.tryOptimisticRead();
        int a = arr[0];
        int b = arr[1];
        int c = arr[2];
        if (!stampedLock.validate(stamp)) {
            try {
                stamp = stampedLock.readLock();
                a = arr[0];
                b = arr[1];
                c = arr[2];
                return a * b * c;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return a * b * c;
    }
}
