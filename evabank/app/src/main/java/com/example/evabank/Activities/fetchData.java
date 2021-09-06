package com.example.evabank.Activities;

public class fetchData {

    String type,account,amount,balance;
    public fetchData(String type, String account, String amount, String balance) {
        this.type = type;
        this.account = account;
        this.amount = amount;
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
