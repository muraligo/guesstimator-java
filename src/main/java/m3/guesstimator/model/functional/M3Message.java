package m3.guesstimator.model.functional;

import m3.guesstimator.model.reference.M3Encoding;

public class M3Message extends M3InternalMessage {
    private static final long serialVersionUID = 1L;

    private M3Encoding encoding;

	public M3Encoding getEncoding() {
		return encoding;
	}
	public void setEncoding(M3Encoding value) {
		encoding = value;
	}

    @Override
    public String toString() {
        String parentstr = super.toString();
        StringBuffer sb = new StringBuffer(parentstr.substring(0, (parentstr.length() - 1)));
        sb.append(", ");
        sb.append(encoding);
        sb.append("}");
        return sb.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

}
