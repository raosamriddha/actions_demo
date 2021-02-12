/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 8/7/19 11:03 AM
 * Modified : 8/7/19 11:03 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.utility;

import java.util.ArrayList;

/**
 * @deprecated
 */
public class SampleCorrelator {

    private final int size;
    private final ArrayList<Double> x;
    private final ArrayList<Double> y;

    //    private boolean valid;
    private double Mx, My, mx, my;
    private int tMx, tMy, tmx, tmy;

    public int getCorrDelay() {
        return corrDelay;
    }

    private double correlation = Double.MAX_VALUE;
    private int corrDelay;
    private double m;
    private double xa;
    private double yc;

    public SampleCorrelator(int size) {
        this.size = size;
        x = new ArrayList<>();
        y = new ArrayList<>();
    }

    public void addSamples(double dx, double dy) {
        x.add(dx);
        if (x.size() > size) x.remove(0);
        y.add(dy);
        if (y.size() > size) y.remove(0);
//
        if (x.size() >= 4) {
            correlation = Double.MAX_VALUE;
            findCorrelation();
        }
    }

    public void dump(int length) {
        for (int i = 0; i < length && i < x.size(); i++) {
            x.remove(0);
            y.remove(0);
        }
    }

    private boolean calMinMax() {
        double pSlopeX = 0, pSlopeY = 0;
        double tSlopeX, tSlopeY;
        boolean fMx = true, fmx = true;
        boolean fMy = true, fmy = true;
        for (int i = 1; i < x.size() && (fmx || fMx); i++) {
            tSlopeX = x.get(i) - x.get(i - 1);
            if (tSlopeX == 0) {
                continue;
            }
//
            if (pSlopeX == 0) {
                pSlopeX = tSlopeX;
                continue;
            }
            if (pSlopeX > 0) {
                if (tSlopeX < 0) {
//                    maxima found
                    Mx = x.get(i - 1);
                    tMx = i - 1;
                    fMx = false;
                }
            } else {
                if (tSlopeX > 0) {
//                    maxima found
                    mx = x.get(i - 1);
                    tmx = i - 1;
                    fmx = false;
                }
            }
            pSlopeX = tSlopeX;
        }
//
        for (int i = 1; i < y.size() && (fmy || fMy); i++) {
            tSlopeY = y.get(i) - y.get(i - 1);
            if (tSlopeY == 0) {
                continue;
            }
//
            if (pSlopeY == 0) {
                pSlopeY = tSlopeY;
                continue;
            }
            if (pSlopeY > 0) {
                if (tSlopeY < 0) {
//                    mayima found
                    My = y.get(i - 1);
                    tMy = i - 1;
                    fMy = false;
                }
            } else {
                if (tSlopeY > 0) {
//                    mayima found
                    my = y.get(i - 1);
                    tmy = i - 1;
                    fmy = false;
                }
            }
            pSlopeY = tSlopeY;
        }
        return !(fMx || fmx || fMy || fmy);
    }

    public double getCorrelation() {
        return correlation;
    }

    private void findCorrelation() {
//        check for absolute mayima and minima on both list
        if (calMinMax()) {
//            sLog.d(this, "Maxima Minima Found");
//            calculate slope
            corrDelay = (Math.abs(tmx - tmy) + Math.abs(tMx - tMy)) / 2;
            m = (My - my) / (Mx - mx);
            xa = mx;
            yc = my;
            //      validate
            if (corrDelay >= 5 && corrDelay <= 15) {
                correlation = 0;
                for (int i = 0; i < x.size(); i++) {
                    correlation += Math.abs(y.get(i) - getY(x.get(i)));
                }
                correlation /= x.size();
            }
        }
    }

    private boolean isAtEnd(ArrayList<Double> l, double v) {
        return l.get(0).equals(v) || l.get(l.size() - 1).equals(v);
    }

    public double getY(double x) {
//        y = m(x-xa) + yc
        return m * (x - xa) + yc;
    }

    public double getX(double y) {
//        y = m(x-xa) + yc
//        x = ((y-yc)/m) + xa
        return ((y - yc) / m) + xa;
    }

}
