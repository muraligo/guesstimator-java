package m3.guesstimator.internal;

import m3.guesstimator.internal.data.AbstractDao;
import m3.guesstimator.internal.data.EstimatorResponse;
import m3.guesstimator.model.SolutionArtifact;

public abstract class AbstractServerResource<E extends SolutionArtifact> {
    AbstractDao dao;

    abstract public EstimatorResponse<E> store(E ent);
    abstract public EstimatorResponse<E> retrieve(String id);
    abstract public EstimatorResponse<E> remove(String id);
}
