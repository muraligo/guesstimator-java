package m3.guesstimator.internal.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.M3ModelFieldsException.M3FieldException;
import m3.guesstimator.model.functional.M3Component;
import m3.guesstimator.model.reference.Complexity;
import m3.guesstimator.model.reference.ComponentContext;
import m3.guesstimator.model.reference.M3ComponentType;
import m3.guesstimator.model.reference.Language;
import m3.guesstimator.model.reference.Layer;

public class EstimatorComponentDao extends AbstractDao {
    private static final Class<?> ARTIFACT_TYPE = M3Component.class;
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

	public M3Component get(String name) {
        StringBuilder colsb = new StringBuilder();
        buildExtendedColumnList(colsb);
        StringBuilder sqlsb = new StringBuilder("SELECT ");
        sqlsb.append(colsb.toString());
        sqlsb.append(" FROM ");
	    sqlsb.append(_TABLE_NAME + " cp, ");
	    sqlsb.append(EstimatorComponentTypeDao._TABLE_NAME + " ct");
	    sqlsb.append(" WHERE cp.NAME = ");
        sqlsb.append(name);
        sqlsb.append(" AND cp." + _TYPE_COLUMN + " = ct." + _NAME_COLUMN);
        List<SolutionArtifact> perfqres = new ArrayList<SolutionArtifact>();
        Function<M3DaoHandlerParam, M3DaoHandlerResult> qfunc = (param) ->
        {
            M3ModelFieldsException mfex = new M3ModelFieldsException(M3Component.class.getSimpleName(), "collecting SQL SELECT exceptions");
	        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
            try {
                if (param.rs.first()) {
                    ResultSetMetaData rsmd = param.rs.getMetaData();
                    int colCnt = rsmd.getColumnCount();
                    List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
                    M3Component cmp = new M3Component();
                    for (int colIx = 0; colIx < colCnt; colIx++) {
                        retrieveValues(param.rs, cmp, rsmd.getColumnLabel(colIx), fieldsState, mfex);
                    }
                    // TODO Do something with fieldsState
                    result.add(fieldsState);
                    result.add(cmp);
                }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
			return result;
        };
        performQuery(new M3Component(), sqlsb.toString(), perfqres, qfunc);
        // TODO Handle Exception
        return (M3Component)perfqres.get(0);
    }

    public List<M3Component> find(M3DaoCriteriaBuilder cb) {
        StringBuilder colsb = new StringBuilder();
        buildExtendedColumnList(colsb);
        StringBuilder sqlsb = new StringBuilder("SELECT ");
        sqlsb.append(colsb.toString());
        sqlsb.append(" FROM ");
	    sqlsb.append(_TABLE_NAME + " cp, ");
	    sqlsb.append(EstimatorComponentTypeDao._TABLE_NAME + " ct");
	    if (cb != null) {
	    	sqlsb.append(" ");
	    	buildWhereClause(cb, sqlsb);
	        sqlsb.append(" AND cp." + _TYPE_COLUMN + " = ct." + _NAME_COLUMN);
	    } else {
	        sqlsb.append(" WHERE cp." + _TYPE_COLUMN + " = ct." + _NAME_COLUMN);
	    }
        List<SolutionArtifact> perfqres = new ArrayList<SolutionArtifact>();
        Function<M3DaoHandlerParam, M3DaoHandlerResult> qfunc = (param) ->
        {
            M3ModelFieldsException mfex = new M3ModelFieldsException(M3Component.class.getSimpleName(), "collecting SQL SELECT exceptions");
	        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
            try {
                if (param.rs.first()) {
                    ResultSetMetaData rsmd = param.rs.getMetaData();
                    int colCnt = rsmd.getColumnCount();
                    List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
                    M3Component cmp = new M3Component();
                    for (int colIx = 0; colIx < colCnt; colIx++) {
                        retrieveValues(param.rs, cmp, rsmd.getColumnLabel(colIx), fieldsState, mfex);
                    }
                    // TODO Do something with fieldsState
                    result.add(fieldsState);
                    result.add(cmp);
                    while (param.rs.next()) {
                        fieldsState = new ArrayList<M3DaoFieldState>();
                        cmp = new M3Component();
                        for (int colIx = 0; colIx < colCnt; colIx++) {
                            retrieveValues(param.rs, cmp, rsmd.getColumnLabel(colIx), fieldsState, mfex);
                        }
                        // TODO Do something with fieldsState
                        result.add(fieldsState);
                        result.add(cmp);
    		        }
    		    }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
			return result;
        };
        performQuery(new M3Component(), sqlsb.toString(), perfqres, qfunc);
        // TODO Handle Exception
        List<M3Component> results = new ArrayList<M3Component>();
        perfqres.forEach(sa1 -> results.add((M3Component)sa1));
        return results;
    }

    public M3Component put(M3Component sa) {
        M3DaoEntityState es = getState(sa);
        M3Component resVal = null;
        if (es.isNew()) {
            resVal = addNew(sa);
        } else {
            resVal = update(sa);
        }
        es.unlockAndReset();
        return resVal;
    }

    // TODO Lock before executing update and verifying
    protected M3Component addNew(M3Component ct) {
        String saname = ct.getName();
        StringBuilder valuesb = new StringBuilder();
        StringBuilder colsb = new StringBuilder();
        buildBasicColumnList(colsb);
        valuesb.append("'" + saname + "'");
        valuesb.append(", '" + ct.getDescription() + "'");
        valuesb.append(", '" + ct.getVersion() + "'");
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
		    // colsb is the same BasicColumnList
		    sqlsb.append("SELECT ");
		    sqlsb.append(colsb.toString());
		    sqlsb.append(" FROM ");
		    sqlsb.append(_TABLE_NAME);
		    sqlsb.append(" WHERE NAME = ");
		    sqlsb.append(saname);
	        List<SolutionArtifact> results = new ArrayList<SolutionArtifact>();
	        Function<M3DaoHandlerParam, M3DaoHandlerResult> qfunc = (param) ->
	        {
	            M3ModelFieldsException mfex2 = new M3ModelFieldsException(ct.getClass().getSimpleName(), "collecting SQL SELECT exceptions");
		        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex2);
	            try {
	    		    if (param.rs.first()) {
	    		        ResultSetMetaData rsmd = param.rs.getMetaData();
	    		        int colCnt = rsmd.getColumnCount();
	    		        List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
	    		        for (int colIx = 0; colIx < colCnt; colIx++) {
	    		            checkAndRefresh(param.rs, ct, rsmd.getColumnLabel(colIx), fieldsState, mfex);
	    		        }
	    		        // TODO Do something with fieldsState
	    		        result.add(fieldsState);
	    		        result.add(ct);
	    		    }
	            } catch (SQLException ex) {
	                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData", ex);
	            }
				return result;
	        };
	        performQuery(ct, sqlsb.toString(), results, qfunc);
		}
        // TODO Check and do something with mfex
        return ct;
    }

    private M3Component update(M3Component sa) {
	    // TODO Auto-generated method stub
	    return null;
    }

    @Override
    protected void populateFieldSpecs() {
        registerBaseFieldSpecs();
        registerFieldSpec("complexity", _COMPLEXITY_COLUMN, Complexity.class, M3DaoFieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("layer", _LAYER_COLUMN, Layer.class, M3DaoFieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("language", _LANGUAGE_COLUMN, Complexity.class, M3DaoFieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("type", _TYPE_COLUMN, Layer.class, M3DaoFieldReferenceKind.EnumType, null, null, null);
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

    private void checkAndRefresh(ResultSet rs, M3Component ct, String colName, List<M3DaoFieldState> fieldsState, M3ModelFieldsException modelException) {
        M3DaoFieldState state = null;
        if (_DESC_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "description", colName, ct.getDescription(), modelException);
            if (!state.same && !state.same && state.newValue != null) {
                ct.setDescription((String) state.newValue);
            }
        } else if (_COMPLEXITY_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "complexity", colName, ct.getComplexity().name(), modelException);
            retrieveComplexity(state, modelException);
            if (state != null && !state.same && state.newValue != null) {
            	ct.setComplexity( (Complexity)state.newValue);
            }
        } else if (_LAYER_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "layer", colName, ct.getLayer().name(), modelException);
            retrieveLayer(state, modelException);
            if (state != null && !state.same && state.newValue != null) {
            	ct.setLayer( (Layer)state.newValue);
            }
        } else if (_LANGUAGE_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "language", colName, ct.getLanguage().name(), modelException);
            retrieveLanguage(state, modelException);
            if (state != null && !state.same && state.newValue != null) {
            	ct.setLanguage( (Language)state.newValue);
            }
        } else if (_COUNT_COLUMN.equals(colName)) {
            state = isSameLongValue(rs, "count", colName, ct.getCount(), modelException);
            if (state != null && !state.same && state.newValue != null) {
                ct.setCount((Long) state.newValue);
            }
        } else if (_TYPE_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "type", colName, ct.getType().getName(), modelException);
            if (state != null && !state.same) {
                // TODO This condition should be an exception
            }
        } else
            modelException.addException(colName, "not associated with any field for column");
        if (state != null) {
            if (!state.same || state.exception != null)
                fieldsState.add(state);
        }
    }

    private void retrieveValues(ResultSet rs, M3Component ct, String colName, List<M3DaoFieldState> fieldsState, M3ModelFieldsException mfex) {
        M3ComponentType compType = new M3ComponentType();
        M3DaoFieldState state = null;
        int cnix = colName.indexOf(".");
        String columnName = (cnix > 0) ? colName.substring(cnix+1) : colName;
        if (_DESC_COLUMN.equals(columnName) && colName.startsWith("cp.")) {
            state = retrieveStringValue(rs, "description", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                ct.setDescription((String) state.newValue);
            }
        } else if (_COMPLEXITY_COLUMN.equals(columnName)) {
            state = retrieveStringValue(rs, "complexity", colName, mfex);
            retrieveComplexity(state, mfex);
            if (state != null && !state.same && state.newValue != null) {
            	ct.setComplexity( (Complexity) state.newValue);
            }
        } else if (_LAYER_COLUMN.equals(columnName) && colName.startsWith("cp.")) {
            state = retrieveStringValue(rs, "layer", colName, mfex);
            retrieveLayer(state, mfex);
            if (state != null && !state.same && state.newValue != null) {
            	ct.setLayer( (Layer) state.newValue);
            }
        } else if (_LANGUAGE_COLUMN.equals(columnName)) {
            state = retrieveStringValue(rs, "language", colName, mfex);
            retrieveLanguage(state, mfex);
            if (state != null && !state.same && state.newValue != null) {
            	ct.setLanguage( (Language) state.newValue);
            }
        } else if (_COUNT_COLUMN.equals(columnName)) {
            state = retrieveLongValue(rs, "count", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                ct.setCount((Long) state.newValue);
            }
        } else if (_TYPE_COLUMN.equals(columnName)) {
            state = retrieveStringValue(rs, "type", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                compType.setName((String) state.newValue);
            }
        } else if (_DESC_COLUMN.equals(columnName) && colName.startsWith("ct.")) {
            state = retrieveStringValue(rs, "description", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                compType.setDescription((String) state.newValue);
            }
        } else if (EstimatorComponentTypeDao._CONTEXT_COLUMN.equals(columnName)) {
            state = retrieveStringValue(rs, "context", colName, mfex);
            EstimatorComponentTypeDao.retrieveContext(state, mfex);
            if (state != null && !state.same && state.newValue != null) {
                compType.setContext( (ComponentContext)state.newValue);
            }
        } else if (_LAYER_COLUMN.equals(columnName) && colName.startsWith("ct.")) {
            state = retrieveStringValue(rs, "architecturalLayer", colName, mfex);
            retrieveLayer(state, mfex);
            if (state != null && !state.same && state.newValue != null) {
                compType.setArchitecturalLayer( (Layer)state.newValue);
            }
        } else if (EstimatorComponentTypeDao._COST_COLUMN.equals(columnName)) {
            state = null;
            // TODO We really do need to retrieve the value
        } else {
            mfex.addException(colName, "not associated with any field for column");
        }
        if (state != null) {
            if (!state.same || state.exception != null)
                fieldsState.add(state);
        }
	}

	static void retrieveComplexity(M3DaoFieldState state, M3ModelFieldsException mfex) {
        if (state != null && !state.same && state.newValue != null) {
            Complexity dbVal = null;
            String objval = (String) state.newValue;
            try {
                dbVal = Complexity.valueOf(objval);
            } catch (Throwable t1) {
                M3FieldException ex = mfex.addException(state.fieldName, "converting value from DB, invalid value", objval, t1);
                state.exception = ex;
            }
            if (dbVal == null) {
                M3FieldException ex = mfex.addException(state.fieldName, "converting value from DB, invalid value", objval);
                state.exception = ex;
            }
            state.newValue = dbVal;
        }
    }

    static void retrieveLanguage(M3DaoFieldState state, M3ModelFieldsException mfex) {
        if (state != null && !state.same && state.newValue != null) {
            Language dbVal = null;
            String objval = (String) state.newValue;
            try {
                dbVal = Language.valueOf(objval);
            } catch (Throwable t1) {
                M3FieldException ex = mfex.addException(state.fieldName, "converting value from DB, invalid value", objval, t1);
                state.exception = ex;
            }
            if (dbVal == null) {
                M3FieldException ex = mfex.addException(state.fieldName, "converting value from DB, invalid value", objval);
                state.exception = ex;
            }
            state.newValue = dbVal;
        }
    }
}
