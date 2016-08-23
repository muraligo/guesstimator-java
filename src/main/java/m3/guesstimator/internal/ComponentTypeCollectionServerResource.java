package m3.guesstimator.internal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.restlet.Application;
import org.restlet.resource.ServerResource;

import m3.guesstimator.model.reference.ComponentType;
import m3.guesstimator.service.ComponentTypeCollectionResource;

public class ComponentTypeCollectionServerResource extends ServerResource implements ComponentTypeCollectionResource {
    EntityManager em;

    @Override
    protected void doInit() {
    	GuesstimatorApplication gapp = getApp();
    	em = gapp.ctx.getEntityManager();
    }

	@Override
	public List<ComponentType> retrieveAll() {
		TypedQuery<ComponentType> q = em.createQuery("SELECT ct FROM ComponentType AS ct", ComponentType.class);
		List<ComponentType> result = q.getResultList();
		if (result != null && !result.isEmpty()) {
			return result;
		}
		return null;
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
