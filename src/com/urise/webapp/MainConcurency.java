package com.urise.webapp;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainConcurency {

    final static int THREADS_NUMBER = 10000;
    private static int counter;
    final private static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        final MainConcurency mainConcurency = new MainConcurency();
        CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < THREADS_NUMBER; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurency.inc();
                }
                latch.countDown();
            });

        }
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
        System.out.println(mainConcurency.counter);



    }

    private void inc() {
        lock.lock();
        counter++;
        lock.unlock();
    }




}
