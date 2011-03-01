package net.lightbody.timecloud.sample;

import com.google.inject.Singleton;
import net.lightbody.timecloud.BaseServlet;
import net.lightbody.timecloud.api.DatabaseAlreadyExistsException;
import net.lightbody.timecloud.api.DatabaseDoesNotExistException;
import net.lightbody.timecloud.api.sample.SampleRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;

import java.io.File;
import java.util.Map;

@Singleton
public class SampleServlet extends BaseServlet<SampleRequest> {
    public SampleServlet() {
        super(SampleRequest.class);
    }

    @Override
    protected Object doPost(String account, String id, SampleRequest request) throws Exception {
        File parent = new File(dataDir, account + "/" + id);
        File file = new File(parent, "database.rrd");

        if (!file.exists()) {
            throw new DatabaseDoesNotExistException();
        }

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
