package com.library.repository.models;

public class TagColor {
    private int corlor;
    private int back1;
    private int back;

    public TagColor(int corlor, int back, int back1) {
        this.back = back;
        this.back1 = back1;
        this.corlor = corlor;
        this.corlor = corlor;
    }

    public int getCorlor() {
        return corlor;
    }

    public void setCorlor(int corlor) {
        this.corlor = corlor;
    }

    public int getBack1() {
        return back1;
    }

    public void setBack1(int back1) {
        this.back1 = back1;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }
}
