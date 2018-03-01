package m3.guesstimator.model.functional;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.ParseablePrimaryCollection;
import m3.guesstimator.model.reference.ConstructionPhase;
import m3.guesstimator.model.reference.M3MessageSubtype;
import m3.guesstimator.model.reference.M3MessageType;
import m3.guesstimator.model.reference.VerifyCategory;

public class M3Interaction extends AbstractSolutionArtifact {
    private static final long serialVersionUID = 1L;

	private ContainingSolutionArtifact source;
	private ContainingSolutionArtifact target;
	private String fromKind;
	private String toKind;
	private String api;
    private M3MessageType messagetype;
    private M3MessageSubtype messagesubtype;
    private String msgdatatypname;
    private Long msgdatatypfactor;
	private String strConstructCosts;
    private String strVerifications;
    private String msgdatatype; // xsd content
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

    public String getFromKind() {
        return fromKind;
    }
    public void setFromKind(String value) {
        fromKind = value;
    }

    public String getToKind() {
        return toKind;
    }
    public void setToKind(String value) {
        toKind = value;
    }

    public String getApi() {
        return api;
    }
    public void setApi(String value) {
        api = value;
    }

	public M3MessageType getMessageType() {
		return messagetype;
	}
	public void setMessageType(M3MessageType value) {
		messagetype = value;
	}

	public M3MessageSubtype getMessageSubtype() {
		return messagesubtype;
	}
	public void setMessageSubtype(M3MessageSubtype value) {
	    messagesubtype = value;
	}

	public String getMessageDatatypname() {
		return msgdatatypname;
	}
	public void setMessageDatatypname(String value) {
		msgdatatypname = value;
	}

	public Long getMessageDatatypfactor() {
		return msgdatatypfactor;
	}
	public void setMessageDatatypfactor(Long value) {
		msgdatatypfactor = value;
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

	public String getMessageDatatype() {
		return msgdatatype;
	}
	public void setMessageDatatype(String value) {
		msgdatatype = value;
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
        StringBuffer sb = new StringBuffer("Interaction#");
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
        sb.append(", api:");
        sb.append(api);
        sb.append(", ");
        sb.append(messagetype);
        sb.append(".");
        if (messagesubtype != null) {
            sb.append(messagesubtype);
        }
        sb.append(".");
        sb.append(msgdatatypname);
        sb.append("-");
        sb.append(msgdatatypfactor);
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
            M3MessageType msg_type = getMessageType();
            estimate *= msg_type.factor();
            estimate *= (msg_type.hasSubtype() ? getMessageSubtype().factor() : 1);
            estimate *= getMessageDatatypfactor();
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
