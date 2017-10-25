package m3.guesstimator.model.functional;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.SolutionArtifact;

@Entity
@Table(name = "SUBSYSTEM")
public class Subsystem extends AbstractContainingArtifact {
    private static final long serialVersionUID = 1L;

	private ContainingSolutionArtifact parent;

	@OneToMany(cascade=CascadeType.ALL, targetEntity=m3.guesstimator.model.functional.Package.class, mappedBy="parent")
	@Override
	public List<SolutionArtifact> getConstituents() {
		return constituents;
	}

	@ManyToOne(targetEntity=m3.guesstimator.model.functional.Application.class)
	@JoinColumn(name="PARENT", nullable=false)
	public ContainingSolutionArtifact getParent() {
		return parent;
	}
	public void setParent(ContainingSolutionArtifact parent) {
		this.parent = parent;
	}

    @Transient
    protected boolean hasConstructOverheads() {
        return true;
	}
}
