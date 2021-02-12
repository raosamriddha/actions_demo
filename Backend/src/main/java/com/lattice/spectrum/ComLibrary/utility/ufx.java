/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.utility;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Anuj Pathak
 */
public class ufx {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(ArrayList<Byte> fifo, int start, int end) {
        char[] hexChars = new char[(end - start) * 2];
        for (int j = 0; j < (end - start); j++) {
            int v = fifo.get(start + j) & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * @param out
     * @param offset
     * @param data
     * @param len
     * @param LSB_FIRST
     */
    public static void saveIntToBytes(byte[] out, int offset, int data, int len, boolean LSB_FIRST) {
        if (out.length > offset + len) {
            if (LSB_FIRST) {
                for (int i = 0; i < len; i++) {
                    out[offset + i] = (byte) (data >> (i * 8));
                }
            } else {
                for (int i = 0; i < len; i++) {
                    out[offset + len - 1 - i] = (byte) (data >> (i * 8));
                }
            }
        }
    }

    /**
     * bytes to integer conversion
     *
     * @param fifo
     * @param offset
     * @param len
     * @param LSB_FIRST
     * @param signed
     * @return
     */
    public static int BytesToInt(ArrayList<Byte> fifo, int offset, int len, boolean LSB_FIRST, boolean signed) {
        int val = 0;
        if (fifo.size() > offset + len) {
            if (LSB_FIRST) {
                len--;
                val = signed ? fifo.get(offset + len) : fifo.get(offset + len) & 0xFF;
                while (len > 0) {
                    len--;
                    val = val << 8;
                    val |= (fifo.get(offset + len) & 0xFF);
                }
            } else {
                int i = 0;
                val = signed ? fifo.get(offset) : fifo.get(offset) & 0xFF;
                while (i < len) {
                    i++;
                    val = val << 8;
                    val |= (fifo.get(offset + i) & 0xFF);
                }
            }
        }
        return val;
    }

    /**
     * bytes to Long conversion
     *
     * @param fifo
     * @param offset
     * @param len
     * @param LSB_FIRST
     * @param signed
     * @return
     */
    public static long BytesToLong(ArrayList<Byte> fifo, int offset, int len, boolean LSB_FIRST, boolean signed) {
        long val = 0;
        if (fifo.size() > offset + len) {
            if (LSB_FIRST) {
                len--;
                val = signed ? fifo.get(offset + len) : fifo.get(offset + len) & 0xFF;
                while (len > 0) {
                    len--;
                    val = val << 8;
                    val |= (fifo.get(offset + len) & 0xFF);
                }
            } else {
                int i = 0;
                val = signed ? fifo.get(offset) : fifo.get(offset) & 0xFF;
                while (i < len) {
                    i++;
                    val = val << 8;
                    val |= (fifo.get(offset + i) & 0xFF);
                }
            }
        }
        return val;
    }
    /**
     * unit conversion from milliBar to PSI
     *
     * @param mBar
     * @return PSI
     */
    public static double mBarToPsi(double mBar) {
        return mBar / 68.9475729318;
    }

    /**
     * unit conversion from PSI to milliBar
     *
     * @param Psi
     * @return mBar
     */
    public static double psiToMBar(double Psi) {
        return Psi * 68.9475729318;
    }

    /**
     * @param val
     * @param min
     * @param max
     * @return
     */
    public static double boundInRange(double val, double min, double max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }


    /**
     * @return time in decimal format
     */
    public static double time() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }
}
