package io.github.jinlongliao.easy.server.mapper.asm;

public class B extends A {
    private int m;
    private int n;

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return super.toString() + "\nB{" +
                "m=" + m +
                ", n=" + n +
                '}';
    }
}
