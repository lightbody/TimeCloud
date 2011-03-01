package net.lightbody.timecloud.create;

import com.google.inject.Singleton;
import net.lightbody.timecloud.BaseServlet;
import net.lightbody.timecloud.api.Archive;
import net.lightbody.timecloud.api.Datasource;
import net.lightbody.timecloud.api.TimeCloudException;
import net.lightbody.timecloud.api.create.CreateRequest;
import net.lightbody.timecloud.api.DatabaseAlreadyExistsException;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Util;

import java.io.File;
import java.io.IOException;

@Singleton
public class CreateServlet extends BaseServlet<CreateRequest> {
    public CreateServlet() {
        super(CreateRequest.class);
    }

    @Override
    protected Object doPost(String account, String id, CreateRequest request) throws IOException, TimeCloudException {
        File parent = new File(dataDir, account + "/" + id);
        parent.mkdirs();
        File file = new File(parent, "database.rrd");

        if (file.exists()) {
            throw new DatabaseAlreadyExistsException();
        }

        RrdDef def = new RrdDef(file.getPath(), Util.normalize(request.getStartTime(), request.getStep()), request.getStep());
        for (Archive archive : request.getArchives()) {
            def.addArchive(ConsolFun.valueOf(archive.getConsoleFun()), archive.getXff(), archive.getSteps(), archive.getRows());
        }

        for (Datasource datasource : request.getDatasources()) {
            def.addDatasource(datasource.getName(), DsType.valueOf(datasource.getType()), datasource.getHeartbeat(), datasource.getMin(), datasource.getMax());
        }

        new RrdDb(def);

        return null;
    }
}
