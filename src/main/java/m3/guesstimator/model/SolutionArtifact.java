package m3.guesstimator.model;

import java.io.Serializable;

import m3.guesstimator.model.reference.ConstructionPhase;

public interface SolutionArtifact extends Serializable, Comparable<SolutionArtifact>, Cloneable {

    String getName();
    void setName(String value);
    String getDescription();
    void setDescription(String value);
    String getVersion();
    void setVersion(String value);
    Long getConstructPhaseEstimate(ConstructionPhase phase);
    Long getEstimate();

}
