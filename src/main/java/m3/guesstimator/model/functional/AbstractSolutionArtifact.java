package m3.guesstimator.model.functional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.reference.BasicCriterionOperator;

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
        return getName().compareTo(o.getName());
    }

/*
    public void buildFieldValueMap(Map<String, String> result) {
        if (_fieldsMap.isEmpty()) {
            discoverFields();
        }
        for (String fldName : _fieldsMap.keySet()) {
            FieldMappingSpec spec = _fieldsMap.get(fldName);
            String value = null;
            if ("name".equals(fldName))
                value = "'" + getName() + "'";
            else if ("description".equals(fldName))
                value = "'" + getDescription() + "'";
            else if ("version".equals(fldName))
                value = "'" + getVersion() + "'";
            else
                value = getOtherFieldValue(fldName);
            result.put(spec.columnName, value);
        }
	}

    public String buildColumnList() {
        if (_fieldsMap.isEmpty()) {
            discoverFields();
        }
        StringBuilder sb = new StringBuilder();
        boolean notfirst = false;
        for (String fldName : _fieldsMap.keySet()) {
            FieldMappingSpec spec = _fieldsMap.get(fldName);
            if (spec.columnName == null)
                continue;
            if (notfirst)
                sb.append(", ");
            else
                notfirst = true;
            sb.append(spec.columnName);
        }
        return sb.toString();
    }

    public String getWhereConditionFor(String fldName, BasicCriterionOperator op, Object value) {
        if (_fieldsMap.isEmpty()) {
            discoverFields();
        }
        FieldMappingSpec spec = _fieldsMap.get(fldName);
        if (spec.fieldRefersTo == FieldReferenceKind.IdForType || spec.fieldRefersTo == FieldReferenceKind.ForeignTable) {
            return null;
        }
        StringBuilder sb = new StringBuilder(spec.columnName);
        sb.append(" ");
        switch (op) {
        case EQ:
        	sb.append("=");
            break;
        case NE:
    	    sb.append("<>");
            break;
        case GT:
    	    sb.append(">");
            break;
        case GE:
    	    sb.append(">=");
            break;
        case LE:
    	    sb.append("<=");
            break;
        case LT:
    	    sb.append("<");
            break;
        default:
            break;
        }
        sb.append(" ");
        switch (spec.fieldRefersTo) {
        case EnumType:
            sb.append("'");
            sb.append(value.toString());
            sb.append("'");
            break;
        case IdForType: // should not happen
            break;
        case ForeignTable: // should not happen
            break;
        default: // NULL
            if (spec.fieldClass == String.class) {
                sb.append("'");
                sb.append(value.toString());
                sb.append("'");
            } else if (spec.fieldClass.isPrimitive()) {
                sb.append(value.toString());
            }
            break;
        }
        return sb.toString();
    }

    abstract protected String getOtherFieldValue(String fldName);

    protected void discoverFields() {
        for (Field field : getClass().getDeclaredFields()) {
            String fldName = field.getName();
            FieldMappingSpec spec = null;
            if ("name".equals(fldName) || "description".equals(fldName) || "version".equals(fldName)) {
                spec = new FieldMappingSpec();
                spec.fieldName = fldName;
                spec.columnName = fldName.toUpperCase();
                spec.fieldClass = String.class;
                _fieldsMap.put(fldName, spec);
                continue;
            }
            String getterName = "get" + fldName.substring(0, 1).toUpperCase() + fldName.substring(1);
            Method getterMth = null;
            try {
                getterMth = getClass().getDeclaredMethod(getterName, new Class<?>[0]);
            } catch (NoSuchMethodException | SecurityException e) {
                // ignore as field may not have a method
            }
            if (getterMth != null && getterMth.isAnnotationPresent(javax.persistence.Column.class)) {
                javax.persistence.Column ann = field.getAnnotation(javax.persistence.Column.class);
                spec = new FieldMappingSpec();
                spec.fieldName = fldName;
                spec.columnName = ann.name();
                spec.fieldClass = field.getType();
                if (! field.getType().isPrimitive()) {
                    if (getterMth.isAnnotationPresent(javax.persistence.Enumerated.class)) {
                        spec.fieldRefersTo = FieldReferenceKind.EnumType;
                    }
                }
            } else if (getterMth != null && 
                    getterMth.isAnnotationPresent(javax.persistence.ManyToOne.class) && 
                    getterMth.isAnnotationPresent(javax.persistence.JoinColumn.class)) {
                javax.persistence.JoinColumn ann = field.getAnnotation(javax.persistence.JoinColumn.class);
                spec = new FieldMappingSpec();
                spec.fieldName = fldName;
                spec.columnName = ann.name();
                spec.fieldClass = field.getType();
                spec.fieldRefersTo = FieldReferenceKind.IdForType;
                javax.persistence.ManyToOne mann = field.getAnnotation(javax.persistence.ManyToOne.class);
                spec.targetClass = mann.targetEntity();
                spec.targetTableName = getTableForClass(spec.targetClass);
            } else if (getterMth != null && 
                    getterMth.isAnnotationPresent(javax.persistence.OneToMany.class)) {
                javax.persistence.OneToMany mann = field.getAnnotation(javax.persistence.OneToMany.class);
                spec = new FieldMappingSpec();
                spec.fieldName = fldName;
                spec.columnName = null;
                spec.fieldClass = field.getType();
                spec.fieldRefersTo = FieldReferenceKind.ForeignTable;
                spec.targetClass = mann.targetEntity();
                spec.targetTableName = getTableForClass(spec.targetClass);
                spec.targetField = mann.mappedBy();
            }
            if (spec != null) {
                _fieldsMap.put(fldName, spec);
            }
        }
    }

    synchronized static protected String getTableForClass(Class<?> targetClass) {
        if (! _classTableMap.containsKey(targetClass)) {
            javax.persistence.Table tann = targetClass.getAnnotation(javax.persistence.Table.class);
            if (tann != null) {
                _classTableMap.putIfAbsent(targetClass, tann.name());
            }
        }
        return _classTableMap.get(targetClass);
    }
*/

    abstract public SolutionArtifact createNew();
}
