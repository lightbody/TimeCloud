package net.lightbody.timecloud.graph;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Util;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphDef;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Singleton
public class GraphServlet extends HttpServlet {
    private File dataDir;

    @Inject
    public GraphServlet(@Named("data") File dataDir) {
        this.dataDir = dataDir;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getPathInfo().substring(1);
        String account = req.getParameter("account");

        File parent = new File(dataDir, account + "/" + id);
        File file = new File(parent, "database.rrd");

        RrdDb db = new RrdDb(file.getPath());
        long step = db.getRrdDef().getStep();

        // default to showing data up until right now
        long end = Util.normalize(Util.getTime(), step);
        if (req.getParameter("end") != null) {
            end = parseTime(step, req.getParameter("end"));
        }

        // default to show the last day of time
        long start = end - TimeUnit.SECONDS.convert(1, TimeUnit.DAYS);
        if (req.getParameter("start") != null) {
            start = parseTime(step, req.getParameter("start"));
        }

        // if a step is provided, make sure to set the start/end times to be a multiple of it
        if (req.getParameter("step") != null) {
            long resolution = Long.parseLong(req.getParameter("step"));
            start = ((int) start / resolution) * resolution;
            end = ((int) end / resolution) * resolution;
        }

        RrdGraphDef graphDef = new RrdGraphDef();
        graphDef.setTimeSpan(start, end);

        // get the datasources
        int i = 1;
        while (true) {
            String name = req.getParameter("ds" + i);
            if (name == null) {
                break;
            }

            String dsName = req.getParameter("dsn" + i);
            ConsolFun fun = ConsolFun.valueOf(req.getParameter("dscf" + i));
            graphDef.datasource(name, file.getPath(), dsName, fun);

            i++;
        }

        // handle any lines
        i = 1;
        while (true) {
            String line = req.getParameter("line" + i);
            if (line == null) {
                break;
            }

            Color color = Color.decode("0x" + req.getParameter("linec" + i));
            String legend = req.getParameter("linel" + i);
            int thinkness = 2;
            try {
                thinkness = Integer.parseInt(req.getParameter("linet" + i));
            } catch (NumberFormatException e) {
                // ignore
            }

            graphDef.line(line, color, legend, thinkness);

            i++;
        }

        if (req.getParameter("step") != null) {
            graphDef.setStep(Long.parseLong(req.getParameter("step")));
        }

        if (req.getParameter("width") != null) {
            graphDef.setWidth(Integer.parseInt(req.getParameter("width")));
        }
        if (req.getParameter("height") != null) {
            graphDef.setHeight(Integer.parseInt(req.getParameter("height")));
        }

        byte[] bytes = new RrdGraph(graphDef).getRrdGraphInfo().getBytes();

        resp.setContentType("image/gif");
        resp.getOutputStream().write(bytes);
    }

    private long parseTime(long step, String value) {
        try {
            return Util.normalize(Long.parseLong(value), step);
        } catch (NumberFormatException e) {
            // not a number? OK let's try +/-###[D|H|M]
            long now = Util.getTime();
            boolean plus = value.charAt(0) == '+';
            int num = Integer.parseInt(value.substring(1, value.length() - 1));
            TimeUnit timeUnit = TimeUnit.MINUTES;

            switch (value.charAt(value.length() - 1)) {
                case 'd':
                case 'D':
                    timeUnit = TimeUnit.DAYS;
                    break;
                case 'h':
                case 'H':
                    timeUnit = TimeUnit.HOURS;
                    break;
                case 'm':
                case 'M':
                default:
                    timeUnit = TimeUnit.MINUTES;
                    break;
            }

            if (plus) {
                return now + TimeUnit.SECONDS.convert(num, timeUnit);
            } else {
                return now - TimeUnit.SECONDS.convert(num, timeUnit);
            }
        }
    }
}
