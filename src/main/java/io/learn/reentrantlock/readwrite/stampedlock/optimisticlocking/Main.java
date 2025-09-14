package io.learn.reentrantlock.readwrite.stampedlock.optimisticlocking;

import java.util.concurrent.locks.StampedLock;

public class Main {
    public static void main(String[] args) {
        StampedLock stampedLock = new StampedLock();
        var stamp = stampedLock.tryOptimisticRead();
        System.out.println(stamp);
        System.out.println(stampedLock.isReadLocked() + " " + stampedLock.getReadLockCount());
        validateOptimisticReadWWrite(stampedLock);
    }

    private static void validateOptimisticReadWWrite(StampedLock stampedLock) {
        System.out.println("---validating optimistic read---");
        var stamp = stampedLock.tryOptimisticRead();
        System.out.println(stampedLock.validate(stamp));
        var writeStamp = stampedLock.writeLock();
        System.out.println(stampedLock.validate(stamp));
        stampedLock.unlockWrite(writeStamp);
    }
}
