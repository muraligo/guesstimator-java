package m3.guesstimator.model.reference;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import m3.guesstimator.model.M3JsonException;
import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.Model.PhaseValue;

@Entity
@Table(name = "component_type")
public class ComponentType {
	private String name;
	private String description;
	private ComponentContext context;
	private Layer architecturalLayer;
	private String strConstructCosts;
	// fields below are non-persistent
	private Long[] costs;
	LocalDateTime constructCostParsedAt;
	LocalDateTime constructCostUpdatedAt;

	public ComponentType() {
		costs = new Long[ConstructionPhase.values().length];
		Arrays.fill(costs, 0L);
		LocalDateTime currTime = LocalDateTime.now();
		constructCostParsedAt = null;
		constructCostUpdatedAt = currTime;
	}

    @Id
	@Column(name = "name", nullable = false, length = 32)
	public String getName() {
		return name;
	}
	public void setName(String value) {
		name = value;
	}

	@Column(name = "description", length = 1024)
	public String getDescription() {
		return description;
	}
	public void setDescription(String value) {
		description = value;
	}

	@Column(name = "costs", nullable = false)
	public String getStrConstructCosts() {
		return strConstructCosts;
	}
	public void setStrConstructCosts(String value) {
		strConstructCosts = value;
		constructCostUpdatedAt = LocalDateTime.now();
	}

    @Column(name = "context")
    @Enumerated(EnumType.STRING)
	public ComponentContext getContext() {
		return context;
	}
	public void setContext(ComponentContext context) {
		this.context = context;
	}

    @Column(name = "layer")
    @Enumerated(EnumType.STRING)
	public Layer getArchitecturalLayer() {
		return architecturalLayer;
	}
	public void setArchitecturalLayer(Layer architecturalLayer) {
		this.architecturalLayer = architecturalLayer;
	}

	@Transient
	public Long getConstructCost(ConstructionPhase phase) {
		if (constructCostParsedAt == null || constructCostUpdatedAt.isAfter(constructCostParsedAt)) {
			try {
				parseConstructCosts("ConstructCosts", strConstructCosts, costs);
				constructCostParsedAt = LocalDateTime.now();
			} catch (M3JsonException|M3ModelException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return costs[phase.ordinal()];
	}

	private void parseConstructCosts(String fld, String s, Long[] arry) {
		ObjectMapper mapper = new ObjectMapper();
		List<PhaseValue> l = null;
		try {
			l = mapper.readValue(s, new TypeReference<List<PhaseValue>>(){});
		} catch (JsonMappingException|JsonParseException e) {
			l = null;
			throw new M3ModelException(getClass().getSimpleName(), fld, s, "parsing Json from", e);
		} catch (IOException ioe) {
			l = null;
			throw new M3ModelException(getClass().getSimpleName(), fld, s, "reading Json from", ioe);
		}
		try {
			l.forEach(pv -> arry[pv.getPhaseEnum().ordinal()] = pv.getValue());
		} catch (IllegalArgumentException iae) {
			throw new M3ModelException(getClass().getSimpleName(), fld, s, "matching enum with phase in", iae);
		}
	}
}
