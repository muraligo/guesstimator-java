package m3.guesstimator.internal;

import java.util.List;

import m3.guesstimator.internal.data.EstimatorPackageDao;
import m3.guesstimator.internal.data.EstimatorResponse;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.functional.M3Package;

public class PackageResource extends AbstractServerResource<M3Package> {
//    String packageId;

//    @Override
//    protected void doInit() {
//    	super.doInit();
//
//        // extract matched parameters
//    	packageId = getAttribute("package");
//    }

    @Override
    public EstimatorResponse<M3Package> retrieve(String packageId) {
        // TODO Auto-generated method stub
//		return em.find(M3Package.class, packageId);
        return null;
    }

    @Override
    public EstimatorResponse<M3Package> store(M3Package value) {
//		em.persist(value);
//		TypedQuery<M3Package> q = em.createQuery("from Package where name = :name", M3Package.class);
//		q.setParameter("name", value.getName());
//		List<M3Package> result = q.getResultList();
//		if (result != null && !result.isEmpty()) {
//			return result.get(0);
//		}
        EstimatorResponse<M3Package> response = null;
        try {
            M3Package result = getDao().put(value);
            if (result != null) {
                response = new EstimatorResponse<M3Package>(result);
            } else {
    		    // TODO Set a NotFound exception
            }
        } catch (M3ModelFieldsException mfex) {
            // TODO handle the exception
        } catch (M3ModelException mex) {
            // TODO handle the exception
        }
        return response;
    }

    @Override
    public EstimatorResponse<M3Package> remove(String packageId) {
        // TODO Auto-generated method stub
//		em.remove(packageId);
        return null;
    }

    private EstimatorPackageDao getDao() {
        if (dao instanceof EstimatorPackageDao) {
            return (EstimatorPackageDao) dao;
        }
        throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
    }

}
