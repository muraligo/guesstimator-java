package m3.guesstimator.model.functional;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import m3.guesstimator.model.SolutionArtifact;

@MappedSuperclass
public abstract class AbstractSolutionArtifact implements SolutionArtifact {
    private static final long serialVersionUID = 1L;

    protected String name;
    protected String description;
    protected String version;

    @Id
    @Column(name = "NAME", nullable = false, length = 32)
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String value) {
        name = value;
    }

    @Column(name = "DESCRIPTION", length = 1024)
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public void setDescription(String value) {
        description = value;
    }

    @Column(name = "VERSION", length = 8)
    @Override
    public String getVersion() {
        return version;
    }
    @Override
    public void setVersion(String value) {
        version = value;
    }

    @Transient
    @Override
    public int compareTo(SolutionArtifact o) {
        return getName().compareTo(o.getName());
    }

}
