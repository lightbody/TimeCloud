package net.lightbody.timecloud.api.fetch;

public class FetchResponse {
    private String[] dsNames;
    private long[] timestamps;
    double[][] values;

    public FetchResponse() {
    }

    public FetchResponse(String[] dsNames, long[] timestamps, double[][] values) {
        this.dsNames = dsNames;
        this.timestamps = timestamps;
        this.values = values;
    }

    public String[] getDsNames() {
        return dsNames;
    }

    public void setDsNames(String[] dsNames) {
        this.dsNames = dsNames;
    }

    public long[] getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long[] timestamps) {
        this.timestamps = timestamps;
    }

    public double[][] getValues() {
        return values;
    }

    public void setValues(double[][] values) {
        this.values = values;
    }
}
