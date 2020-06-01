package com.example.recipeworld;

public class User {
    private int mId;
    private String mName;
    private String mID;
    private String mPassword;

    public User(int mId, String mName, String mID, String mPassword) {
        this.mId = mId;
        this.mName = mName;
        this.mID = mID;
        this.mPassword = mPassword;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getPseudo() {
        return mName;
    }

    public void setPseudo(String mPseudo) {
        this.mName = mPseudo;
    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
