package m3.guesstimator.internal;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class GuesstimatorApplication extends Application {
	GuesstimatorContext ctx;
	GuesstimatorInitializer initializer;

	public GuesstimatorApplication() {
		super();
		ctx = new GuesstimatorContext();
		ctx.initialize();
		initializer = new GuesstimatorInitializer();
		initializer.ctx = ctx;
//		initializer.initializeComponentType();
	}

	@Override
    public Restlet createInboundRoot() {
    	Router router = new Router(getContext());
    	router.attach("/componenttypes", ComponentTypeCollectionServerResource.class);
        router.attach("/service/{id}", ServiceServerResource.class);
        return router;
    }
}
