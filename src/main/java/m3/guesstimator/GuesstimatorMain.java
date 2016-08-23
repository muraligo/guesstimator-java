package m3.guesstimator;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;

import m3.guesstimator.internal.GuesstimatorApplication;

public class GuesstimatorMain {

	public static void main(String[] args) {
        // Create a new Restlet component, add a HTTP server connector to it
        Component comp = new Component();
        comp.getServers().add(Protocol.HTTP, 8182);

        // Attach the paths and resources to the local host
        Application app = new GuesstimatorApplication();
        comp.getDefaultHost().attach(app);

        // Start the component. NOTE: HTTP server connector is also automatically started.
        try {
            comp.start();
        } catch (Exception ex) {
        	// TODO: handle exception
        }
	}

}
