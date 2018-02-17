package m3.guesstimator.internal;

import m3.guesstimator.internal.data.AbstractDao;
import m3.guesstimator.internal.data.EstimatorResponse;

public abstract class AbstractCollectionResource {
    AbstractDao dao;

    abstract public EstimatorResponse findAll();
}
