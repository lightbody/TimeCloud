package net.lightbody.timecloud.api.client;

import net.lightbody.timecloud.api.TimeCloudException;
import net.lightbody.timecloud.api.create.CreateRequest;
import net.lightbody.timecloud.api.sample.SampleRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.map.ObjectMapper;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public class TimeCloudClient {
    private ObjectMapper mapper = new ObjectMapper();
    private HttpClient client;
    private String account;
    private String url;

    public TimeCloudClient(String account, String url) {
        this.account = account;
        this.url = url;
        HttpParams params = new BasicHttpParams();

        // MOB-338: 30 total connections and 6 connections per host matches the behavior in Firefox 3
        ConnManagerParams.setMaxTotalConnections(params, 30);
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(6);
        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", new PlainSocketFactory(), 80));
        try {
            schemeRegistry.register(new Scheme("https", new SSLSocketFactory(SSLContext.getDefault()), 443));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);
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
            InputStream is = response.getEntity().getContent();
            try {
                if (response.getStatusLine().getStatusCode() != 200) {
                    JsonException exception = mapper.readValue(is, JsonException.class);
                    if ("GenericException".equals(exception.getException())) {
                        throw new RuntimeException(exception.getMessage());
                    } else {
                        throw exception.makeTypedException();
                    }
                }
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not issue API call", e);
        }
    }
}
