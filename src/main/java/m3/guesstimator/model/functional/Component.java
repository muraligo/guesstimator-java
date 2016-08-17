package m3.guesstimator.model.functional;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import m3.guesstimator.model.Model;
import m3.guesstimator.model.reference.Complexity;
import m3.guesstimator.model.reference.ComponentType;
import m3.guesstimator.model.reference.ConstructionPhase;
import m3.guesstimator.model.reference.Language;
import m3.guesstimator.model.reference.Layer;

@Entity
@Table(name = "component")
public class Component implements Model {
	private String name;
	private String description;
	private String version;
	private Model parent;
	private ComponentType type;
	private Complexity complexity;
	private Layer layer;
	private Language language;
	private Long count;
	// fields below are non-persistent
	private Long[] constructionEstimates;
	private boolean constructEstimateComputed = false;

    @Id
	@Column(name = "name", nullable = false, length = 32)
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String value) {
		name = value;
	}

	@Column(name = "description", length = 1024)
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String value) {
		description = value;
	}

	@Column(name = "version", length = 8)
	@Override
	public String getVersion() {
		return version;
	}
	@Override
	public void setVersion(String value) {
		version = value;
	}

	@Override
	public List<Model> getConstituents() {
		return null;
	}
	@Override
	public void setConstituents(List<Model> value) {
		throw new UnsupportedOperationException();
	}

	@ManyToOne(optional=false, targetEntity=m3.guesstimator.model.functional.Package.class)
	@JoinColumn(name="parent", nullable=false)
	public Model getParent() {
		return parent;
	}
	public void setParent(Model parent) {
		this.parent = parent;
	}

	@ManyToOne(optional=false)
	@JoinColumn(name="component_type", nullable=false)
	public ComponentType getType() {
		return type;
	}
	public void setType(ComponentType value) {
		type = value;
		constructEstimateComputed = false;
	}

	@Column(name = "complexity", nullable = false)
    @Enumerated(EnumType.STRING)
	public Complexity getComplexity() {
		return complexity;
	}
	public void setComplexity(Complexity complexity) {
		this.complexity = complexity;
		constructEstimateComputed = false;
	}

	@Column(name = "layer", nullable = false)
    @Enumerated(EnumType.STRING)
	public Layer getLayer() {
		return layer;
	}
	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	@Column(name = "language", nullable = false)
    @Enumerated(EnumType.STRING)
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}

	@Column(name = "count", nullable = false)
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
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
