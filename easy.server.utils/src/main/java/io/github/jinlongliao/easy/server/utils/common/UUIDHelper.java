package io.github.jinlongliao.easy.server.utils.common;


import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;


public class UUIDHelper {
    private static class Holder {
        static final SecureRandom numberGenerator = new SecureRandom();
        static final AtomicInteger NEXT_COUNTER = new AtomicInteger(new SecureRandom().nextInt());

        static final int RANDOM_VALUE1 = numberGenerator.nextInt(0x01000000);
        static final short RANDOM_VALUE2 = (short) numberGenerator.nextInt(0x00008000);

        static byte int3(final int x) {
            return (byte) (x >> 24);
        }

        static byte int2(final int x) {
            return (byte) (x >> 16);
        }

        static byte int1(final int x) {
            return (byte) (x >> 8);
        }

        static byte int0(final int x) {
            return (byte) (x);
        }

        static byte short1() {
            return (byte) (Holder.RANDOM_VALUE2 >> 8);
        }

        static byte short0() {
            return (byte) (Holder.RANDOM_VALUE2);
        }
    }

    public static String random(String prefix) {
        SecureRandom ng = UUIDHelper.Holder.numberGenerator;
        byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f;  /* clear version        */
        randomBytes[6] |= 0x40;  /* set to version 4     */
        randomBytes[8] &= 0x3f;  /* clear variant        */
        randomBytes[8] |= 0x80;  /* set to IETF variant  */
        long mostSigBits = 0;
        long leastSigBits = 0;
        for (int i = 0; i < 8; i++) {
            mostSigBits = (mostSigBits << 8) | (randomBytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            leastSigBits = (leastSigBits << 8) | (randomBytes[i] & 0xff);
        }
        return prefix + (digits(mostSigBits >> 32, 8) +
                digits(mostSigBits >> 16, 4) +
                digits(mostSigBits, 4) +
                digits(leastSigBits >> 48, 4) +
                digits(leastSigBits, 12));
    }

    public static String random() {
        return random("");
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }


    public static String mongoId() {
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        byte[] idByte = new byte[12];
        idByte[0] = (UUIDHelper.Holder.int3(timestamp));
        idByte[1] = (UUIDHelper.Holder.int2(timestamp));
        idByte[2] = UUIDHelper.Holder.int1(timestamp);
        idByte[3] = (UUIDHelper.Holder.int0(timestamp));
        idByte[4] = (UUIDHelper.Holder.int2(UUIDHelper.Holder.RANDOM_VALUE1));
        idByte[5] = (UUIDHelper.Holder.int1(UUIDHelper.Holder.RANDOM_VALUE1));
        idByte[6] = (UUIDHelper.Holder.int0(UUIDHelper.Holder.RANDOM_VALUE1));
        idByte[7] = (UUIDHelper.Holder.short1());
        idByte[8] = (UUIDHelper.Holder.short0());
        int counter = (UUIDHelper.Holder.NEXT_COUNTER.getAndIncrement() & 0x00ffffff);
        idByte[9] = UUIDHelper.Holder.int2(counter);
        idByte[10] = (UUIDHelper.Holder.int1(counter));
        idByte[11] = (UUIDHelper.Holder.int0(counter));
        return HexUtil.byte2Hex(idByte);
    }

    public static String randomId(int byteSize) {
        byte[] bytes = new byte[byteSize];
        Holder.numberGenerator.nextBytes(bytes);
        return HexUtil.byte2Hex(bytes);
    }

}
