package m3.guesstimator.model.functional;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.reference.Complexity;
import m3.guesstimator.model.reference.M3ComponentType;
import m3.guesstimator.model.reference.ConstructionPhase;
import m3.guesstimator.model.reference.Language;
import m3.guesstimator.model.reference.Layer;

public class M3Component extends AbstractSolutionArtifact {
    private static final long serialVersionUID = 1L;

	private ContainingSolutionArtifact parent;
	private M3ComponentType type;
	private Complexity complexity;
	private Layer layer;
	private Language language;
	private Long count;
	// fields below are non-persistent
	private Long[] constructionEstimates;
	private boolean constructEstimateComputed = false;

    public ContainingSolutionArtifact getParent() {
        return parent;
	}
    public void setParent(ContainingSolutionArtifact parent) {
        this.parent = parent;
    }

    public M3ComponentType getType() {
        return type;
    }
    public void setType(M3ComponentType value) {
        type = value;
        constructEstimateComputed = false;
    }

    public Complexity getComplexity() {
        return complexity;
    }
    public void setComplexity(Complexity value) {
        complexity = value;
        constructEstimateComputed = false;
    }

    public Layer getLayer() {
        return layer;
    }
    public void setLayer(Layer value) {
        layer = value;
    }

    public Language getLanguage() {
        return language;
    }
    public void setLanguage(Language value) {
        language = value;
    }

    public Long getCount() {
        return count;
    }
    public void setCount(Long value) {
        count = value;
        constructEstimateComputed = false;
    }

    @Override
    public Long getConstructPhaseEstimate(ConstructionPhase phase) {
        if (! constructEstimateComputed) {
            computeConstructEstimate();
            constructEstimateComputed = true;
        }
        return constructionEstimates[phase.ordinal()];
    }

    /**
     * Only provides construction estimate as a computed value 
     * as there are only construction estimates at this level.
     */
    @Override
    public Long getEstimate() {
        Long estimate = 0L;

        for (ConstructionPhase p : ConstructionPhase.values()) {
            estimate += getConstructPhaseEstimate(p);
        }

        return estimate;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Component#");
        sb.append(super.toString());
        sb.append("{");
        sb.append(type);
        sb.append(", complexity:");
        sb.append(complexity);
        sb.append(", layer:");
        sb.append(layer);
        sb.append(", language:");
        sb.append(language);
        sb.append(", count");
        sb.append(count);
        sb.append("}");
        return sb.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    private void computeConstructEstimate() {
        for (ConstructionPhase p : ConstructionPhase.values()) {
            Long estimate = getType().getConstructCost(p);
            estimate *= getComplexity().getMultiplier();
            estimate *= count;
            constructionEstimates[p.ordinal()] = estimate;
        }
    }
}
