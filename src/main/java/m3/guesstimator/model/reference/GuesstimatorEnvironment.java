package m3.guesstimator.model.reference;

public enum GuesstimatorEnvironment {
    GENERAL("General"), 
    DEVELOPMENT("Dev"),
    QA("QA"), 
    INTEGRATION("Integ", "Perf"), 
    PREPROD("Preprod", "Stage"), 
    PRODUCTION("Prod");

    private final String[] _names;

    private GuesstimatorEnvironment(String...strings) {
       _names = strings;
    }

    public String[] names() {
        return _names;
    }

    public static GuesstimatorEnvironment named(String nm) {
        for (GuesstimatorEnvironment val : GuesstimatorEnvironment.values()) {
            for (String n2 : val._names) {
                if (n2.equals(nm)) {
                    return val;
                }
            }
        }
        return null;
    }
}
