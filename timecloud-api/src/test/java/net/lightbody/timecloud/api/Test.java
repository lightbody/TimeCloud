package net.lightbody.timecloud.api;

import net.lightbody.timecloud.api.client.TimeCloudClient;
import net.lightbody.timecloud.api.create.CreateRequest;
import net.lightbody.timecloud.api.sample.SampleRequest;

import java.io.*;

public class Test {
    public static void main(String[] args) throws TimeCloudException, IOException {
        String queue = "sf-ff";

        TimeCloudClient client = new TimeCloudClient("prod_queues", "http://localhost:8080");

        CreateRequest o = new CreateRequest(60);
        o.setStartTime(1298835740);
        archive(o, "AVERAGE");
        archive(o, "MIN");
        archive(o, "MAX");
        archive(o, "TOTAL");
        o.addDatasource("takes", "GAUGE", 60, Double.NaN, Double.NaN);
        o.addDatasource("puts", "GAUGE", 60, Double.NaN, Double.NaN);
        client.create(queue, o);

        File file = new File("/Users/plightbo/Desktop/logs 2/sf-ff.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int count = 0;
        long last = 0;
        long v;
        while ((line = br.readLine()) != null) {
            v = normalize((Long.parseLong(line) + 500L) / 1000L, 60);

            if (v == last || last == 0) {
                last = v;
                count++;
            } else if (count > 0) {
                SampleRequest req = new SampleRequest();
                req.setTime(v);
                req.addValue("puts", count);
                client.sample(queue, req);

                System.out.println(v + " -> " + count);

                last = v;
                count = 1;
            }
        }

    }

    public static long normalize(long timestamp, long step) {
        return timestamp - timestamp % step;
    }

    private static void archive(CreateRequest o, String average) {
        o.addArchive(average, 0.5, 1      , 60 * 24 * 365);
        o.addArchive(average, 0.5, 15     , 4  * 24 * 365);
        o.addArchive(average, 0.5, 60     ,      24 * 365);
        o.addArchive(average, 0.5, 60 * 24,       5 * 365);
    }
}
