package m3.guesstimator.model.functional;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.reference.Complexity;
import m3.guesstimator.model.reference.ComponentType;
import m3.guesstimator.model.reference.ConstructionPhase;
import m3.guesstimator.model.reference.Language;
import m3.guesstimator.model.reference.Layer;

public class Component extends AbstractSolutionArtifact {
    private static final long serialVersionUID = 1L;

	private ContainingSolutionArtifact parent;
	private ComponentType type;
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

    public ComponentType getType() {
        return type;
    }
    public void setType(ComponentType value) {
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

    private void computeConstructEstimate() {
        for (ConstructionPhase p : ConstructionPhase.values()) {
            Long estimate = getType().getConstructCost(p);
            estimate *= getComplexity().getMultiplier();
            estimate *= count;
            constructionEstimates[p.ordinal()] = estimate;
        }
    }

/*
    @Override
    protected String getOtherFieldValue(String fldName) {
        String value = null;
        if ("complexity".equals(fldName))
            value = "'" + getComplexity().name() + "'";
        else if ("layer".equals(fldName))
            value = "'" + getLayer().name() + "'";
        else if ("language".equals(fldName))
            value = "'" + getLanguage().name() + "'";
        else if ("count".equals(fldName))
            value = getCount().toString();
        else if ("type".equals(fldName))
            value = getType().getName(); // name is the id which is the foreign key
        else if ("parent".equals(fldName))
            value = getParent().getName(); // name is the id which is the foreign key
        return value;
    }
*/
	@Override
	public SolutionArtifact createNew() {
		// TODO Auto-generated method stub
		return null;
	}

}
