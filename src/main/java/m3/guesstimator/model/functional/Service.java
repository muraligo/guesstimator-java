package m3.guesstimator.model.functional;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import m3.guesstimator.model.SolutionArtifact;

@Entity
@Table(name = "SERVICE")
public class Service extends AbstractContainingArtifact {
    private static final long serialVersionUID = 1L;

    @OneToMany(cascade=CascadeType.ALL, targetEntity=m3.guesstimator.model.functional.Package.class)
    @JoinColumn(name="SERVICE_ID")
    @Override
    public List<SolutionArtifact> getConstituents() {
        return constituents;
    }

    @Transient
    protected boolean hasConstructOverheads() {
        return true;
	}
}
