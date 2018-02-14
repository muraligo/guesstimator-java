package m3.guesstimator.model;

public class M3ModelException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected final String model;
    private final String jsonField;

	public M3ModelException(String model, String fld, String s, String action, Throwable cause) {
		super("Exception " + action + " DB string value [" + s + "] for " + model, cause);
		this.model = model;
		this.jsonField = fld;
	}

    public M3ModelException(String model, String fld, String s, String action) {
        super("Exception " + action + " DB string value [" + s + "] for " + model);
        this.model = model;
        this.jsonField = fld;
    }

    public M3ModelException(String model, String action, Throwable cause) {
        super("Exception " + action + " for " + model, cause);
        this.model = model;
        this.jsonField = null;
    }

    public M3ModelException(String model, String action) {
        super("Exception " + action + " for " + model);
        this.model = model;
        this.jsonField = null;
    }

	public String getModel() {
		return model;
	}

	public String getJsonField() {
		return jsonField;
	}
}
