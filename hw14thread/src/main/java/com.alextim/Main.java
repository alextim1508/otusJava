package com.alextim;

public class Main {

    public static class PingPong {
        boolean flag;
    }

    private static final PingPong pingPong = new PingPong();

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(Main::sequencePrint);
        Thread thread2 = new Thread(Main::sequencePrint);

        thread1.setName("thread1");
        thread2.setName("thread2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }

    private static void sequencePrint() {
        for(int i=1; i<10; i++) {
            print(Thread.currentThread().getName(), i);
        }
        for(int i=10; i>0; i--) {
            print(Thread.currentThread().getName(), i);
        }
    }


    private static void print(String threadName, int i) {
        synchronized(pingPong) {
            //while -> spurious wakeup
            while ( (threadName.equals("thread1") && pingPong.flag) ||
                (threadName.equals("thread2") && !pingPong.flag) ) {
                try {
                    pingPong.wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(Thread.currentThread().getName() + " " + i);

            if(threadName.equals("thread1"))
                pingPong.flag = true;
            else if(threadName.equals("thread2"))
                pingPong.flag = false;

            pingPong.notify();
        }

    }



}
