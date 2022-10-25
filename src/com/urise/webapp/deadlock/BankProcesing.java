package com.urise.webapp.deadlock;

public class BankProcesing {
    public static void main(String[] args) throws InterruptedException {

        BankAccount bankAccount1 = new BankAccount("first", 1000);
        BankAccount bankAccount2 = new BankAccount("second",1500);

        System.out.println(bankAccount1);
        System.out.println(bankAccount2);

        Thread firstThread = new Thread(() -> bankAccount1.transferTo(bankAccount2, 200));
        firstThread.start();

        Thread secondThread = new Thread(() -> bankAccount2.transferTo(bankAccount1, 400));
        secondThread.start();

        firstThread.join();
        secondThread.join();

        System.out.println(bankAccount1);
        System.out.println(bankAccount2);
    }


}
