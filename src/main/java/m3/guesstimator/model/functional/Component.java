package m3.guesstimator.model.functional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.reference.Complexity;
import m3.guesstimator.model.reference.ComponentType;
import m3.guesstimator.model.reference.ConstructionPhase;
import m3.guesstimator.model.reference.Language;
import m3.guesstimator.model.reference.Layer;

@Entity
@Table(name = "COMPONENT")
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

    @ManyToOne(optional=false, targetEntity=m3.guesstimator.model.functional.Package.class)
    @JoinColumn(name="PARENT", nullable=false)
    public ContainingSolutionArtifact getParent() {
        return parent;
	}
    public void setParent(ContainingSolutionArtifact parent) {
        this.parent = parent;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COMPONENT_TYPE", nullable=false)
    public ComponentType getType() {
        return type;
    }
    public void setType(ComponentType value) {
        type = value;
        constructEstimateComputed = false;
    }

    @Column(name = "COMPLEXITY", nullable = false)
    @Enumerated(EnumType.STRING)
    public Complexity getComplexity() {
        return complexity;
    }
    public void setComplexity(Complexity value) {
        complexity = value;
        constructEstimateComputed = false;
    }

    @Column(name = "LAYER", nullable = false)
    @Enumerated(EnumType.STRING)
    public Layer getLayer() {
        return layer;
    }
    public void setLayer(Layer value) {
        layer = value;
    }

    @Column(name = "LANGUAGE", nullable = false)
    @Enumerated(EnumType.STRING)
    public Language getLanguage() {
        return language;
    }
    public void setLanguage(Language value) {
        language = value;
    }

    @Column(name = "COUNT", nullable = false)
    public Long getCount() {
        return count;
    }
    public void setCount(Long value) {
        count = value;
        constructEstimateComputed = false;
    }

    @Transient
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
    @Transient
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
}
