package io.exercises;

public class BoundedBlockingQueue {
    private final int[] storage;
    private int counter = 0;
    private int head = 0;
    private int tail = 0;
    private final int capacity;
    private final Object lock = new Object();

    public BoundedBlockingQueue(int capacity) {
        this.storage = new int[capacity];
        this.capacity = capacity;
    }

    public void enqueue(int val) throws InterruptedException {
        synchronized (lock) {
            while (counter == storage.length) {
                lock.wait();
            }
            Thread.sleep(1000);
            // at 0 -> 1%3, 1 -> 2%3, 2 ->  3%3 (reset).
            storage[tail] = val;
            tail = (tail + 1) % capacity;
            counter++;
            lock.notifyAll();
        }
    }

    public int dequeue() throws InterruptedException {
        int toBeReturned;
        synchronized (lock) {
            while (counter == 0) {
                lock.wait();
            }
            // same as with tail.
            toBeReturned = storage[head];
            storage[head] = -1;
            head = (head + 1) % capacity;
            counter--;
            System.out.println("dequeued");
            lock.notifyAll();
        }
        return toBeReturned;
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
