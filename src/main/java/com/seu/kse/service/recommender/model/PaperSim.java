package com.seu.kse.service.recommender.model;



import java.io.Serializable;


/**
 * Created by yaosheng on 2017/5/31.
 */
public class PaperSim implements Comparable<PaperSim>, Serializable {
    public PaperSim(String pid,  double sim){
        this.pid = pid;
        this.sim = sim;
    }
    private String pid;
    private double sim;




    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public double getSim() {
        return sim;
    }

    public void setSim(double sim) {
        this.sim = sim;
    }

    public int compareTo(PaperSim o) {
        return Double.compare(this.sim,o.sim);
    }

}
