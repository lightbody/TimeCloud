package net.lightbody.timecloud.api.sample;

import java.util.HashMap;
import java.util.Map;

public class SampleRequest {
    private long time;
    private Map<String, Double> values = new HashMap<String, Double>();

    public SampleRequest() {
        this.time = (System.currentTimeMillis() + 500L) / 1000L;
    }

    public void addValue(String name, double value) {
        values.put(name, value);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map<String, Double> getValues() {
        return values;
    }

    public void setValues(Map<String, Double> values) {
        this.values = values;
    }
}
