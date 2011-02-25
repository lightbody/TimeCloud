package net.lightbody.timecloud.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.lightbody.timecloud.api.sample.SampleRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Singleton
public class SampleServlet extends HttpServlet {
    private ObjectMapper mapper;
    private File dataDir;

    @Inject
    public SampleServlet(ObjectMapper mapper, @Named("data") File dataDir) {
        this.mapper = mapper;
        this.dataDir = dataDir;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SampleRequest request = mapper.readValue(req.getInputStream(), SampleRequest.class);

        String id = req.getPathInfo().substring(1);

        File path = new File(dataDir, id + ".rrd");
        RrdDb db = new RrdDb(path.getPath());

        Sample sample = db.createSample(Util.normalize(request.getTime(), db.getRrdDef().getStep()));
        for (Map.Entry<String, Double> entry : request.getValues().entrySet()) {
            sample.setValue(entry.getKey(), entry.getValue());
        }
        sample.update();

        db.close();
    }
}
