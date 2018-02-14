package m3.guesstimator.internal.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.M3ModelFieldsException.M3FieldException;
import m3.guesstimator.model.functional.AbstractSolutionArtifact;
import m3.guesstimator.model.functional.Component;
import m3.guesstimator.model.reference.BasicCriterionOperator;
import m3.guesstimator.model.reference.Complexity;
import m3.guesstimator.model.reference.ComponentType;
import m3.guesstimator.model.reference.Language;
import m3.guesstimator.model.reference.Layer;

public class EstimatorComponentDao extends AbstractDao {
    private static final Class<?> ARTIFACT_TYPE = Component.class;
    private static final Class<?> CONTAINING_ARTIFACT_TYPE = ContainingSolutionArtifact.class;
    private static final String _TABLE_NAME = "COMPONENT";
    private static final String _TYPE_COLUMN = "COMPONENT_TYPE";
    private static final String _COMPLEXITY_COLUMN = "COMPLEXITY";
    private static final String _LAYER_COLUMN = "LAYER";
    private static final String _LANGUAGE_COLUMN = "LANGUAGE";
    private static final String _COUNT_COLUMN = "COUNT";

    public EstimatorComponentDao() {
        super();
        populateFieldSpecs();
    }

    @Override
    protected void ensureObjectOfType(Object o) {
        if (!ARTIFACT_TYPE.isAssignableFrom(o.getClass()) || CONTAINING_ARTIFACT_TYPE.isAssignableFrom(o.getClass())) {
            throw new IllegalArgumentException("Entity must be of type " + ARTIFACT_TYPE.getSimpleName() + " and must not be a " + CONTAINING_ARTIFACT_TYPE.getSimpleName() + "!!!");
        }
    }

    @Override
    protected Class<?> getTypeOfObject(Object o) {
        return ARTIFACT_TYPE;
    }

    @Override
    protected String getNameOfObject(Object o) {
        SolutionArtifact sa = (SolutionArtifact)o;
        return sa.getName();
    }

    public SolutionArtifact put(SolutionArtifact sa) {
        EntityState es = getState(sa);
        SolutionArtifact resVal = null;
        if (es.isNew()) {
            resVal = addNew(sa);
        } else {
            resVal = update(sa);
        }
        es.unlockAndReset();
        return resVal;
    }

	public SolutionArtifact get(SolutionArtifact dummy, String name) {
        AbstractSolutionArtifact adum = (AbstractSolutionArtifact)dummy;
        StringBuilder colsb = new StringBuilder();
        buildExtendedColumnList(colsb);
        StringBuilder sqlsb = new StringBuilder("SELECT ");
        sqlsb.append(colsb.toString());
        sqlsb.append(" FROM ");
	    sqlsb.append(_TABLE_NAME + " cp ");
	    sqlsb.append(EstimatorComponentTypeDao._TABLE_NAME + " ct");
	    sqlsb.append(" WHERE cp.NAME = ");
        sqlsb.append(name);
        sqlsb.append(" AND cp." + _TYPE_COLUMN + " = ct." + _NAME_COLUMN);
        List<SolutionArtifact> results = new ArrayList<SolutionArtifact>();
        Function<M3DaoHandlerParam, M3DaoHandlerResult> qfunc = (param) ->
        {
            M3ModelFieldsException mfex = new M3ModelFieldsException(dummy.getClass().getSimpleName(), "collecting SQL SELECT exceptions");
	        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
            try {
    		    if (param.rs.first()) {
    		        ResultSetMetaData rsmd = param.rs.getMetaData();
    		        int colCnt = rsmd.getColumnCount();
    			    // TODO Drop the helper approach below and use the approach used for ComponentType instead
    		        AbstractArtifactHelper helper = getHelperForType(adum.getClass());
    		        List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
    		        SolutionArtifact sa1 = adum.createNew();
    		        for (int colIx = 0; colIx < colCnt; colIx++) {
    		            sa1 = helper.retrieveValues(this, param.rs, sa1, rsmd.getColumnLabel(colIx), fieldsState, mfex);
    		        }
    		        // TODO Do something with fieldsState
    		        result.add(fieldsState);
    		        result.add(sa1);
    		    }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
			return result;
        };
        performQuery(adum, sqlsb.toString(), results, qfunc);
        return results.get(0);
    }

    public List<SolutionArtifact> find(SolutionArtifact dummy, CriteriaBuilder cb) {
        AbstractSolutionArtifact adum = (AbstractSolutionArtifact)dummy;
        StringBuilder colsb = new StringBuilder();
        buildExtendedColumnList(colsb);
        StringBuilder sqlsb = new StringBuilder("SELECT ");
        sqlsb.append(colsb.toString());
        sqlsb.append(" FROM ");
	    sqlsb.append(_TABLE_NAME + " cp ");
	    sqlsb.append(EstimatorComponentTypeDao._TABLE_NAME + " ct");
	    // FIXME get the buildWhereClause part done right
        sqlsb.append(buildWhereClause(cb));
        sqlsb.append(" AND cp." + _TYPE_COLUMN + " = ct." + _NAME_COLUMN);
        List<SolutionArtifact> results = new ArrayList<SolutionArtifact>();
        Function<M3DaoHandlerParam, M3DaoHandlerResult> qfunc = (param) ->
        {
            M3ModelFieldsException mfex = new M3ModelFieldsException(dummy.getClass().getSimpleName(), "collecting SQL SELECT exceptions");
	        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
            try {
    		    if (param.rs.first()) {
    		        ResultSetMetaData rsmd = param.rs.getMetaData();
    		        int colCnt = rsmd.getColumnCount();
    			    // TODO Drop the helper approach below and use the approach used for ComponentType instead
    		        AbstractArtifactHelper helper = getHelperForType(adum.getClass());
    		        // TODO Implement
    		    }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
			return result;
        };
        performQuery(adum, sqlsb.toString(), results, qfunc);
        return results;
    }

    protected SolutionArtifact addNew(SolutionArtifact sa) {
        String saname = sa.getName();
        Component ct = (Component) sa;
        StringBuilder valuesb = new StringBuilder();
        StringBuilder colsb = new StringBuilder();
        buildBasicColumnList(colsb);
        valuesb.append("'" + saname + "'");
        valuesb.append(", '" + sa.getDescription() + "'");
        valuesb.append(", '" + sa.getVersion() + "'");
        valuesb.append(", '" + ct.getComplexity() + "'");
        valuesb.append(", '" + ct.getLayer() + "'");
        valuesb.append(", '" + ct.getLanguage() + "'");
        valuesb.append(", " + ct.getCount());
        valuesb.append(", '" + ct.getType().getName() + "'");
        String col_names = colsb.toString();
        StringBuilder sqlsb = new StringBuilder("INSERT INTO " + _TABLE_NAME + " (");
        sqlsb.append(col_names);
        sqlsb.append(") VALUES (");
        sqlsb.append(valuesb.toString());
        sqlsb.append(")");
        String ins_sql = sqlsb.toString();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int res_count = -1;
        M3ModelFieldsException mfex = new M3ModelFieldsException(ct.getClass().getSimpleName(), "collecting SQL INSERT exceptions");
        try {
            conn = getConnection(ct.getClass().getSimpleName());
            stmt = conn.createStatement();
            res_count = stmt.executeUpdate(ins_sql);
        } catch (SQLException ex) {
            // TODO Handle exception
        } finally {
            try {
                if (rs != null && !rs.isClosed())
                    rs.close();
                if (stmt != null && !stmt.isClosed())
                    stmt.close();
                if (conn != null && !conn.isClosed())
                    conn.close();
            } catch (SQLException sqle2) {
                // Ignore
            }
        }
		if (res_count > 0) { // row was inserted so we can go out and retrieve it
		    sqlsb.delete(0, sqlsb.length());
		    colsb.delete(0, colsb.length());
	        buildExtendedColumnList(colsb);
		    sqlsb.append("SELECT ");
		    sqlsb.append(colsb.toString());
		    sqlsb.append(" FROM ");
		    sqlsb.append(_TABLE_NAME + " cp ");
		    sqlsb.append(EstimatorComponentTypeDao._TABLE_NAME + " ct");
		    sqlsb.append(" WHERE cp.NAME = ");
		    sqlsb.append(saname);
	        sqlsb.append(" AND cp." + _TYPE_COLUMN + " = ct." + _NAME_COLUMN);
	        List<SolutionArtifact> results = new ArrayList<SolutionArtifact>();
	        Function<M3DaoHandlerParam, M3DaoHandlerResult> qfunc = (param) ->
	        {
	            M3ModelFieldsException mfex2 = new M3ModelFieldsException(ct.getClass().getSimpleName(), "collecting SQL SELECT exceptions");
		        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex2);
	            try {
	    		    if (param.rs.first()) {
	    		        ResultSetMetaData rsmd = param.rs.getMetaData();
	    		        int colCnt = rsmd.getColumnCount();
	    			    // TODO Drop the helper approach below and use the approach used for ComponentType instead
	    		        List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
	    		        for (int colIx = 0; colIx < colCnt; colIx++) {
	    		            checkAndRefresh(param.rs, ct, rsmd.getColumnLabel(colIx), fieldsState, mfex);
	    		        }
	    		        // TODO Do something with fieldsState
	    		        result.add(fieldsState);
	    		        result.add(ct);
	    		    }
	            } catch (SQLException ex) {
	                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
	            }
				return result;
	        };
	        performQuery(ct, sqlsb.toString(), results, qfunc);
		}
        // TODO Check and do something with mfex
        return sa;
    }

    private SolutionArtifact update(SolutionArtifact sa) {
	    // TODO Auto-generated method stub
	    return null;
    }

    @Override
    protected void populateFieldSpecs() {
        registerBaseFieldSpecs();
        registerFieldSpec("complexity", _COMPLEXITY_COLUMN, Complexity.class, FieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("layer", _LAYER_COLUMN, Layer.class, FieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("language", _LANGUAGE_COLUMN, Complexity.class, FieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("type", _TYPE_COLUMN, Layer.class, FieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("count", _COUNT_COLUMN, Long.class, null, null, null, null);
    }

    @Override
    protected void buildBasicColumnList(StringBuilder colsb) {
        colsb.append(_NAME_COLUMN);
        colsb.append(", " + _DESC_COLUMN);
        colsb.append(", " + _VERSION_COLUMN);
        colsb.append(", " + _COMPLEXITY_COLUMN);
        colsb.append(", " + _LAYER_COLUMN);
        colsb.append(", " + _LANGUAGE_COLUMN);
        colsb.append(", " + _COUNT_COLUMN);
        colsb.append(", " + _TYPE_COLUMN);
    }

    @Override
    protected void buildExtendedColumnList(StringBuilder colsb) {
        colsb.append("cp." + _NAME_COLUMN);
        colsb.append(", cp." + _DESC_COLUMN);
        colsb.append(", cp." + _VERSION_COLUMN);
        colsb.append(", cp." + _COMPLEXITY_COLUMN);
        colsb.append(", cp." + _LAYER_COLUMN);
        colsb.append(", cp." + _LANGUAGE_COLUMN);
        colsb.append(", cp." + _COUNT_COLUMN);
        colsb.append(", ct." + _NAME_COLUMN);
        colsb.append(", ct." + _DESC_COLUMN);
        colsb.append(", ct." + _VERSION_COLUMN);
        colsb.append(", ct." + EstimatorComponentTypeDao._CONTEXT_COLUMN);
        colsb.append(", ct." + _LAYER_COLUMN);
        colsb.append(", ct." + EstimatorComponentTypeDao._COST_COLUMN);
    }

    private String buildWhereClause(CriteriaBuilder builder) {
        StringBuilder sb = new StringBuilder("WHERE ");
        // TODO Build where clause
        final AtomicBoolean notfirst = new AtomicBoolean(false);
        builder.criteria.forEach(lc -> {
            BasicCriteria bc1;
            if (notfirst.get())
                sb.append(" AND ");
            else
                notfirst.set(true);
            switch (lc.op) {
            case ASIS:
                sb.append("(");
                bc1 = lc.criterion1;
                getWhereConditionFor(bc1.fieldName, bc1.op, bc1.value, sb);
                sb.append(")");
                break;
            case OR:
                sb.append("(");
                sb.append("(");
                bc1 = lc.criterion1;
                getWhereConditionFor(bc1.fieldName, bc1.op, bc1.value, sb);
                sb.append(")");
                sb.append(" OR ");
                BasicCriteria bc2 = lc.criterion2;
                sb.append("(");
                getWhereConditionFor(bc2.fieldName, bc2.op, bc2.value, sb);
                sb.append(")");
                sb.append(")");
                break;
            case NOT:
                sb.append("NOT (");
                bc1 = lc.criterion1;
                getWhereConditionFor(bc1.fieldName, bc1.op, bc1.value, sb);
                sb.append(")");
                break;
            }
        });
        return sb.toString();
    }

    private void getWhereConditionFor(String fldName, BasicCriterionOperator op, Object value, StringBuilder sb) {
        FieldMappingSpec spec = _fieldsMap.get(fldName);
        if (spec.fieldRefersTo == FieldReferenceKind.IdForType || spec.fieldRefersTo == FieldReferenceKind.ForeignTable) {
            return;
        }
        sb.append(spec.columnName);
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
    }

    private void checkAndRefresh(ResultSet rs, Component ct, String columnName, List<M3DaoFieldState> fieldsState, M3ModelFieldsException modelException) {
        M3DaoFieldState state = null;
        if (_DESC_COLUMN.equals(columnName)) {
            state = isSameStringValue(rs, "description", _DESC_COLUMN, ct.getDescription(), modelException);
            if (!state.same) {
                ct.setDescription((String) state.newValue);
            }
        } else if (_COMPLEXITY_COLUMN.equals(columnName)) {
            state = isSameStringValue(rs, "complexity", _COMPLEXITY_COLUMN, ct.getComplexity().name(), modelException);
            if (state != null && !state.same) {
                Complexity dbVal = null;
                String objval = (String) state.newValue;
                try {
                    dbVal = Complexity.valueOf(objval);
                } catch (Throwable t1) {
                    M3FieldException ex = modelException.addException(state.fieldName, "converting value from DB, invalid value", objval, t1);
                    state.exception = ex;
                }
                if (dbVal == null) {
                    M3FieldException ex = modelException.addException(state.fieldName, "converting value from DB, invalid value", objval);
                    state.exception = ex;
                }
            	ct.setComplexity(dbVal);
            }
        } else if (_LAYER_COLUMN.equals(columnName)) {
            state = isSameStringValue(rs, "layer", _LAYER_COLUMN, ct.getLayer().name(), modelException);
            if (state != null && !state.same) {
                Layer dbVal = null;
                String objval = (String) state.newValue;
                try {
                    dbVal = Layer.valueOf(objval);
                } catch (Throwable t1) {
                    M3FieldException ex = modelException.addException(state.fieldName, "converting value from DB, invalid value", objval, t1);
                    state.exception = ex;
                }
                if (dbVal == null) {
                    M3FieldException ex = modelException.addException(state.fieldName, "converting value from DB, invalid value", objval);
                    state.exception = ex;
                }
            	ct.setLayer(dbVal);
            }
        } else if (_LANGUAGE_COLUMN.equals(columnName)) {
            state = isSameStringValue(rs, "language", _LANGUAGE_COLUMN, ct.getLanguage().name(), modelException);
            if (state != null && !state.same) {
                Language dbVal = null;
                String objval = (String) state.newValue;
                try {
                    dbVal = Language.valueOf(objval);
                } catch (Throwable t1) {
                    M3FieldException ex = modelException.addException(state.fieldName, "converting value from DB, invalid value", objval, t1);
                    state.exception = ex;
                }
                if (dbVal == null) {
                    M3FieldException ex = modelException.addException(state.fieldName, "converting value from DB, invalid value", objval);
                    state.exception = ex;
                }
            	ct.setLanguage(dbVal);
            }
        } else if (_COUNT_COLUMN.equals(columnName)) {
            state = isSameLongValue(rs, "count", _COUNT_COLUMN, ct.getCount(), modelException);
            if (!state.same) {
                ct.setCount((Long) state.newValue);
            }
        } else if (_TYPE_COLUMN.equals(columnName)) {
            state = isSameStringValue(rs, "type", _TYPE_COLUMN, ct.getType().getName(), modelException);
            if (!state.same) {
                // TODO Retrieve Component Type having new name and attach here
            	// or better still form the query to extract the Component Type data too
//                ct.setCount((Long) state.newValue);
            }
        } else
            modelException.addException(columnName, "not associated with any field for column");
        if (state != null) {
            if (!state.same || state.exception != null)
                fieldsState.add(state);
        }
    }
}
