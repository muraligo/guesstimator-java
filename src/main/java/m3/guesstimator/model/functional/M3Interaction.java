package m3.guesstimator.model.functional;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.ParseablePrimaryCollection;
import m3.guesstimator.model.reference.ConstructionPhase;
import m3.guesstimator.model.reference.M3MessageType;
import m3.guesstimator.model.reference.VerifyCategory;

public class M3Interaction extends AbstractSolutionArtifact {
    private static final long serialVersionUID = 1L;

	private ContainingSolutionArtifact source;
	private ContainingSolutionArtifact target;
	private M3InternalMessage message;
	private String api;
	private String strConstructCosts;
    protected String strVerifications;
	// fields below are non-persistent
	transient private Long[] constructionEstimates;
	transient private boolean constructEstimateComputed = false;
    transient private Long[] verificationEstimates;
    transient protected boolean verifyEstimateComputed = false;
    transient ParseablePrimaryCollection<ConstructionPhase, Long> constructCosts;
    transient private ParseablePrimaryCollection<VerifyCategory, Long> verifications;

    public ContainingSolutionArtifact getSource() {
        return source;
	}
    public void setSource(ContainingSolutionArtifact value) {
        source = value;
    }

    public ContainingSolutionArtifact getTarget() {
        return target;
	}
    public void setTarget(ContainingSolutionArtifact value) {
        target = value;
    }

    public M3InternalMessage getMessage() {
        return message;
    }
    public void setMessage(M3InternalMessage value) {
        message = value;
        constructEstimateComputed = false;
    }

    public String getApi() {
        return api;
    }
    public void setApi(String value) {
        api = value;
    }

    public String getStrConstructCosts() {
        return strConstructCosts;
    }
    public void setStrConstructCosts(String value) {
        strConstructCosts = value;
        constructCosts.setStrCollection(value);
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
        verifyEstimateComputed = false;
    }

    @Override
    public Long getConstructPhaseEstimate(ConstructionPhase phase) {
        if (! constructEstimateComputed) {
            computeConstructEstimate();
            constructEstimateComputed = true;
        }
        return constructionEstimates[phase.ordinal()];
    }

    public Long getVerificationEstimate() {
        if (! verifyEstimateComputed) {
            computeVerifyEstimate();
            verifyEstimateComputed = true;
        }
        Long verifyEstimate = 0L;
        for (VerifyCategory p : VerifyCategory.values()) {
        	verifyEstimate += verificationEstimates[p.ordinal()];
        }
        return verifyEstimate;
    }

	@Override
    public Long getEstimate() {
        if (! constructEstimateComputed) {
            computeConstructEstimate();
            constructEstimateComputed = true;
        }
        Long constructEstimate = 0L;
        for (ConstructionPhase p : ConstructionPhase.values()) {
            constructEstimate += constructionEstimates[p.ordinal()];
        }
        return constructEstimate + getVerificationEstimate();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Component#");
        sb.append(super.toString());
        sb.append("{source:");
        sb.append(source.getName());
        sb.append("<");
        sb.append(source.getClass().getSimpleName());
        sb.append(">");
        sb.append(", target:");
        sb.append(target.getName());
        sb.append("<");
        sb.append(target.getClass().getSimpleName());
        sb.append(">");
        sb.append(", ");
        sb.append(message);
        sb.append(", api:");
        sb.append(api);
        sb.append("}");
        return sb.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    private Long getConstructCost(ConstructionPhase phase) {
        return constructCosts.get(phase.ordinal());
    }

    private void computeConstructEstimate() {
        for (ConstructionPhase p : ConstructionPhase.values()) {
            long estimate = getConstructCost(p);
            M3InternalMessage msg = getMessage();
            M3MessageType msg_type = msg.getType();
            estimate *= msg_type.factor();
            estimate *= (msg_type.hasSubtype() ? msg.getSubtype().factor() : 1);
            estimate *= msg.getDatatype().factor();
            constructionEstimates[p.ordinal()] = estimate;
        }
    }

    private void computeVerifyEstimate() {
        for (VerifyCategory p : VerifyCategory.values()) {
            int ip = p.ordinal();
            verificationEstimates[ip] = verifications.get(ip);
        }
    }
}
