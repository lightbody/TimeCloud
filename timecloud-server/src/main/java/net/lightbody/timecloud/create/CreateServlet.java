package net.lightbody.timecloud.create;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.lightbody.timecloud.api.Archive;
import net.lightbody.timecloud.api.Datasource;
import net.lightbody.timecloud.api.create.CreateRequest;
import net.lightbody.timecloud.api.create.CreateResponse;
import net.lightbody.timecloud.util.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Singleton
public class CreateServlet extends HttpServlet {
    private ObjectMapper mapper;
    private File dataDir;

    @Inject
    public CreateServlet(ObjectMapper mapper, @Named("data") File dataDir) {
        this.mapper = mapper;
        this.dataDir = dataDir;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateRequest request = mapper.readValue(req.getInputStream(), CreateRequest.class);

        String id = UUID.randomUUID().toString();

        RrdDef def = new RrdDef(new File(dataDir, id + ".rrd").getPath(), Util.normalize(request.getStartTime(), request.getStep()), request.getStep());
        for (Archive archive : request.getArchives()) {
            def.addArchive(ConsolFun.valueOf(archive.getConsoleFun()), archive.getXff(), archive.getSteps(), archive.getRows());
        }

        for (Datasource datasource : request.getDatasources()) {
            def.addDatasource(datasource.getName(), DsType.valueOf(datasource.getType()), datasource.getHeartbeat(), datasource.getMin(), datasource.getMax());
        }

        new RrdDb(def);

        mapper.writeValue(resp.getOutputStream(), new CreateResponse(id));
    }
}
