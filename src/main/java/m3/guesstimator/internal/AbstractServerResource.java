package m3.guesstimator.internal;

import m3.guesstimator.internal.data.AbstractDao;
import m3.guesstimator.internal.data.EstimatorResponse;
import m3.guesstimator.model.SolutionArtifact;

public abstract class AbstractServerResource {
    AbstractDao dao;

    abstract public EstimatorResponse put(SolutionArtifact ent);
    abstract public EstimatorResponse get(String id);
}
