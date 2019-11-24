package com.example.recipeworld;

public class User {
    private int mId;
    private String mPseudo;
    private String mID;
    private String mPassword;

    public User(int mId, String mPseudo, String mID, String mPassword) {
        this.mId = mId;
        this.mPseudo = mPseudo;
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
        return mPseudo;
    }

    public void setPseudo(String mPseudo) {
        this.mPseudo = mPseudo;
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
