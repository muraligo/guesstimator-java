package m3.guesstimator.model.functional;

import java.util.Arrays;
import java.util.List;

import m3.guesstimator.model.ConstituentSet;
import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.ParseablePrimaryCollection;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.reference.ConstructionPhase;
import m3.guesstimator.model.reference.NfrCategory;
import m3.guesstimator.model.reference.VerifyCategory;

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
    transient protected Long[] nfrBuildEstimates;
    transient protected Long[] verificationEstimates;
    transient protected Long[] constructionEstimates;
    transient protected boolean constructEstimateComputed = false;
    transient protected boolean verifyEstimateComputed = false;
    transient protected ParseablePrimaryCollection<ConstructionPhase, Long> constructionOverheads;
    transient protected ParseablePrimaryCollection<ConstructionPhase, Long> compositionFactors;
    transient protected ConstituentSet constituentSet = null;

    public AbstractContainingArtifact() {
        constructionEstimates = new Long[ConstructionPhase.values().length];
        Arrays.fill(constructionEstimates, 0L);
        nfrBuildEstimates = new Long[NfrCategory.values().length];
        Arrays.fill(nfrBuildEstimates, 0L);
        verificationEstimates = new Long[VerifyCategory.values().length];
        Arrays.fill(verificationEstimates, 0L);
        compositionFactors = new ParseablePrimaryCollection<ConstructionPhase, Long>(getClass().getSimpleName(), 
                "CompositionFactors", ConstructionPhase.class, Long.class, 0L);
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
    public Long getConstructNfrEstimate(NfrCategory cat) {
        if (! constructEstimateComputed) {
            computeConstructEstimate();
            constructEstimateComputed = true;
        }
        return nfrBuildEstimates[cat.ordinal()];
    }

    @Override
    public Long getVerifyEstimate(VerifyCategory cat) {
        if (! verifyEstimateComputed) {
            computeVerifyEstimate();
            verifyEstimateComputed = true;
        }
        return verificationEstimates[cat.ordinal()];
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
        for (NfrCategory p : NfrCategory.values()) {
            constructEstimate += nfrBuildEstimates[p.ordinal()];
        }
        return constructEstimate + getVerificationEstimate();
    }

    public Long getVerificationEstimate() {
        if (! verifyEstimateComputed) {
            computeVerifyEstimate();
            verifyEstimateComputed = true;
        }
        Long verifyEstimate = 0L;
        for (VerifyCategory p : VerifyCategory.values()) {
        	verifyEstimate += verificationEstimates[p.ordinal()];
        }
        return verifyEstimate;
    }

    /**
     * Whether Construction Overheads need to be considered for this Container.
     * Default is that Construction Overheads need to be considered.
     * Must override if Construction Overheads must not to be considered.
     * @return
     */
    protected boolean hasConstructOverheads() {
        return true;
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
        computeNfrBuildEstimate();
    }

    abstract protected void computeNfrBuildEstimate();

    abstract protected void computeVerifyEstimate();
}
