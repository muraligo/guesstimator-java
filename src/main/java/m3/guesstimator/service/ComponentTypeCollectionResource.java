package m3.guesstimator.service;

import java.util.List;

import org.restlet.resource.Get;

import m3.guesstimator.model.reference.ComponentType;

public interface ComponentTypeCollectionResource {


	@Get
	List<ComponentType> retrieveAll();
}
