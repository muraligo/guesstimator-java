package m3.guesstimator.model.reference;

public enum M3MessageActionSubtype implements M3MessageSubtype {
    QUERY(1), 
    UPDATE(2),
    REMOVE(3)
    ;

    private final long _factor;

    private M3MessageActionSubtype(long fact) {
        _factor = fact;
    }

    @Override
    public long factor() {
		return _factor;
    }

}
