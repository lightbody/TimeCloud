package net.lightbody.timecloud.api;

public class Datasource {
    private String name;
    private String type;
    private long heartbeat;
    private double min;
    private double max;

    public Datasource() {
    }

    public Datasource(String name, String type, long heartbeat, double min, double max) {
        this.name = name;
        this.type = type;
        this.heartbeat = heartbeat;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(long heartbeat) {
        this.heartbeat = heartbeat;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
