package m3.guesstimator.internal;

import java.util.List;

import m3.guesstimator.internal.data.EstimatorPackageDao;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.functional.M3Package;

public class PackageCollectionResource extends AbstractCollectionResource<M3Package> {

    @Override
    public EstimatorResponse<M3Package> retrieveAll() {
        EstimatorResponse<M3Package> response = null;
        try {
            List<M3Package> results = getDao().find(null);
            if (results != null && !results.isEmpty()) {
                response = new EstimatorResponse<M3Package>(results);
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

    private EstimatorPackageDao getDao() {
        if (dao instanceof EstimatorPackageDao) {
            return (EstimatorPackageDao) dao;
        }
        throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
    }

}
