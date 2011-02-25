package net.lightbody.timecloud.api.create;

import net.lightbody.timecloud.api.Archive;
import net.lightbody.timecloud.api.Datasource;

import java.util.ArrayList;
import java.util.List;

public class CreateRequest {
    private long startTime;
    private long step;
    private List<Archive> archives = new ArrayList<Archive>();
    private List<Datasource> datasources = new ArrayList<Datasource>();

    public CreateRequest() {
    }

    public CreateRequest(long step) {
        this.startTime = System.currentTimeMillis();
        this.step = step;
    }

    public void addArchive(String consoleFun, double xff, int steps, int rows) {
        archives.add(new Archive(consoleFun, xff, steps, rows));
    }

    public void addDatasource(String name, String type, long heartbeat, double min, double max) {
        datasources.add(new Datasource(name, type, heartbeat, min, max));
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public List<Archive> getArchives() {
        return archives;
    }

    public void setArchives(List<Archive> archives) {
        this.archives = archives;
    }

    public List<Datasource> getDatasources() {
        return datasources;
    }

    public void setDatasources(List<Datasource> datasources) {
        this.datasources = datasources;
    }
}
