package m3.guesstimator.internal;

import java.util.List;

import m3.guesstimator.internal.data.EstimatorResponse;
// TODO change below to Service and checkin both this and DAO
import m3.guesstimator.internal.data.EstimatorSubsystemDao;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.functional.M3Service;
import m3.guesstimator.model.functional.M3Subsystem;

public class ServiceCollectionResource extends AbstractCollectionResource<M3Service> {

    @Override
    public EstimatorResponse<M3Service> retrieveAll() {
        EstimatorResponse<M3Service> response = null;
        try {
            // TODO Change below to Service and checkin both this and DAO
            List<M3Subsystem> results = getDao().find(null);
            if (results != null && !results.isEmpty()) {
                response = null; // new EstimatorResponse<M3Service>(results);
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

    //TODO change below to Service and checkin both this and DAO
    private EstimatorSubsystemDao getDao() {
        if (dao instanceof EstimatorSubsystemDao) {
            return (EstimatorSubsystemDao) dao;
        }
        throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
    }

}
