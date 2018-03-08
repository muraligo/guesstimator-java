package m3.guesstimator.internal;

import m3.guesstimator.internal.data.AbstractDao;
import m3.guesstimator.internal.data.EstimatorResponse;
import m3.guesstimator.model.SolutionArtifact;

public abstract class AbstractCollectionResource<E extends SolutionArtifact> {
    AbstractDao dao;

    abstract public EstimatorResponse<E> retrieveAll();
}
