package net.lightbody.timecloud;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import net.lightbody.timecloud.create.CreateServlet;
import net.lightbody.timecloud.fetch.FetchServlet;
import net.lightbody.timecloud.graph.GraphServlet;
import net.lightbody.timecloud.sample.SampleServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletContextEvent;

public class Main {
    public static void main(String[] args) throws Exception {
        final Injector injector = Guice.createInjector(new ConfigModule(args), new JettyModule(), new ServletModule() {
            @Override
            protected void configureServlets() {
                serve("/create/*").with(CreateServlet.class);
                serve("/sample/*").with(SampleServlet.class);
                serve("/graph/*").with(GraphServlet.class);
                serve("/fetch/*").with(FetchServlet.class);
            }
        });

        Server server = injector.getInstance(Server.class);
        GuiceServletContextListener gscl = new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return injector;
            }
        };
        server.start();

        ServletContextHandler context = (ServletContextHandler) server.getHandler();
        gscl.contextInitialized(new ServletContextEvent(context.getServletContext()));

        server.join();
    }
}
