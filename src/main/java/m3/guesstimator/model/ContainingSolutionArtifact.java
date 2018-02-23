package m3.guesstimator.model;

import java.util.List;

import m3.guesstimator.model.reference.NfrCategory;
import m3.guesstimator.model.reference.VerifyCategory;

public interface ContainingSolutionArtifact extends SolutionArtifact {

    List<SolutionArtifact> getConstituents();
    void setConstituents(List<SolutionArtifact> value);
    Long getConstructNfrEstimate(NfrCategory p);
    Long getVerifyEstimate(VerifyCategory cat);

}
