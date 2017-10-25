package m3.guesstimator.model;

import java.util.List;

public interface ContainingSolutionArtifact extends SolutionArtifact {

    List<SolutionArtifact> getConstituents();
    void setConstituents(List<SolutionArtifact> value);
    Long getFunctionalEstimate();
    void setFunctionalEstimate(Long value);
    Long getPerformanceEstimate();
    void setPerformanceEstimate(Long value);
    Long getSecurityEstimate();
    void setSecurityEstimate(Long value);

}
