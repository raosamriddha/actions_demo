package org.lattice.spectrum_backend_final.beans;

public class ReferenceCalculator {

    private double feedWtAfterC1;
    private double feedWtAfterC2;
    private double feedWtAfterD1;
    private double feedWtAfterD2;
    private double feedWtAfterCFC;
    private double permeateWtAfterC1;
    private double permeateWtAfterC2;
    private double permeateWtAfterD1;
    private double permeateWtAfterD2;
    private double permeateWtAfterCFC;
    private double feedBuffer1;
    private double feedBuffer2;

    public ReferenceCalculator() {
    }

    public ReferenceCalculator(double feedWtAfterC1, double mFeedAfterC2, double mFeedAfterD1, double mFeedAfterD2, double mFeedAfterCFC, double mPermeateAfterC1, double mPermeateAfterC2, double mPermeateAfterD1, double mPermeateAfterD2, double mPermeateAfterCFC, double feedBuffer1, double feedBuffer2) {
        this.feedWtAfterC1 = feedWtAfterC1;
        this.feedWtAfterC2 = mFeedAfterC2;
        this.feedWtAfterD1 = mFeedAfterD1;
        this.feedWtAfterD2 = mFeedAfterD2;
        this.feedWtAfterCFC = mFeedAfterCFC;
        this.permeateWtAfterC1 = mPermeateAfterC1;
        this.permeateWtAfterC2 = mPermeateAfterC2;
        this.permeateWtAfterD1 = mPermeateAfterD1;
        this.permeateWtAfterD2 = mPermeateAfterD2;
        this.permeateWtAfterCFC = mPermeateAfterCFC;
        this.feedBuffer1 = feedBuffer1;
        this.feedBuffer2 = feedBuffer2;
    }

    public double getFeedWtAfterC1() {
        return feedWtAfterC1;
    }

    public void setFeedWtAfterC1(double feedWtAfterC1) {
        this.feedWtAfterC1 = feedWtAfterC1;
    }

    public double getFeedWtAfterC2() {
        return feedWtAfterC2;
    }

    public void setFeedWtAfterC2(double feedWtAfterC2) {
        this.feedWtAfterC2 = feedWtAfterC2;
    }

    public double getFeedWtAfterD1() {
        return feedWtAfterD1;
    }

    public void setFeedWtAfterD1(double feedWtAfterD1) {
        this.feedWtAfterD1 = feedWtAfterD1;
    }

    public double getFeedWtAfterD2() {
        return feedWtAfterD2;
    }

    public void setFeedWtAfterD2(double feedWtAfterD2) {
        this.feedWtAfterD2 = feedWtAfterD2;
    }

    public double getFeedWtAfterCFC() {
        return feedWtAfterCFC;
    }

    public void setFeedWtAfterCFC(double feedWtAfterCFC) {
        this.feedWtAfterCFC = feedWtAfterCFC;
    }

    public double getPermeateWtAfterC1() {
        return permeateWtAfterC1;
    }

    public void setPermeateWtAfterC1(double permeateWtAfterC1) {
        this.permeateWtAfterC1 = permeateWtAfterC1;
    }

    public double getPermeateWtAfterC2() {
        return permeateWtAfterC2;
    }

    public void setPermeateWtAfterC2(double permeateWtAfterC2) {
        this.permeateWtAfterC2 = permeateWtAfterC2;
    }

    public double getPermeateWtAfterD1() {
        return permeateWtAfterD1;
    }

    public void setPermeateWtAfterD1(double permeateWtAfterD1) {
        this.permeateWtAfterD1 = permeateWtAfterD1;
    }

    public double getPermeateWtAfterD2() {
        return permeateWtAfterD2;
    }

    public void setPermeateWtAfterD2(double permeateWtAfterD2) {
        this.permeateWtAfterD2 = permeateWtAfterD2;
    }

    public double getPermeateWtAfterCFC() {
        return permeateWtAfterCFC;
    }

    public void setPermeateWtAfterCFC(double permeateWtAfterCFC) {
        this.permeateWtAfterCFC = permeateWtAfterCFC;
    }

    public double getFeedBuffer1() {
        return feedBuffer1;
    }

    public void setFeedBuffer1(double feedBuffer1) {
        this.feedBuffer1 = feedBuffer1;
    }

    public double getFeedBuffer2() {
        return feedBuffer2;
    }

    public void setFeedBuffer2(double feedBuffer2) {
        this.feedBuffer2 = feedBuffer2;
    }

    @Override
    public String toString() {
        return "ReferenceCalculator{" +
                "mFeedAfterC1=" + feedWtAfterC1 +
                ", mFeedAfterC2=" + feedWtAfterC2 +
                ", mFeedAfterD1=" + feedWtAfterD1 +
                ", mFeedAfterD2=" + feedWtAfterD2 +
                ", mFeedAfterCFC=" + feedWtAfterCFC +
                ", mPermeateAfterC1=" + permeateWtAfterC1 +
                ", mPermeateAfterC2=" + permeateWtAfterC2 +
                ", mPermeateAfterD1=" + permeateWtAfterD1 +
                ", mPermeateAfterD2=" + permeateWtAfterD2 +
                ", mPermeateAfterCFC=" + permeateWtAfterCFC +
                ", feedBuffer1=" + feedBuffer1 +
                ", feedBuffer2=" + feedBuffer2 +
                '}';
    }
}
