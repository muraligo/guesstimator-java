package m3.guesstimator.internal;

import m3.guesstimator.internal.data.EstimatorServiceDao;
import m3.guesstimator.model.functional.M3Service;

public class ServiceResource extends AbstractServerResource<M3Service> {
    @Override
    public EstimatorResponse<M3Service> store(M3Service ent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EstimatorResponse<M3Service> retrieve(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EstimatorResponse<M3Service> remove(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    private EstimatorServiceDao getDao() {
        if (dao instanceof EstimatorServiceDao) {
            return (EstimatorServiceDao) dao;
        }
        throw new ClassCastException("Invalid dao class " + dao.getClass().getName() + ".");
    }
}
