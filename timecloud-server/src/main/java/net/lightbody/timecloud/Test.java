package net.lightbody.timecloud;

import net.lightbody.timecloud.api.create.CreateRequest;
import net.lightbody.timecloud.api.sample.SampleRequest;
import net.lightbody.timecloud.util.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphDef;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class Test {
    public static void main(String[] args) throws IOException {
        ObjectMapper om = new ObjectMapper();
        System.out.println(Util.getTime());

//        URL url = new URL("http://localhost:8080/create");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setDoOutput(true);
//
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
//        om.writeValue(conn.getOutputStream(), o);
//        System.out.println(conn.getResponseCode());
//        System.out.println(IOUtils.readFully(conn.getInputStream()));


//        long time = 1298676470;
//        for (int i = 0; i < 60 * 12; i++) {
//            URL url = new URL("http://localhost:8080/sample/ed58ff0b-ab65-477a-aea5-1ea295cbe152");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//
//            SampleRequest o = new SampleRequest();
//            o.setTime(time);
//
//            o.addValue("puts", 10 + new Random().nextInt(30));
//            o.addValue("takes", 40 + new Random().nextInt(60));
//            om.writeValue(conn.getOutputStream(), o);
//
//            System.out.println(conn.getResponseCode());
//            System.out.println(IOUtils.readFully(conn.getInputStream()));
//
//            time += 60;
//        }
//
//        System.out.println(time);

//        RrdDef rrdDef = new RrdDef("./test.rrd");
//        rrdDef.setStartTime(920804400L);
//        rrdDef.addDatasource("speed", DsType.COUNTER, 600, Double.NaN, Double.NaN);
//        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, 24);
//        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 6, 10);
//        rrdDef.addArchive(ConsolFun.MIN, 0.5, 1, 24);
//        rrdDef.addArchive(ConsolFun.MIN, 0.5, 6, 10);
//        rrdDef.addArchive(ConsolFun.MAX, 0.5, 1, 24);
//        rrdDef.addArchive(ConsolFun.MAX, 0.5, 6, 10);
//        RrdDb rrdDb = new RrdDb(rrdDef);
//        rrdDb.close();
//
//        rrdDb = new RrdDb("./test.rrd");
//        Sample sample = rrdDb.createSample();
//        sample.setAndUpdate("920804700:12345");
//        sample.setAndUpdate("920805000:12357");
//        sample.setAndUpdate("920805300:12363");
//        sample.setAndUpdate("920805600:12363");
//        sample.setAndUpdate("920805900:12363");
//        sample.setAndUpdate("920806200:12373");
//        sample.setAndUpdate("920806500:12383");
//        sample.setAndUpdate("920806800:12393");
//        sample.setAndUpdate("920807100:12399");
//        sample.setAndUpdate("920807400:12405");
//        sample.setAndUpdate("920807700:12411");
//        sample.setAndUpdate("920808000:12415");
//        sample.setAndUpdate("920808300:12420");
//        sample.setAndUpdate("920808600:12422");
//        sample.setAndUpdate("920808900:12423");
//        rrdDb.close();

        RrdGraphDef graphDef = new RrdGraphDef();
        graphDef.setTimeSpan(920804400L, 920808000L);
        graphDef.datasource("myspeed", "./test.rrd", "speed", ConsolFun.AVERAGE);
        graphDef.datasource("minspeed", "./test.rrd", "speed", ConsolFun.MIN);
        graphDef.datasource("maxspeed", "./test.rrd", "speed", ConsolFun.MAX);
        graphDef.line("myspeed", Color.BLACK, null, 2);
        graphDef.line("minspeed", Color.BLUE, null, 2);
        graphDef.line("maxspeed", Color.RED, null, 2);
        graphDef.setFilename("./speed.gif");
        graphDef.setStep(600 * 6);
        RrdGraph graph = new RrdGraph(graphDef);
        BufferedImage bi = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
        graph.render(bi.getGraphics());

    }
}
