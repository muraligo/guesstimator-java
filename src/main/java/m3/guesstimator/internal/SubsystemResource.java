package m3.guesstimator.internal;

import m3.guesstimator.internal.data.EstimatorResponse;
import m3.guesstimator.internal.data.EstimatorSubsystemDao;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.functional.M3Subsystem;

public class SubsystemResource extends AbstractServerResource<M3Subsystem> {
//    String subsystemId;

//    @Override
//    protected void doInit() {
//    	super.doInit();
//
//        // extract matched parameters
//    	subsystemId = getAttribute("subsystem");
//    }

    @Override
    public EstimatorResponse<M3Subsystem> retrieve(String subsystemId) {
//        return em.find(M3Subsystem.class, subsystemId);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EstimatorResponse<M3Subsystem> store(M3Subsystem value) {
//        em.persist(value);
//        TypedQuery<M3Subsystem> q = em.createQuery("from Subsystem where name = :name", M3Subsystem.class);
//        q.setParameter("name", value.getName());
//        List<M3Subsystem> result = q.getResultList();
//        if (result != null && !result.isEmpty()) {
//            return result.get(0);
//        }
        EstimatorResponse<M3Subsystem> response = null;
        try {
            M3Subsystem result = getDao().put(value);
            if (result != null) {
                response = new EstimatorResponse<M3Subsystem>(result);
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
    public EstimatorResponse<M3Subsystem> remove(String subsystemId) {
//        em.remove(subsystemId);
        // TODO Auto-generated method stub
        return null;
    }

    private EstimatorSubsystemDao getDao() {
        if (dao instanceof EstimatorSubsystemDao) {
            return (EstimatorSubsystemDao) dao;
        }
        throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
    }

}
