package functional_interfaces;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ProducerConsumerExample {
    public static void main(String[] args) {
        Queue<Integer> buffer = new LinkedList<>();
        int maxSize = 5;

        Thread producerThread = new Thread(new Producer(buffer, maxSize), "Producer");
        Thread consumerThread = new Thread(new Consumer(buffer), "Consumer");

        producerThread.start();
        consumerThread.start();
    }
}

class Producer implements Runnable {
    private Queue<Integer> buffer;
    private int maxSize;

    public Producer(Queue<Integer> buffer, int maxSize) {
        this.buffer = buffer;
        this.maxSize = maxSize;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (buffer) {
                while (buffer.size() == maxSize) {
                    try {
                        System.out.println("Buffer is full, waiting for consumer to consume...");
                        buffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Random random = new Random();
                int item = random.nextInt(100);
                buffer.offer(item);
                System.out.println("Produced: " + item);
                buffer.notifyAll();
            }

            try {
                Thread.sleep(1000); // Producer sleeps for a second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable {
    private Queue<Integer> buffer;

    public Consumer(Queue<Integer> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (buffer) {
                while (buffer.isEmpty()) {
                    try {
                        System.out.println("Buffer is empty, waiting for producer to produce...");
                        buffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int consumed = buffer.poll();
                System.out.println("Consumed: " + consumed);
                buffer.notifyAll();
            }

            try {
                Thread.sleep(2000); // Consumer sleeps for two seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

