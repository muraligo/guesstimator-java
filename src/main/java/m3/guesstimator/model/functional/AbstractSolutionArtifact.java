package m3.guesstimator.model.functional;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import m3.guesstimator.model.SolutionArtifact;

public abstract class AbstractSolutionArtifact implements SolutionArtifact {
    private static final long serialVersionUID = 1L;

    protected static final Map<Class<?>, String> _classTableMap = Collections.synchronizedMap(
            new ConcurrentHashMap<Class<?>, String>(20));

    protected String name;
    protected String description;
    protected String version;

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String value) {
        name = value;
    }

    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public void setDescription(String value) {
        description = value;
    }

    @Override
    public String getVersion() {
        return version;
    }
    @Override
    public void setVersion(String value) {
        version = value;
    }

    @Override
    public int compareTo(SolutionArtifact o) {
        int res = getName().compareTo(o.getName());
        return (res == 0) ? getVersion().compareTo(o.getVersion()) : res;
    }

    @Override
    public int hashCode() {
        return getName().hashCode() * 31 + getVersion().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo((SolutionArtifact)obj) == 0;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(".");
        sb.append(version);
        return sb.toString();
    }
}
