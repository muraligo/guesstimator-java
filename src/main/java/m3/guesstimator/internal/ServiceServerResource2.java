package m3.guesstimator.internal;

import m3.guesstimator.internal.data.EstimatorResponse;
import m3.guesstimator.internal.data.EstimatorServiceDao;
import m3.guesstimator.model.SolutionArtifact;

import java.util.List;

//This resource is mapped to path "/service/{id}"
public class ServiceServerResource extends AbstractServerResource {

//  @Override
//  @Delete("json")
//  public void remove() {
//  	em.remove(serviceId);
//  }

//    @Override
//    protected void doRelease() {
//    	if (em != null) {
//        	GuesstimatorApplication gapp = getApp();
//        	gapp.ctx.releaseEntityManager();
//        	em = null;
//    	}
//    }

    @Override
//  @Put("json")
    public EstimatorResponse put(SolutionArtifact ent) {
//      em.persist(value);
        return null;
    }

    @Override
//  @Get("json")
    public EstimatorResponse get(String id) {
		// return em.find(Service.class, serviceId);
        return null;
    }

	private EstimatorServiceDao getDao() {
		if (dao instanceof EstimatorServiceDao) {
			return (EstimatorServiceDao) dao;
		}
		throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
	}
}
