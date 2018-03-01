package m3.guesstimator.model.functional;

import java.io.Serializable;

import m3.guesstimator.model.reference.M3MessageSubtype;
import m3.guesstimator.model.reference.M3MessageType;

public class M3InternalMessage implements Serializable, Comparable<M3InternalMessage>, Cloneable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String version;
    private M3MessageType type;
    private M3MessageSubtype subtype;
    private String datatypname;
    private String datatype; // xsd content
    private Long datatypfactor;

	public String getName() {
		return name;
	}
	public void setName(String value) {
		name = value;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String value) {
		description = value;
	}

    public String getVersion() {
        return version;
    }
    public void setVersion(String value) {
        version = value;
    }

    @Override
    public int compareTo(M3InternalMessage o) {
        int res = getName().compareTo(o.getName());
        return (res == 0) ? getVersion().compareTo(o.getVersion()) : res;
    }

    @Override
    public int hashCode() {
        return getName().hashCode() * 31 + getVersion().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo((M3InternalMessage)obj) == 0;
    }

	public M3MessageType getType() {
		return type;
	}
	public void setType(M3MessageType value) {
		type = value;
	}

	public M3MessageSubtype getSubtype() {
		return subtype;
	}
	public void setSubtype(M3MessageSubtype value) {
	    subtype = value;
	}

	public String getDatatypname() {
		return datatypname;
	}
	public void setDatatypname(String value) {
		datatypname = value;
	}

	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String value) {
		datatype = value;
	}

	public Long getDatatypfactor() {
		return datatypfactor;
	}
	public void setDatatypfactor(Long value) {
		datatypfactor = value;
	}

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Message#" + name + "{");
        sb.append(type);
        sb.append(".");
        sb.append(subtype);
        sb.append(", ");
        sb.append(datatypname);
        sb.append("-");
        sb.append(datatypfactor);
        sb.append("}");
        return sb.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

}
