package net.lightbody.timecloud;

import org.apache.commons.cli.*;
import org.eclipse.jetty.server.Server;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("p", false, "the port to listen on (8080 by default)");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Command line problem: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar timecloud-server.jar", options);

            return;
        }

        int port = Integer.parseInt(cmd.getOptionValue('p', "8080"));
        System.out.println("Port " + port);

        //Server server = new Server(8080);

    }
}
