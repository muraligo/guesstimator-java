package m3.guesstimator.model.reference;

public enum M3MessageType {
    ACTION(1, true), 
    INFO(1, false), 
    ERROR(2, false), 
    DATA(1, false);

    private final long _factor;
    private final boolean _has_subtype;

    private M3MessageType(long fact, boolean subtype) {
        _factor = fact;
        _has_subtype = subtype;
    }

    public long factor() {
        return _factor;
    }

    public boolean hasSubtype() {
        return _has_subtype;
    }
}
