package m3.guesstimator.model.functional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import m3.guesstimator.model.M3JsonException;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.Model;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.reference.ApplicationType;
import m3.guesstimator.model.reference.ConstructionPhase;

@Entity
@Table(name = "application")
public class Application extends AbstractContainingArtifact {
    private static final long serialVersionUID = 1L;

    private ApplicationType applicationType;
    private Long initiationEstimate;
    private Long deploymentEstimate;

	public Application() {
	}

	@OneToMany(cascade=CascadeType.ALL, mappedBy="parent")
	@Override
	public List<SolutionArtifact> getConstituents() {
		return constituents;
	}

	@Column(name = "APPLICATION_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
	public ApplicationType getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(ApplicationType value) {
		applicationType = value;
	}

	@Column(name = "INITIATION", nullable = false)
	public Long getInitiationEstimate() {
		return initiationEstimate;
	}
	public void setInitiationEstimate(Long value) {
		initiationEstimate = value;
	}

	@Column(name = "DEPLOYMENT", nullable = false)
	public Long getDeploymentEstimate() {
		return deploymentEstimate;
	}
	public void setDeploymentEstimate(Long value) {
		deploymentEstimate = value;
	}

    @Transient
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
    protected void computeFunctionalEstimate() {
        super.computeFunctionalEstimate();
        functionalEstimate *= getApplicationType().getMultiplier();
    }

    @Override
    protected void computeSecurityEstimate() {
        super.computeSecurityEstimate();
        securityEstimate *= getApplicationType().getMultiplier();
    }

    @Override
    protected void computePerformanceEstimate() {
        super.computePerformanceEstimate();
        performanceEstimate *= getApplicationType().getMultiplier();
    }

	@Transient
    protected boolean areOtherEstimatesComputed() {
        return true;
    }

    @Transient
    protected boolean hasConstructOverheads() {
        return true;
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
}
