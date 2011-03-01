package net.lightbody.timecloud.api.client;

import net.lightbody.timecloud.api.TimeCloudException;
import net.lightbody.timecloud.api.create.CreateRequest;
import net.lightbody.timecloud.api.sample.SampleRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TimeCloudClient {
    private ObjectMapper mapper = new ObjectMapper();
    private HttpClient client;
    private String account;
    private String url;

    public TimeCloudClient(String account, String url) {
        this.account = account;
        this.url = url;
        client = new DefaultHttpClient();
    }

    public void create(String name, CreateRequest request) throws TimeCloudException {
        apiCall("/create/" + name, request);
    }

    public void sample(String name, SampleRequest request) throws TimeCloudException {
        apiCall("/sample/" + name, request);
    }

    private void apiCall(String path, Object request) throws TimeCloudException {
        HttpPost post = new HttpPost(url + path);
        try {
            post.addHeader("Account", account);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            mapper.writeValue(out, request);
            post.setEntity(new ByteArrayEntity(out.toByteArray()));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != 200) {
                JsonException exception = mapper.readValue(response.getEntity().getContent(), JsonException.class);
                if ("GenericException".equals(exception.getException())) {
                    throw new RuntimeException(exception.getMessage());
                } else {
                    throw exception.makeTypedException();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not issue API call", e);
        }
    }
}
