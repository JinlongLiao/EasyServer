package io.github.jinlongliao.easy.server.cached;

import io.github.jinlongliao.easy.server.mapper.annotation.Mapping;

import java.util.Date;

public class A {
    private int a;
    private B b;
    private boolean c;
    private String d;

    private Date date;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public boolean isC() {
        return c;
    }

    public void setC(boolean c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
