package m3.guesstimator.model.functional;

import java.util.List;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.SolutionArtifact;

public class M3Package extends AbstractContainingArtifact {
    private static final long serialVersionUID = 1L;

	private ContainingSolutionArtifact parent;

	@Override
	public List<SolutionArtifact> getConstituents() {
		return constituents;
	}

    /**
     * Parent could be a Subsystem or a Service but not both.
     * 
     * @return
    */
	public ContainingSolutionArtifact getParent() {
		return parent;
	}
	public void setParent(ContainingSolutionArtifact parent) {
		this.parent = parent;
	}

	public boolean isIndependent() {
		return getParent() == null;
	}

    @Override
    protected boolean hasConstructOverheads() {
        return false;
	}

    @Override
    protected void computeNfrBuildEstimate() {
        // Empty implementation as no action required here
    }

    @Override
    protected void computeVerifyEstimate() {
        // Empty implementation as no action required here
    }
}
