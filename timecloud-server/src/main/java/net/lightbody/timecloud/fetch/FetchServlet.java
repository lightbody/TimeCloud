package net.lightbody.timecloud.fetch;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.lightbody.timecloud.api.fetch.FetchResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Singleton
public class FetchServlet extends HttpServlet {
    private ObjectMapper mapper;
    private File dataDir;

    @Inject
    public FetchServlet(ObjectMapper mapper, @Named("data") File dataDir) {
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


        FetchRequest fr;
        if (req.getParameter("resolution") != null) {
            long resolution = Long.parseLong(req.getParameter("resolution"));
            start = ((int) start / resolution) * resolution;
            end = ((int) end / resolution) * resolution;
            fr = db.createFetchRequest(ConsolFun.valueOf(req.getParameter("consoleFun")), start, end, resolution);
        } else {
            fr = db.createFetchRequest(ConsolFun.valueOf(req.getParameter("consoleFun")), start, end);
        }

        FetchData data = fr.fetchData();

        System.out.println(data.dump());

        String[] dsNames = data.getDsNames();
        long[] timestamps = data.getTimestamps();
        double[][] values = data.getValues();

        FetchResponse response = new FetchResponse(dsNames, timestamps, values);
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
