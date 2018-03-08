package m3.guesstimator.internal;

import java.util.List;

import m3.guesstimator.internal.data.EstimatorSubsystemDao;
import m3.guesstimator.internal.data.EstimatorResponse;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.functional.M3Subsystem;

public class SubsystemCollectionResource extends AbstractCollectionResource<M3Subsystem> {

    @Override
    public EstimatorResponse<M3Subsystem> retrieveAll() {
        EstimatorResponse<M3Subsystem> response = null;
        try {
            List<M3Subsystem> results = getDao().find(null);
            if (results != null && !results.isEmpty()) {
                response = new EstimatorResponse<M3Subsystem>(results);
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

    private EstimatorSubsystemDao getDao() {
        if (dao instanceof EstimatorSubsystemDao) {
            return (EstimatorSubsystemDao) dao;
        }
        throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
    }

}
