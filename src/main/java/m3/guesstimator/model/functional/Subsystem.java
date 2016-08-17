package m3.guesstimator.model.functional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import m3.guesstimator.model.M3JsonException;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.Model;
import m3.guesstimator.model.reference.ConstructionPhase;

@Entity
@Table(name = "subsystem")
public class Subsystem implements Model {
	private String name;
	private String description;
	private String version;
	private List<Model> constituents;
	private Model parent;
	private String strCompositionFactors;
	private Long functionalEstimate;
	private Long performanceEstimate;
	private Long securityEstimate;
	private String strConstructionOverheads;
	// fields below are non-persistent
	private Long[] compositionFactors;
	private LocalDateTime compFactorParsedAt;
	private LocalDateTime compFactorUpdatedAt;
	private Long[] constructionEstimates;
	private Long[] constructionOverheads;
	private LocalDateTime constructOverheadParsedAt;
	private LocalDateTime constructOverheadUpdatedAt;
	private boolean constructEstimateComputed = false;

	public Subsystem() {
		compositionFactors = new Long[ConstructionPhase.values().length];
		Arrays.fill(compositionFactors, 0L);
		constructionEstimates = new Long[ConstructionPhase.values().length];
		constructionOverheads = new Long[ConstructionPhase.values().length];
		Arrays.fill(constructionEstimates, 0L);
		Arrays.fill(constructionOverheads, 0L);
		LocalDateTime currTime = LocalDateTime.now();
		compFactorParsedAt = null;
		compFactorUpdatedAt = currTime;
		constructOverheadParsedAt = null;
		constructOverheadUpdatedAt = currTime;
	}

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

	@OneToMany(cascade=CascadeType.ALL, targetEntity=m3.guesstimator.model.functional.Package.class, mappedBy="parent")
	@Override
	public List<Model> getConstituents() {
		return constituents;
	}
	@Override
	public void setConstituents(List<Model> value) {
		constituents = value;
		constructEstimateComputed = false;
	}

	@Column(name = "composition_factors", nullable = false)
	public String getStrCompositionFactors() {
		return strCompositionFactors;
	}
	public void setStrCompositionFactors(String value) {
		strCompositionFactors = value;
		compFactorUpdatedAt = LocalDateTime.now();
		constructEstimateComputed = false;
	}

	@Column(name = "functional", nullable = false)
	public Long getFunctionalEstimate() {
		return functionalEstimate;
	}
	public void setFunctionalEstimate(Long value) {
		functionalEstimate = value;
	}

	@Column(name = "performance", nullable = false)
	public Long getPerformanceEstimate() {
		return performanceEstimate;
	}
	public void setPerformanceEstimate(Long value) {
		performanceEstimate = value;
	}

	@Column(name = "security", nullable = false)
	public Long getSecurityEstimate() {
		return securityEstimate;
	}
	public void setSecurityEstimate(Long value) {
		securityEstimate = value;
	}

	@Column(name = "construct_overheads", nullable = false)
	public String getStrConstructionOverheads() {
		return strConstructionOverheads;
	}
	public void setStrConstructionOverheads(String value) {
		strConstructionOverheads = value;
		constructOverheadUpdatedAt = LocalDateTime.now();
		constructEstimateComputed = false;
	}

	@ManyToOne(targetEntity=m3.guesstimator.model.functional.Application.class)
	@JoinColumn(name="parent", nullable=false)
	public Model getParent() {
		return parent;
	}
	public void setParent(Model parent) {
		this.parent = parent;
	}

	@Transient
	public Long getCompositionFactor(ConstructionPhase phase) {
		return compositionFactors[phase.ordinal()];
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
	 */
	@Transient
	@Override
	public Long getEstimate() {
		Long estimate = 0L;
		prepareEstimateInputs();

		for (ConstructionPhase p : ConstructionPhase.values()) {
			estimate += getConstructPhaseEstimate(p);
		}

		return estimate;
	}

	private void prepareEstimateInputs() {
		if (constructOverheadParsedAt == null || constructOverheadUpdatedAt.isAfter(constructOverheadParsedAt)) {
			try {
				parseConstructPhaseValues("ConstructionOverheads", strConstructionOverheads, constructionOverheads);
				constructOverheadParsedAt = LocalDateTime.now();
			} catch (M3JsonException|M3ModelException e) {
				e.printStackTrace();
				throw e;
			}
		}
		if (compFactorParsedAt == null || compFactorUpdatedAt.isAfter(compFactorParsedAt)) {
			try {
				parseConstructPhaseValues("CompositionFactors", strCompositionFactors, compositionFactors);
				compFactorParsedAt = LocalDateTime.now();
			} catch (M3JsonException|M3ModelException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	private void computeConstructEstimate() {
		for (ConstructionPhase p : ConstructionPhase.values()) {
			int ip = p.ordinal();
			constructionEstimates[ip] = 0L;
			for (Model m : getConstituents()) {
				constructionEstimates[ip] += m.getConstructPhaseEstimate(p);
			}
			constructionEstimates[ip] *= getCompositionFactor(p);
			constructionEstimates[ip] += constructionOverheads[ip];
		}
	}
}
