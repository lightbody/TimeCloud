package net.lightbody.timecloud.graph;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.codehaus.jackson.map.ObjectMapper;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Util;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphDef;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Singleton
public class GraphServlet extends HttpServlet {
    private ObjectMapper mapper;
    private File dataDir;

    @Inject
    public GraphServlet(ObjectMapper mapper, @Named("data") File dataDir) {
        this.mapper = mapper;
        this.dataDir = dataDir;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getPathInfo().substring(1);
        String path = new File(dataDir, id + ".rrd").getPath();

        RrdDb db = new RrdDb(path);
        long step = db.getRrdDef().getStep();
        long start = Util.normalize(Long.parseLong(req.getParameter("start")), step);
        long end = Util.normalize(Long.parseLong(req.getParameter("end")), step);

//        long e = ((int) end / (60 * 60)) * 60 * 60;
//        long s = e - 60 * 60 * 2;
//        FetchRequest fr = db.createFetchRequest(ConsolFun.TOTAL, s, e, 60 * 60);
//        FetchData data = fr.fetchData();
//        System.out.println(data.dump());

        RrdGraphDef graphDef = new RrdGraphDef();
        graphDef.setTimeSpan(start, end);
        graphDef.datasource("a-puts", path, "puts", ConsolFun.AVERAGE);
        graphDef.datasource("t-puts", path, "puts", ConsolFun.TOTAL);
        graphDef.datasource("n-puts", path, "puts", ConsolFun.MIN);
        graphDef.datasource("n-takes", path, "takes", ConsolFun.MIN);
        graphDef.datasource("x-puts", path, "puts", ConsolFun.MAX);
        graphDef.line("a-puts", new Color(0xFF, 0, 0), null, 2);
        graphDef.line("t-puts", new Color(0x55, 0, 0), null, 2);
        graphDef.line("n-puts", new Color(0, 0x55, 0), null, 2);
        graphDef.line("n-takes", new Color(0, 0, 0x55), null, 2);
        graphDef.line("x-puts", new Color(0, 0xFF, 0), null, 2);

        if (req.getParameter("step") != null) {
            graphDef.setStep(Long.parseLong(req.getParameter("step")));
        }

        byte[] bytes = new RrdGraph(graphDef).getRrdGraphInfo().getBytes();

        resp.setContentType("image/gif");
        resp.getOutputStream().write(bytes);
    }
}
