package net.lightbody.timecloud.api;

public class Archive {
    private String consoleFun;
    private double xff;
    private int steps;
    private int rows;

    public Archive() {
    }

    public Archive(String consoleFun, double xff, int steps, int rows) {
        this.consoleFun = consoleFun;
        this.xff = xff;
        this.steps = steps;
        this.rows = rows;
    }

    public String getConsoleFun() {
        return consoleFun;
    }

    public void setConsoleFun(String consoleFun) {
        this.consoleFun = consoleFun;
    }

    public double getXff() {
        return xff;
    }

    public void setXff(double xff) {
        this.xff = xff;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
