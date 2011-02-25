package net.lightbody.timecloud.create;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.lightbody.timecloud.api.create.CreateRequest;
import net.lightbody.timecloud.api.create.CreateResponse;
import net.lightbody.timecloud.util.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CreateServlet extends HttpServlet {
    private ObjectMapper mapper;

    @Inject
    public CreateServlet(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = IOUtils.readFully(req.getInputStream());
        CreateRequest request = mapper.readValue(s, CreateRequest.class);
        CreateResponse response = new CreateResponse(request.getFoo() + "!!!!");
        mapper.writeValue(resp.getWriter(), response);
    }
}
