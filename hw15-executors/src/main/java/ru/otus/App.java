package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class.getName());
    private static final int COUNT = 10;
    private static final int START_NUM = 1;

    private int lastThreadNum = 2;

    public static void main(String[] args) {
        var app = new App();
        new Thread(() -> app.printNums(1)).start();
        new Thread(() -> app.printNums(2)).start();
    }

    private synchronized void printNums(int threadNum) {
        var currNum = START_NUM;
        var reverseFlag = false;

        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (lastThreadNum == threadNum) {
                    this.wait();
                }

                var numToPrint = reverseFlag ? currNum-- : currNum++;

                if(currNum == COUNT) {
                    reverseFlag = true;
                }
                if (currNum == START_NUM) {
                    reverseFlag = false;
                }

                log.info("-> {}", numToPrint);
                lastThreadNum = threadNum;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
