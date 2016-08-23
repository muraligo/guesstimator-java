package m3.guesstimator.service;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import m3.guesstimator.model.functional.Application;

public interface ApplicationResource {

	@Get("json")
	Application retrieve();

	@Put("json")
	Application store(Application value);

	@Delete("json")
    void remove();
}
