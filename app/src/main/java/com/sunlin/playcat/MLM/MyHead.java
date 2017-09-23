package com.sunlin.playcat.MLM;

/**
 * Created by sunlin on 2017/5/1.
 */

public class MyHead {
    private   int v;
    private  int t;
    private  int d;
    private  int e;
    private  int l;
    private  long from;
    private  long  to;
    public  static  int size=16;

    public int getD() {
        return d;
    }

    public int getE() {
        return e;
    }

    public int getL() {
        return l;
    }

    public int getT() {
        return t;
    }

    public int getV() {
        return v;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public void setD(int d) {
        this.d = d;
    }

    public void setE(int e) {
        this.e = e;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public void setL(int l) {
        this.l = l;
    }

    public void setT(int t) {
        this.t = t;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public void setV(int v) {
        this.v = v;
    }
}