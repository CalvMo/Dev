package com.example.evabank.Activities;

public class UserHelperClass {
    String id;
    String name;
    String email;
    String authpassword;
    String android_id;



    String balance;

    public UserHelperClass() {}

    public UserHelperClass(String android_id, String balance) {
        this.android_id = android_id;
        this.balance = balance;
    }

    public UserHelperClass(String id, String name, String email, String authpassword, String android_id, String Balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.android_id = android_id;
        this.authpassword = authpassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }

    public String getAuthpassword() {
        return authpassword;
    }

    public void setAuthpassword(String authpassword) {
        this.authpassword = authpassword;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
