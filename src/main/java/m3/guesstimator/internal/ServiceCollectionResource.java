package m3.guesstimator.internal;

import java.util.List;

import m3.guesstimator.internal.data.EstimatorServiceDao;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.functional.M3Service;

public class ServiceCollectionResource extends AbstractCollectionResource<M3Service> {

    @Override
    public EstimatorResponse<M3Service> retrieveAll() {
        EstimatorResponse<M3Service> response = null;
        try {
            List<M3Service> results = getDao().find(null);
            if (results != null && !results.isEmpty()) {
                response = new EstimatorResponse<M3Service>(results);
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

    private EstimatorServiceDao getDao() {
        if (dao instanceof EstimatorServiceDao) {
            return (EstimatorServiceDao) dao;
        }
        throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
    }

}
