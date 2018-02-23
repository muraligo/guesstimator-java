package m3.guesstimator.model.functional;

import java.util.List;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.ParseablePrimaryCollection;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.reference.NfrCategory;
import m3.guesstimator.model.reference.VerifyCategory;

public class M3Service extends AbstractContainingArtifact {
    private static final long serialVersionUID = 1L;

    protected String strNfrBuildFactors;
    protected String strVerifications;
    // fields below are non-persistent
    transient protected ParseablePrimaryCollection<NfrCategory, Long> nfrBuildFactors;
    transient protected ParseablePrimaryCollection<VerifyCategory, Long> verifications;

    @Override
    public List<SolutionArtifact> getConstituents() {
        return constituents;
    }

    public String getStrNfrBuildFactors() {
        return strNfrBuildFactors;
    }
    public void setStrNfrBuildFactors(String value) {
        strNfrBuildFactors = value;
        if (nfrBuildFactors == null) {
            nfrBuildFactors = new ParseablePrimaryCollection<NfrCategory, Long>(getClass().getSimpleName(), 
                    "NfrBuildFactors", NfrCategory.class, Long.class, 0L);
        }
        nfrBuildFactors.setStrCollection(value);
        constructEstimateComputed = false;
    }

    public String getStrVerifications() {
        return strVerifications;
    }
    public void setStrVerifications(String value) {
        strVerifications = value;
        if (verifications == null) {
            verifications = new ParseablePrimaryCollection<VerifyCategory, Long>(getClass().getSimpleName(), 
                    "VerificationFactors", VerifyCategory.class, Long.class, 0L);
        }
        verifications.setStrCollection(value);
        constructEstimateComputed = false;
    }

    protected void computeNfrBuildEstimate() {
        for (NfrCategory p : NfrCategory.values()) {
            int ip = p.ordinal();
            nfrBuildEstimates[ip] = nfrBuildFactors.get(ip);
        }
    }

    protected void computeVerifyEstimate() {
        for (VerifyCategory p : VerifyCategory.values()) {
            int ip = p.ordinal();
            verificationEstimates[ip] = verifications.get(ip);
        }
    }
}
