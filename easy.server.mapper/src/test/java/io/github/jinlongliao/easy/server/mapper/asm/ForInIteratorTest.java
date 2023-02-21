package io.github.jinlongliao.easy.server.mapper.asm;

public class ForInIteratorTest {
    private void forTest(Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            System.out.println("objects[i] = " + objects[i]);

        }
    }

    private void forIn(Object[] objects) {
        for (Object object : objects) {
            System.out.println("object = " + object);
        }
    }

    private void ifTest(int flag) {
        int result;
        if (flag == 1) {
            result = 2;
        } else if (flag == 2) {
            result = 3;
        } else {
            result = 4;
        }
        System.out.println("result = " + result);
    }

    private void ifTest2(int flag) {
        int result;
        if (flag == 1) {
            result = 2;
        } else if (flag == 2) {
            result = 3;
        } else if (flag == 3) {
            result = 56;
        } else if (flag == 4) {
            result = 13;
        } else {
            result = 4;
        }
        System.out.println("result = " + result);
    }

    private void switchTest(int flag) {
        int result;
        switch (flag) {

            case 0:
                result = 0;
                break;
            case 1:
                result = 1;
                break;

            default:
                result = 4;

        }
        System.out.println("result = " + result);
    }

    private void switchTest2(int flag) {
        int result;
        switch (flag) {

            case 1:
                result = 2;
                break;
            case 2:
                result = 3;
                break;
            case 3:
                result = 5;
                break;
            case 4:
                result = 9;
                break;
            case 11:
                result = 6;
                break;
            case 20:
                result = 11;
                break;
            case 25:
                result = 30;
                break;
            default:
                result = 4;

        }
        System.out.println("result = " + result);
    }

}
