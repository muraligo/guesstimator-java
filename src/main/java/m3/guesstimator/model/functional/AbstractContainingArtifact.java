package m3.guesstimator.model.functional;

import java.util.Arrays;
import java.util.List;

import m3.guesstimator.model.ConstituentSet;
import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.ParseablePrimaryCollection;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.reference.ConstructionPhase;

public abstract class AbstractContainingArtifact extends AbstractSolutionArtifact
		implements ContainingSolutionArtifact {
    private static final long serialVersionUID = 1L;

    protected List<SolutionArtifact> constituents;
    protected Long functionalEstimate;
    protected Long performanceEstimate;
    protected Long securityEstimate;
    protected String strCompositionFactors;
    protected String strConstructionOverheads;
    // fields below are non-persistent
    transient protected Long[] constructionEstimates;
    transient protected boolean constructEstimateComputed = false;
    transient protected boolean functionEstimateComputed = false;
    transient protected boolean perfEstimateComputed = false;
    transient protected boolean secEstimateComputed = false;
    transient protected boolean verifyEstimateComputed = false;
    transient protected ParseablePrimaryCollection<ConstructionPhase, Long> constructionOverheads;
    transient protected ParseablePrimaryCollection<ConstructionPhase, Long> compositionFactors;
    transient protected ConstituentSet constituentSet = null;

    public AbstractContainingArtifact() {
        constructionEstimates = new Long[ConstructionPhase.values().length];
        Arrays.fill(constructionEstimates, 0L);
        compositionFactors = new ParseablePrimaryCollection<ConstructionPhase, Long>(getClass().getSimpleName(), 
                "CompositionFactors", ConstructionPhase.class, Long.class, 0L);
    }

    @Override
    public Long getFunctionalEstimate() {
        if (! functionEstimateComputed) {
            computeFunctionalEstimate();
        }
        return functionalEstimate;
    }
    @Override
    public void setFunctionalEstimate(Long value) {
        functionalEstimate = value;
        if (areOtherEstimatesComputed()) {
            functionEstimateComputed = false;
            verifyEstimateComputed = false;
        }
    }

    @Override
    public Long getPerformanceEstimate() {
        if (! perfEstimateComputed) {
            computePerformanceEstimate();
        }
        return performanceEstimate;
    }
    @Override
    public void setPerformanceEstimate(Long value) {
        performanceEstimate = value;
        if (areOtherEstimatesComputed()) {
            perfEstimateComputed = false;
            verifyEstimateComputed = false;
        }
    }

    @Override
    public Long getSecurityEstimate() {
        if (! secEstimateComputed) {
            computeSecurityEstimate();
        }
        return securityEstimate;
    }
    @Override
    public void setSecurityEstimate(Long value) {
        securityEstimate = value;
        if (areOtherEstimatesComputed()) {
            secEstimateComputed = false;
            verifyEstimateComputed = false;
        }
    }

    public String getStrCompositionFactors() {
        return strCompositionFactors;
    }
    public void setStrCompositionFactors(String value) {
        strCompositionFactors = value;
        compositionFactors.setStrCollection(value);
        constructEstimateComputed = false;
    }

    public String getStrConstructionOverheads() {
        return strConstructionOverheads;
    }
    public void setStrConstructionOverheads(String value) {
        strConstructionOverheads = value;
        if (hasConstructOverheads()) {
            if (constructionOverheads == null) {
                constructionOverheads = new ParseablePrimaryCollection<ConstructionPhase, Long>(getClass().getSimpleName(), 
                        "ConstructionOverheads", ConstructionPhase.class, Long.class, 0L);
            }
            constructionOverheads.setStrCollection(value);
            constructEstimateComputed = false;
        }
    }

	@Override
    public void setConstituents(List<SolutionArtifact> value) {
        constituents = value;
        constructEstimateComputed = false;
        if (constituentSet == null) {
            constituentSet = new ConstituentSet(value);
        } else {
            constituentSet.refreshElements(value);
        }
    }

    @Override
    public Long getConstructPhaseEstimate(ConstructionPhase phase) {
        if (! constructEstimateComputed) {
            computeConstructEstimate();
            constructEstimateComputed = true;
        }
        return constructionEstimates[phase.ordinal()];
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
        return constructEstimate + getVerificationEstimate();
    }

    public Long getVerificationEstimate() {
        return getFunctionalEstimate() + getPerformanceEstimate() + getSecurityEstimate();
    }

    /**
     * Whether estimates for Verification - Functional, Security, and Performance 
     * are derived or specified.
     * Default is that these values are specified.
     * Must override if derived.
     * @return
     */
    protected boolean areOtherEstimatesComputed() {
        return false;
    }

    /**
     * Whether Construction Overheads need to be considered for this Container.
     * Default is that no Construction Overheads need to be considered.
     * Must override if Construction Overheads need to be considered.
     * @return
     */
    protected boolean hasConstructOverheads() {
        return false;
	}

    protected void computeConstructEstimate() {
        for (ConstructionPhase p : ConstructionPhase.values()) {
            int ip = p.ordinal();
            constructionEstimates[ip] = 0L; // get rid of old value
            if (getConstituents() != null) {
                for (SolutionArtifact sa : getConstituents()) {
                    constructionEstimates[ip] += sa.getConstructPhaseEstimate(p);
                }
            }
            constructionEstimates[ip] *= compositionFactors.get(ip);
            if (hasConstructOverheads() && constructionEstimates[ip] != 0) {
                constructionEstimates[ip] += constructionOverheads.get(ip);
            }
        }
    }

    protected void computeFunctionalEstimate() {
        if (areOtherEstimatesComputed() && constituents != null & ! constituents.isEmpty()) {
            SolutionArtifact sa = constituents.get(0);
            if (sa instanceof ContainingSolutionArtifact) {
                functionalEstimate = 0L;
                constituents.forEach(sa2 -> {
                    ContainingSolutionArtifact csa = (ContainingSolutionArtifact)sa2;
                    functionalEstimate += csa.getFunctionalEstimate();
                });
                functionEstimateComputed = true;
                if (secEstimateComputed && perfEstimateComputed) {
                    verifyEstimateComputed = true;
                }
            }
        }
    }

    protected void computeSecurityEstimate() {
        if (areOtherEstimatesComputed() && constituents != null & ! constituents.isEmpty()) {
            SolutionArtifact sa = constituents.get(0);
            if (sa instanceof ContainingSolutionArtifact) {
            	securityEstimate = 0L;
                constituents.forEach(sa2 -> {
                    ContainingSolutionArtifact csa = (ContainingSolutionArtifact)sa2;
                    securityEstimate += csa.getSecurityEstimate();
                });
            }
            secEstimateComputed = false;
            if (functionEstimateComputed && perfEstimateComputed) {
                verifyEstimateComputed = true;
            }
        }
    }

    protected void computePerformanceEstimate() {
        if (areOtherEstimatesComputed() && constituents != null & ! constituents.isEmpty()) {
            SolutionArtifact sa = constituents.get(0);
            if (sa instanceof ContainingSolutionArtifact) {
            	performanceEstimate = 0L;
                constituents.forEach(sa2 -> {
                    ContainingSolutionArtifact csa = (ContainingSolutionArtifact)sa2;
                    performanceEstimate += csa.getPerformanceEstimate();
                });
                perfEstimateComputed = true;
                if (functionEstimateComputed && secEstimateComputed) {
                    verifyEstimateComputed = true;
                }
            }
        }
    }
}
