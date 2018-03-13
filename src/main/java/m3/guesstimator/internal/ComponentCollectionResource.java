package m3.guesstimator.internal;

import java.util.HashMap;
import java.util.List;

import m3.guesstimator.internal.data.EstimatorComponentDao;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.functional.M3Component;

public class ComponentCollectionResource extends AbstractCollectionResource<M3Component> {

    @Override
    public EstimatorResponse<M3Component> retrieveAll() {
        EstimatorResponse<M3Component> response = null;
        try {
            List<M3Component> results = getDao().find(null);
            if (results != null && !results.isEmpty()) {
                response = new EstimatorResponse<M3Component>(results);
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

    private EstimatorComponentDao getDao() {
        if (dao instanceof EstimatorComponentDao) {
            return (EstimatorComponentDao) dao;
        }
        throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
    }

    public static HashMap<String, Object> toTableAndJsonWithIndex(EstimatorResponse<M3Component> resp) {
        // TODO Implement
        return null;
    }
}
