package m3.guesstimator.service;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import m3.guesstimator.model.functional.Subsystem;

public interface SubsystemResource {

	@Get("json")
	Subsystem retrieve();

	@Put("json")
	Subsystem store(Subsystem value);

	@Delete("json")
    void remove();
}
