package io.github.jinlongliao.easy.server.mapper.asm;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation.Mapping2;

public class A {
    // @Ignore2
    private int a;
    private int b;
    private boolean c;
    @Mapping2(converterClass = TestConverter.class, converterMethod = "testString")
    private String d;
    private short e;
    private short f;
    private short i;
    private short j;
    private short k;
    private short l;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
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

    public short getE() {
        return e;
    }

    public void setE(short e) {
        this.e = e;
    }

    public short getF() {
        return f;
    }

    public void setF(short f) {
        this.f = f;
    }

    public short getI() {
        return i;
    }

    public void setI(short i) {
        this.i = i;
    }

    public short getJ() {
        return j;
    }

    public void setJ(short j) {
        this.j = j;
    }

    public short getK() {
        return k;
    }

    public void setK(short k) {
        this.k = k;
    }

    public short getL() {
        return l;
    }

    public void setL(short l) {
        this.l = l;
    }

    @Override
    public String toString() {
        return "A{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d='" + d + '\'' +
                ", e=" + e +
                ", f=" + f +
                ", i=" + i +
                ", j=" + j +
                ", k=" + k +
                ", l=" + l +
                '}';
    }
}
