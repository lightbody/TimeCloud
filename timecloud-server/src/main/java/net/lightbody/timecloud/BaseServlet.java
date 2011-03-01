package net.lightbody.timecloud;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.lightbody.timecloud.api.client.JsonException;
import net.lightbody.timecloud.api.TimeCloudException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Singleton
public abstract class BaseServlet<T> extends HttpServlet {
    protected ObjectMapper mapper;
    protected File dataDir;
    private Class<T> type;

    protected BaseServlet(Class<T> type) {
        this.type = type;
    }

    @Inject
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Inject
    public void setDataDir(@Named("data") File dataDir) {
        this.dataDir = dataDir;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = null;
        if (req.getPathInfo() != null) {
            id = req.getPathInfo().substring(1);
        }

        String account = req.getHeader("Account");
        T request = mapper.readValue(req.getInputStream(), type);

        try {
            Object response = doPost(account, id, request);
            respond(resp, response);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    protected Object doPost(String account, String id, T request) throws Exception {
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = null;
        if (req.getPathInfo() != null) {
            id = req.getPathInfo().substring(1);
        }

        String account = req.getHeader("Account");

        Object response = doGet(account, id, req);
        try {
            respond(resp, response);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    protected Object doGet(String account, String id, HttpServletRequest req) throws ServletException, IOException {
        return null;
    }

    private void handleError(HttpServletResponse resp, Exception e) throws IOException {
        if (e instanceof TimeCloudException) {
            resp.setStatus(((TimeCloudException) e).getStatusCode());
        } else {
            resp.setStatus(500);
        }

        try {
            respond(resp, new JsonException(e));
        } catch (Exception e1) {
            e.printStackTrace(resp.getWriter());
        }
    }

    private void respond(HttpServletResponse resp, Object response) throws Exception {
        if (response != null) {
            mapper.writeValue(resp.getOutputStream(), response);
        }
    }
}
