package m3.guesstimator.model;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import m3.guesstimator.model.reference.ConstructionPhase;

public interface Model {

	String getName();
	void setName(String value);
	String getDescription();
	void setDescription(String value);
	String getVersion();
	void setVersion(String value);
	List<Model> getConstituents();
	void setConstituents(List<Model> value);
	Long getConstructPhaseEstimate(ConstructionPhase phase);
	Long getEstimate();

	default void parseConstructPhaseValues(String fld, String s, Long[] arry) {
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

	public class PhaseValue {
		private String phase;
		private Long value;

		public String getPhase() {
			return phase;
		}
		public void setPhase(String phase) {
			this.phase = phase;
		}

		public Long getValue() {
			return value;
		}
		public void setValue(Long value) {
			this.value = value;
		}

		public ConstructionPhase getPhaseEnum() {
			return ConstructionPhase.valueOf(getPhase());
		}
	}
}
