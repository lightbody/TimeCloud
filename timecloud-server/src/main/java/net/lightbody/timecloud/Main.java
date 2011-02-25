package net.lightbody.timecloud;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import net.lightbody.timecloud.create.CreateServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletContextEvent;

public class Main {
    public static void main(String[] args) throws Exception {
        final Injector injector = Guice.createInjector(new ConfigModule(args), new JettyModule(), new ServletModule() {
            @Override
            protected void configureServlets() {
                serve("/create").with(CreateServlet.class);
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
