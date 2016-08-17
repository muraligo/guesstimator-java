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
import m3.guesstimator.model.reference.ApplicationType;
import m3.guesstimator.model.reference.ConstructionPhase;

@Entity
@Table(name = "application")
public class Application implements Model {
	private String name;
	private String description;
	private String version;
	private List<Model> constituents;
	private String strCompositionFactors;
	private ApplicationType applicationType;
	private Long initiationEstimate;
	private Long deploymentEstimate;
	private String strConstructionOverheads;
	private List<Service> services;
	// fields below are non-persistent
	private Long[] compositionFactors;
	private LocalDateTime compFactorParsedAt;
	private LocalDateTime compFactorUpdatedAt;
	private Long functionalEstimate;
	private Long performanceEstimate;
	private Long securityEstimate;
	private Long[] constructionEstimates;
	private Long[] constructionOverheads;
	private LocalDateTime constructOverheadParsedAt;
	private LocalDateTime constructOverheadUpdatedAt;
	private boolean constructEstimateComputed = false;

	public Application() {
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

	@OneToMany(cascade=CascadeType.ALL, targetEntity=m3.guesstimator.model.functional.Subsystem.class, mappedBy="parent")
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

	@Column(name = "application_type", nullable = false)
    @Enumerated(EnumType.STRING)
	public ApplicationType getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(ApplicationType value) {
		applicationType = value;
	}

	@Column(name = "initiation", nullable = false)
	public Long getInitiationEstimate() {
		return initiationEstimate;
	}
	public void setInitiationEstimate(Long value) {
		initiationEstimate = value;
	}

	@Column(name = "deployment", nullable = false)
	public Long getDeploymentEstimate() {
		return deploymentEstimate;
	}
	public void setDeploymentEstimate(Long value) {
		deploymentEstimate = value;
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

	@OneToMany(cascade=CascadeType.ALL, targetEntity=m3.guesstimator.model.functional.Service.class)
	public List<Service> getServices() {
		return services;
	}
	public void setServices(List<Service> value) {
		services = value;
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

	@Transient
	@Override
	public Long getEstimate() {
		Long estimate = 0L;
		prepareEstimateInputs();

		Long constructionEstimate = 0L;
		for (ConstructionPhase p : ConstructionPhase.values()) {
			constructionEstimate += getConstructPhaseEstimate(p);
		}
		constructionEstimate *= getApplicationType().getMultiplier();

		// Testing estimates
		functionalEstimate = 0L;
		for (Model m : getConstituents()) {
			if (m instanceof Subsystem) {
				Subsystem sys = (Subsystem) m;
				functionalEstimate += sys.getFunctionalEstimate();
			}
		}
		for (Service svc : getServices()) {
			functionalEstimate += svc.getFunctionalEstimate();
		}
		functionalEstimate *= getApplicationType().getMultiplier();
		performanceEstimate = 0L;
		for (Model m : getConstituents()) {
			if (m instanceof Subsystem) {
				Subsystem sys = (Subsystem) m;
				performanceEstimate += sys.getPerformanceEstimate();
			}
		}
		for (Service svc : getServices()) {
			performanceEstimate += svc.getPerformanceEstimate();
		}
		performanceEstimate *= getApplicationType().getMultiplier();
		securityEstimate = 0L;
		for (Model m : getConstituents()) {
			if (m instanceof Subsystem) {
				Subsystem sys = (Subsystem) m;
				securityEstimate += sys.getSecurityEstimate();
			}
		}
		for (Service svc : getServices()) {
			securityEstimate += svc.getSecurityEstimate();
		}
		securityEstimate *= getApplicationType().getMultiplier();

		estimate = getInitiationEstimate() + constructionEstimate + getVerificationEstimate() + getDeploymentEstimate();
		return estimate;
	}

	@Transient
	public Long getVerificationEstimate() {
		return functionalEstimate + performanceEstimate + securityEstimate;
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
			for (Service svc : getServices()) {
				constructionEstimates[ip] += svc.getConstructPhaseEstimate(p);
			}
			constructionEstimates[ip] *= getCompositionFactor(p);
			constructionEstimates[ip] += constructionOverheads[ip];
		}
	}
}
