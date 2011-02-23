package net.lightbody.timecloud;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import net.lightbody.timecloud.util.NamedImpl;
import org.apache.commons.cli.*;

public class ConfigModule implements Module {
    private String[] args;

    public ConfigModule(String[] args) {
        this.args = args;
    }

    @Override
    public void configure(Binder binder) {
        Options options = new Options();
        Option option = new Option("p", true, "the port to listen on (8080 by default)");
        options.addOption(option);

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar timecloud-server.jar", options);
            binder.addError(e);

            return;
        }

        int port = Integer.parseInt(cmd.getOptionValue('p', "8080"));
        binder.bind(Key.get(Integer.class, new NamedImpl("port"))).toInstance(port);
    }
}
