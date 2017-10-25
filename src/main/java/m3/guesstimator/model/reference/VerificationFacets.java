package m3.guesstimator.model.reference;

public enum VerificationFacets {
    FUNCTIONAL("func"),
    SECURITY("sec"),
    PERFORMANCE("perf");

    final String _shortName;

	private VerificationFacets(String shortName) {
		_shortName = shortName;
	}

	public String getShortName() {
		return _shortName;
	}
}
