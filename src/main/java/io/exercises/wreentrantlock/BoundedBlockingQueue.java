package io.exercises.wreentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBlockingQueue {
    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    private final int capacity;
    private final int[] storage;
    private int counter;
    private int head;
    private int tail;

    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.storage = new int[capacity];
        this.counter = 0;
    }

    public void enqueue(int val) throws InterruptedException {
        lock.lock();
        if (storage.length == counter) {
            notEmpty.await();
        }
        head = (head + 1) % capacity;
    }
}
