package m3.guesstimator.model;

public class M3JsonException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final String model;
	private final String jsonField;

	public M3JsonException(String model, String fld, String msg, Throwable cause) {
		super(msg, cause);
		this.model = model;
		this.jsonField = fld;
	}

	public String getModel() {
		return model;
	}

	public String getJsonField() {
		return jsonField;
	}
}
