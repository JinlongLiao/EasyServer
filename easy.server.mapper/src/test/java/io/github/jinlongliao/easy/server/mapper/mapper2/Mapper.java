package io.github.jinlongliao.easy.server.mapper.mapper2;

import java.util.Date;

public class Mapper {
    private byte b;
    private Byte b1;
    private boolean bool;
    private Boolean bool1;
    private char c;
    private Character c1;
    private short s;
    private Short s1;
    private int i;
    private Integer i1;
    private long l;
    private Long l1;
    private float f;
    private Float f1;
    private double d;
    private Double d1;

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte getB() {
        return b;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public Byte getB1() {
        return b1;
    }

    public void setB1(Byte b1) {
        this.b1 = b1;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public Boolean getBool1() {
        return bool1;
    }

    public void setBool1(Boolean bool1) {
        this.bool1 = bool1;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public Character getC1() {
        return c1;
    }

    public void setC1(Character c1) {
        this.c1 = c1;
    }

    public short getS() {
        return s;
    }

    public void setS(short s) {
        this.s = s;
    }

    public Short getS1() {
        return s1;
    }

    public void setS1(Short s1) {
        this.s1 = s1;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public Integer getI1() {
        return i1;
    }

    public void setI1(Integer i1) {
        this.i1 = i1;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public Long getL1() {
        return l1;
    }

    public void setL1(Long l1) {
        this.l1 = l1;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public Float getF1() {
        return f1;
    }

    public void setF1(Float f1) {
        this.f1 = f1;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public Double getD1() {
        return d1;
    }

    public void setD1(Double d1) {
        this.d1 = d1;
    }

    @Override
    public String toString() {
        return "Mapper{" +
                "b=" + b +
                ", b1=" + b1 +
                ", bool=" + bool +
                ", bool1=" + bool1 +
                ", c=" + c +
                ", c1=" + c1 +
                ", s=" + s +
                ", s1=" + s1 +
                ", i=" + i +
                ", i1=" + i1 +
                ", l=" + l +
                ", l1=" + l1 +
                ", f=" + f +
                ", f1=" + f1 +
                ", d=" + d +
                ", d1=" + d1 +
                ", date=" + date +
                '}';
    }
}
