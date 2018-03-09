package m3.guesstimator.internal;

import java.util.List;

//TODO change below to Application and checkin both this and DAO
import m3.guesstimator.internal.data.EstimatorSubsystemDao;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.functional.M3Application;
import m3.guesstimator.model.functional.M3Subsystem;

public class ApplicationCollectionResource extends AbstractCollectionResource<M3Application> {

	@Override
	public EstimatorResponse<M3Application> retrieveAll() {
        EstimatorResponse<M3Application> response = null;
        try {
            // TODO Change below to Application and checkin both this and DAO
            List<M3Subsystem> results = getDao().find(null);
            if (results != null && !results.isEmpty()) {
                response = null; // new EstimatorResponse<M3Application>(results);
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

	//TODO change below to Application and checkin both this and DAO
    private EstimatorSubsystemDao getDao() {
        if (dao instanceof EstimatorSubsystemDao) {
            return (EstimatorSubsystemDao) dao;
        }
        throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
    }

}
