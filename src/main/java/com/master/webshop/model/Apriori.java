package com.master.webshop.model;

public class Apriori {

    private double support;

    private double confidence;

    public double lift;

    public Apriori(double support, double confidence, double lift) {
        this.support = support;
        this.confidence = confidence;
        this.lift = lift;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getLift() {
        return lift;
    }

    public void setLift(double lift) {
        this.lift = lift;
    }

    public Apriori mergeApriori(Apriori apriori){
        this.support += apriori.support;
        this.confidence += apriori.confidence;
        this.lift += apriori.lift;

        return null;
    }
}
