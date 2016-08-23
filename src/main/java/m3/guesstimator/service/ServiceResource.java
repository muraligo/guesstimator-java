package m3.guesstimator.service;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import m3.guesstimator.model.functional.Service;

public interface ServiceResource {

	@Get("json")
    Service retrieve();

	@Put("json")
	Service store(Service value);

	@Delete("json")
    void remove();
}
