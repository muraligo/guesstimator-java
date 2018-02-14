package m3.guesstimator.model;

import java.util.ArrayList;
import java.util.List;

public class M3ModelFieldsException extends M3ModelException {
	private static final long serialVersionUID = 1L;

    private List<M3FieldException> _exceptions = new ArrayList<M3FieldException>();

	public M3ModelFieldsException(String model, String action, Throwable cause) {
		super(model, action, cause);
	}

	public M3ModelFieldsException(String model, String action) {
		super(model, action);
	}

    public M3FieldException[] getExceptions() {
        return _exceptions.toArray(new M3FieldException[_exceptions.size()]);
    }

    public M3FieldException addException(String fld, String action, Object value, Throwable cause) {
        M3FieldException ex = new M3FieldException(fld, action, value, cause);
        _exceptions.add(ex);
        return ex;
    }

    public M3FieldException addException(String fld, String action, Object value) {
        M3FieldException ex = new M3FieldException(fld, action, value);
        _exceptions.add(ex);
        return ex;
    }

    public M3FieldException addException(String column, String action) {
        M3FieldException ex = new M3FieldException(column, action);
        _exceptions.add(ex);
        return ex;
    }

    public class M3FieldException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public M3FieldException(String fld, String action, Object value, Throwable cause) {
    	    super("Exception " + action + " value [" + value + "] for [" + fld + "] in " + model, cause);
        }

        public M3FieldException(String fld, String action, Object value) {
    	    super("Exception " + action + " value [" + value + "] for [" + fld + "] in " + model);
        }

        public M3FieldException(String fld, String action) {
    	    super("Exception " + action + " [" + fld + "] for " + model);
        }
    }

}
