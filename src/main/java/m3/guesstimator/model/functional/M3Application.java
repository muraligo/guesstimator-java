package m3.guesstimator.model.functional;

import java.util.List;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.reference.ApplicationType;
import m3.guesstimator.model.reference.ConstructionPhase;
import m3.guesstimator.model.reference.NfrCategory;
import m3.guesstimator.model.reference.VerifyCategory;

public class M3Application extends AbstractContainingArtifact {
    private static final long serialVersionUID = 1L;

    private ApplicationType applicationType;
    private Long initiationEstimate;
    private Long deploymentEstimate;

	public M3Application() {
	}

	@Override
	public List<SolutionArtifact> getConstituents() {
		return constituents;
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(ApplicationType value) {
		applicationType = value;
	}

	public Long getInitiationEstimate() {
		return initiationEstimate;
	}
	public void setInitiationEstimate(Long value) {
		initiationEstimate = value;
	}

	public Long getDeploymentEstimate() {
		return deploymentEstimate;
	}
	public void setDeploymentEstimate(Long value) {
		deploymentEstimate = value;
	}

    @Override
    public Long getEstimate() {
        if (! constructEstimateComputed) {
            computeConstructEstimate();
            constructEstimateComputed = true;
        }
        Long constructEstimate = 0L;
        for (ConstructionPhase p : ConstructionPhase.values()) {
            constructEstimate += constructionEstimates[p.ordinal()];
        }
        constructEstimate *= getApplicationType().getMultiplier();

        return getInitiationEstimate() + constructEstimate + getVerificationEstimate() + getDeploymentEstimate();
	}

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Application#" + name + "." + version + "{");
        sb.append(applicationType);
        sb.append(", Estimates {init=");
        sb.append(initiationEstimate);
        sb.append(", deploy=");
        sb.append(deploymentEstimate);
        sb.append("}, compose=");
        sb.append(strCompositionFactors);
        sb.append(", overhead=");
        sb.append(strConstructionOverheads);
        sb.append("}");
        return sb.toString();
    }

    protected boolean hasConstructOverheads() {
        return true;
	}

    @Override
    protected void computeNfrBuildEstimate() {
        for (NfrCategory p : NfrCategory.values()) {
            int ip = p.ordinal();
            nfrBuildEstimates[ip] = 0L; // get rid of old value
            if (getConstituents() != null && !getConstituents().isEmpty() && (getConstituents().get(0) instanceof ContainingSolutionArtifact)) {
                getConstituents().forEach(sa -> {
                    ContainingSolutionArtifact csa = (ContainingSolutionArtifact)sa;
                    nfrBuildEstimates[ip] += csa.getConstructNfrEstimate(p);
                });
            }
        }
        // TODO Handle applicationType
    }

    @Override
    protected void computeVerifyEstimate() {
        for (VerifyCategory p : VerifyCategory.values()) {
            int ip = p.ordinal();
            verificationEstimates[ip] = 0L; // get rid of old value
            if (getConstituents() != null && !getConstituents().isEmpty() && (getConstituents().get(0) instanceof ContainingSolutionArtifact)) {
                getConstituents().forEach(sa -> {
                    ContainingSolutionArtifact csa = (ContainingSolutionArtifact)sa;
                    verificationEstimates[ip] += csa.getVerifyEstimate(p);
                });
            }
        }
        // TODO Handle applicationType
    }
}
