package m3.guesstimator.internal;

//TODO change below to Application and checkin both this and DAO
import m3.guesstimator.internal.data.EstimatorSubsystemDao;
import m3.guesstimator.model.functional.M3Application;

public class ApplicationResource extends AbstractServerResource<M3Application> {
//    String applicationId;

//    @Override
//    protected void doInit() {
//    	super.doInit();
//
//        // extract matched parameters
//    	applicationId = getAttribute("application");
//    	System.out.println("Initialization complete");
//    }

    @Override
    public EstimatorResponse<M3Application> retrieve(String applicationId) {
        // TODO Auto-generated method stub
//		return em.find(M3Application.class, applicationId);
        return null;
    }

    @Override
    public EstimatorResponse<M3Application> store(M3Application value) {
        System.out.println("Entered store with " + value.getName());
        System.out.println(value);
        // TODO Auto-generated method stub
//		em.persist(value);
//		TypedQuery<M3Application> q = em.createQuery("from Application where name = :name", M3Application.class);
//		q.setParameter("name", value.getName());
//		List<M3Application> result = q.getResultList();
//		if (result != null && !result.isEmpty()) {
//			return result.get(0);
//		}
        return null;
    }

    @Override
    public EstimatorResponse<M3Application> remove(String applicationId) {
        // TODO Auto-generated method stub
//		em.remove(applicationId);
        return null;
    }

	//TODO change below to Application and checkin both this and DAO
    private EstimatorSubsystemDao getDao() {
        if (dao instanceof EstimatorSubsystemDao) {
            return (EstimatorSubsystemDao) dao;
        }
        throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
    }

}
