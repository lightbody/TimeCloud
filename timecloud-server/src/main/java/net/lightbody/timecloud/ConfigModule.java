package net.lightbody.timecloud;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import net.lightbody.timecloud.util.NamedImpl;
import org.apache.commons.cli.*;

import java.io.File;

public class ConfigModule implements Module {
    private String[] args;

    public ConfigModule(String[] args) {
        this.args = args;
    }

    @Override
    public void configure(Binder binder) {
        Options options = new Options();

        Option port = new Option("p", true, "the port to listen on (8080 by default)");
        options.addOption(port);

        Option dir = new Option("d", true, "the directory to store files in ('data' in the working directory by default)");
        options.addOption(dir);

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

        int portValue = Integer.parseInt(cmd.getOptionValue('p', "8080"));
        binder.bind(Key.get(Integer.class, new NamedImpl("port"))).toInstance(portValue);

        File dataValue = new File(cmd.getOptionValue('d', "data"));
        if ((!dataValue.exists() && !dataValue.mkdirs())) {
            binder.addError("Data directory " + dataValue + " could not be created, failing startup");
            return;
        }
        if (dataValue.exists() && !dataValue.isDirectory()) {
            binder.addError("Data directory " + dataValue + " is not a directory, failing startup");
            return;
        }
        binder.bind(Key.get(File.class, new NamedImpl("data"))).toInstance(dataValue);
    }
}
