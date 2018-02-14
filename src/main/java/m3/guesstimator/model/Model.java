package m3.guesstimator.model;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type collectionType = new TypeToken<List<PhaseValue>>(){}.getType();
		List<PhaseValue> l = null;
		try {
            l = gson.fromJson(s, collectionType);
		} catch (JsonSyntaxException e) {
			l = null;
			throw new M3ModelException(getClass().getSimpleName(), fld, s, "parsing Json from", e);
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
