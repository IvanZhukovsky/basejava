package com.urise.webapp.deadlock;

public class BankAccount {

    private String id;
    private int balance;

    public BankAccount(String id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public synchronized void deposit(int amount) {
        int newBalace = balance + amount;
        balance = newBalace;
    }

    public synchronized void withdraw(int amount) {
        int newBalance = balance - amount;
        balance = newBalance;
    }

    public synchronized void transferTo(BankAccount targetAccount, int amount){
        withdraw(amount);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        targetAccount.deposit(amount);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "balance=" + balance +
                ", id='" + id + '\'' +
                '}';
    }
}
