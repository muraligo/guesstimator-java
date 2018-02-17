package m3.guesstimator.internal;

import java.util.List;

import m3.guesstimator.internal.data.EstimatorComponentDao;
import m3.guesstimator.internal.data.EstimatorResponse;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.functional.Component;

public class ComponentCollectionResource extends AbstractCollectionResource {

    @Override
    public EstimatorResponse findAll() {
        EstimatorResponse response = null;
        try {
            List<SolutionArtifact> results = getDao().find(new Component(), null);
            response = new EstimatorResponse(results);
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

}
