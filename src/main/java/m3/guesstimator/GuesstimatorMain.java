package m3.guesstimator;

import static spark.Spark.port;
import static spark.Spark.threadPool;

import java.io.IOException;

import m3.guesstimator.internal.GuesstimatorApplication;
import m3.guesstimator.internal.GuesstimatorContext;
import m3.guesstimator.service.ApplicationContext;

public class GuesstimatorMain {

	public static void main(String[] args) throws IOException {
		ApplicationContext config = null;
        if (args.length > 0){
            String home = args[0].endsWith("/") ? args[0] : args[0] + "/";
            System.out.println("app home set: "+ home);
            config = GuesstimatorContext.loadConfig(home+"config.yml", home);

            // Port and threadpool for HTTP server
//          comp.getServers().add(Protocol.HTTP, 8182);
        } else {
            config = GuesstimatorContext.defaultContext();
        }
        port(Integer.parseInt(config.getWeb().get("port")));
        threadPool(Integer.parseInt(config.getWeb().get("threads.max")),
                Integer.parseInt(config.getWeb().get("threads.min")),
                Integer.parseInt(config.getWeb().get("threads.timeout")));

        // Attach the paths and resources to the local host
        GuesstimatorApplication app = new GuesstimatorApplication(config);
	}

}
