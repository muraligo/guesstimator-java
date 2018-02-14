package m3.guesstimator.model.reference;

public enum GuesstimatorResourceType {
    PHYSICAL("Physical", null, null), 
    TOOLS("Tools", null, ComponentContext.DevOps), 
    SECURITY("Security", null, null), 
    PLATFORM("AppFramework", null, null), 
    DATA("Data", null, null), 
    SCHEMA("Schema", GuesstimatorEnvironment.GENERAL, null), 
    CODE("Code", GuesstimatorEnvironment.GENERAL, null);

    private final String _name;
    private final GuesstimatorEnvironment _env;
    private final ComponentContext _context;

    private GuesstimatorResourceType(String nm, GuesstimatorEnvironment env, ComponentContext ctx) {
       _name = nm;
       _env = env;
       _context = ctx;
    }

    public String named() {
        return _name;
    }

    public GuesstimatorEnvironment environment() {
        return _env;
    }

    public ComponentContext context() {
        return _context;
    }

    public static GuesstimatorResourceType named(String nm) {
        for (GuesstimatorResourceType val : GuesstimatorResourceType.values()) {
            if (val._name.equals(nm)) {
                return val;
            }
        }
        return null;
    }
}
