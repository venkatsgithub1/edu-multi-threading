package io.exercises.wreentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBlockingQueue {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
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
        try {
            while (capacity == counter) {
                // wait until this condition notFull becomes available.
                notFull.await();
            }
            storage[tail] = val;
            tail = (tail + 1) % capacity;
            counter++;
            // signal any waiting condition on condition that is notEmpty now.
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public int dequeue() throws InterruptedException {
        int numToBeRemoved;
        lock.lock();
        try {
            while (counter == 0) {
                // wait until notEmpty condition is achieved, means, the condition is
                // clarified, yes not empty now.
                notEmpty.await();
            }
            numToBeRemoved = storage[head];
            storage[head] = -1;
            head = (head + 1) % capacity;
            counter--;
            // tell any threads waiting on condition notFull,
            // this means threads using enqueue are waiting for
            // a deque thread to remove atleast 1 number and make
            // notFull condition successful.
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
        return numToBeRemoved;
    }

    public static void main(String[] args) throws InterruptedException {
        BoundedBlockingQueue queue = new BoundedBlockingQueue(3);
        Thread t1 = new Thread(() -> {
            try {
                queue.enqueue(1);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                queue.enqueue(2);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t3 = new Thread(() -> {
            try {
                queue.enqueue(3);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t4 = new Thread(() -> {
            try {
                queue.enqueue(4);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t5 = new Thread(() -> {
            try {
                Thread.sleep(4000);
                System.out.println("Trying deque after 4 seconds");
                System.out.println("got " + queue.dequeue());
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        for (int i = 0; i < 3; i++) {
            System.out.println(queue.storage[i]);
        }
    }
}
