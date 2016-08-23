package m3.guesstimator.internal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.restlet.Application;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import m3.guesstimator.model.functional.Service;
import m3.guesstimator.service.ServiceResource;

//This resource is mapped to path "/service/{id}"
public class ServiceServerResource extends ServerResource implements ServiceResource {
    EntityManager em;

    String serviceId;

    @Override
    protected void doInit() {
    	GuesstimatorApplication gapp = getApp();
    	em = gapp.ctx.getEntityManager();

        // extract matched parameters
    	serviceId = getAttribute("id");
    }

    @Override
	@Get("json")
	public Service retrieve() {
		return em.find(Service.class, serviceId);
	}

	@Override
	@Put("json")
	public Service store(Service value) {
		em.persist(value);
		TypedQuery<Service> q = em.createQuery("from Issue where description = :desc", Service.class);
		q.setParameter("desc", value.getDescription());
		List<Service> result = q.getResultList();
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	@Delete("json")
	public void remove() {
		em.remove(serviceId);
	}

	@Override
	protected void doRelease() {
		if (em != null) {
	    	GuesstimatorApplication gapp = getApp();
	    	gapp.ctx.releaseEntityManager();
	    	em = null;
		}
	}

	private GuesstimatorApplication getApp() {
    	Application app = getApplication();
    	if (app instanceof GuesstimatorApplication) {
    		return (GuesstimatorApplication)app;
    	} else {
    		// TODO throw
    	}
    	return null;
	}
}
