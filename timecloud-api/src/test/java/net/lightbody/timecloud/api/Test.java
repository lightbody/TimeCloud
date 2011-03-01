package net.lightbody.timecloud.api;

import net.lightbody.timecloud.api.client.TimeCloudClient;
import net.lightbody.timecloud.api.create.CreateRequest;
import net.lightbody.timecloud.api.sample.SampleRequest;

public class Test {
    public static void main(String[] args) throws TimeCloudException {
        TimeCloudClient client = new TimeCloudClient("prod_queues", "http://localhost:8080");

//        CreateRequest o = new CreateRequest(60);
//        o.addArchive("AVERAGE", 0.5, 1, 90 * 24 * 60);
//        o.addArchive("AVERAGE", 0.5, 60, 365 * 24);
//        o.addArchive("AVERAGE", 0.5, 60 * 24, 5 * 365);
//        o.addArchive("TOTAL", 0.5, 1, 90 * 24 * 60);
//        o.addArchive("TOTAL", 0.5, 60, 365 * 24);
//        o.addArchive("TOTAL", 0.5, 60 * 24, 5 * 365);
//        o.addArchive("MIN", 0.5, 1, 90 * 24 * 60);
//        o.addArchive("MIN", 0.5, 60, 365 * 24);
//        o.addArchive("MIN", 0.5, 60 * 24, 5 * 365);
//        o.addArchive("MAX", 0.5, 1, 90 * 24 * 60);
//        o.addArchive("MAX", 0.5, 60, 365 * 24);
//        o.addArchive("MAX", 0.5, 60 * 24, 5 * 365);
//        o.addDatasource("takes", "GAUGE", 600, Double.NaN, Double.NaN);
//        o.addDatasource("puts", "GAUGE", 600, Double.NaN, Double.NaN);
//        client.create("sf-ff", o);

        SampleRequest req = new SampleRequest();
        req.addValue("foo", 5.6);
        client.sample("sf-ff", req);
    }
}
