package m3.guesstimator.model.reference;

import java.io.Serializable;
import java.time.LocalDateTime;

import m3.guesstimator.model.ParseablePrimaryCollection;
import m3.guesstimator.model.SolutionArtifact;

public class ComponentType implements Serializable, Comparable<ComponentType>, Cloneable {
    private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private String version;
	private ComponentContext context;
	private Layer architecturalLayer;
	private String strConstructCosts;
	// fields below are non-persistent
    transient ParseablePrimaryCollection<ConstructionPhase, Long> constructCosts;

    public ComponentType() {
        constructCosts = new ParseablePrimaryCollection<ConstructionPhase, Long>(getClass().getSimpleName(), 
                "ConstructCosts", ConstructionPhase.class, Long.class, 0L);
	}

    ComponentType(LocalDateTime currTime) {
        constructCosts = new ParseablePrimaryCollection<ConstructionPhase, Long>(getClass().getSimpleName(), 
                "ConstructCosts", ConstructionPhase.class, Long.class, 0L, currTime);
	}

	public String getName() {
		return name;
	}
	public void setName(String value) {
		name = value;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String value) {
		description = value;
	}

    public String getVersion() {
        return version;
    }
    public void setVersion(String value) {
        version = value;
    }

    public String getStrConstructCosts() {
        return strConstructCosts;
    }
    public void setStrConstructCosts(String value) {
        strConstructCosts = value;
        constructCosts.setStrCollection(value);
    }

	public ComponentContext getContext() {
		return context;
	}
	public void setContext(ComponentContext context) {
		this.context = context;
	}

	public Layer getArchitecturalLayer() {
		return architecturalLayer;
	}
	public void setArchitecturalLayer(Layer architecturalLayer) {
		this.architecturalLayer = architecturalLayer;
	}

	public Long getConstructCost(ConstructionPhase phase) {
		return constructCosts.get(phase.ordinal());
	}

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("ComponentType#" + name + "{");
        sb.append(context);
        sb.append(".");
        sb.append(architecturalLayer);
        sb.append(", ");
        sb.append(strConstructCosts);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int compareTo(ComponentType o) {
        return getName().compareTo(o.getName());
    }

    boolean isConstructParseTimeSameAs(LocalDateTime currTime) {
        return (!constructCosts.isParsedAfter(currTime) && !constructCosts.isParsedBefore(currTime));
    }

    boolean isConstructUpdateTimeSameAs(LocalDateTime currTime) {
        return (!constructCosts.isUpdatedAfter(currTime) && !constructCosts.isUpdatedBefore(currTime));
    }
}
