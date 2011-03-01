package net.lightbody.timecloud.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.lightbody.timecloud.BaseServlet;
import net.lightbody.timecloud.api.create.CreateRequest;
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
public class SampleServlet extends BaseServlet<SampleRequest> {
    public SampleServlet() {
        super(SampleRequest.class);
    }

    @Override
    protected Object doPost(String account, String id, SampleRequest request) throws Exception {
        File parent = new File(dataDir, account + "/" + id);
        parent.mkdirs();
        File file = new File(parent, "database.rrd");

        RrdDb db = new RrdDb(file.getPath());

        Sample sample = db.createSample(Util.normalize(request.getTime(), db.getRrdDef().getStep()));
        for (Map.Entry<String, Double> entry : request.getValues().entrySet()) {
            sample.setValue(entry.getKey(), entry.getValue());
        }
        sample.update();

        db.close();

        return null;
    }
}
