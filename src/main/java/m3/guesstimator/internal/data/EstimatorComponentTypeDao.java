package m3.guesstimator.internal.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;

import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.M3ModelFieldsException.M3FieldException;
import m3.guesstimator.model.reference.ComponentContext;
import m3.guesstimator.model.reference.M3ComponentType;
import m3.guesstimator.model.reference.Layer;

public class EstimatorComponentTypeDao extends AbstractDao {
    private static final Class<?> ARTIFACT_TYPE = M3ComponentType.class;
    static final String _TABLE_NAME = "COMPONENT_TYPE";
    static final String _CONTEXT_COLUMN = "CONTEXT";
    static final String _LAYER_COLUMN = "LAYER";
    static final String _COST_COLUMN = "COSTS";

    public EstimatorComponentTypeDao() {
        super(M3DaoContext.COMPONENT_TYPE);
        populateFieldSpecs();
    }

    @Override
    protected void ensureObjectOfType(Object o) {
        if (!ARTIFACT_TYPE.isAssignableFrom(o.getClass())) {
            throw new IllegalArgumentException("Entity must be of type " + ARTIFACT_TYPE.getSimpleName() + "!!!");
        }
    }

    @Override
    protected Class<?> getTypeOfObject(Object o) {
        return ARTIFACT_TYPE;
    }

    @Override
    protected String getNameOfObject(Object o) {
        M3ComponentType ct = (M3ComponentType)o;
        return ct.getName();
    }

    public M3ComponentType put(M3ComponentType ct) {
        M3DaoEntityState es = getState(ct);
        M3ComponentType resVal = null;
        if (es.isNew()) {
            resVal = addNew(ct);
        } else {
            resVal = update(ct);
        }
        es.unlockAndReset();
        return resVal;
    }

	public M3ComponentType get(String name) {
        return null; // TODO Implement
    }

    public List<M3ComponentType> find(M3DaoCriteriaBuilder cb) {
        StringBuilder colsb = new StringBuilder();
        buildBasicColumnList(colsb);
        StringBuilder sqlsb = new StringBuilder("SELECT ");
        sqlsb.append(colsb.toString());
        sqlsb.append(" FROM ");
	    sqlsb.append(_TABLE_NAME);
	    if (cb != null) {
	        sqlsb.append(" ");
	    	buildWhereClause(cb, sqlsb);
	    }
        List<M3ComponentType> results = new ArrayList<M3ComponentType>();
        Function<M3DaoHandlerParam, M3ComponentTypeHandlerResult> qfunc = (param) ->
        {
            M3ModelFieldsException mfex = new M3ModelFieldsException(M3ComponentType.class.getSimpleName(), "collecting SQL SELECT exceptions");
	        M3ComponentTypeHandlerResult result = new M3ComponentTypeHandlerResult(mfex);
            try {
    		    if (param.rs.first()) {
    		        ResultSetMetaData rsmd = param.rs.getMetaData();
    		        int colCnt = rsmd.getColumnCount();
    		        List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
    		        M3ComponentType ct1 = retrieveValues(param.rs, rsmd, colCnt, fieldsState, mfex);
    		        result.add(fieldsState);
    		        result.add(ct1);
    		        while (param.rs.next()) {
        		        fieldsState = new ArrayList<M3DaoFieldState>();
        		        ct1 = retrieveValues(param.rs, rsmd, colCnt, fieldsState, mfex);
        		        result.add(fieldsState);
        		        result.add(ct1);
    		        }
    		        // TODO Do something with fieldsState
    		    }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
			return result;
        };
        performQuery(sqlsb.toString(), results, qfunc);
        return results;
    }

    public void removeAll() {
        String forModel = M3ComponentType.class.getSimpleName();
        StringBuilder sqlsb = new StringBuilder("SELECT FROM ");
	    sqlsb.append(_TABLE_NAME);
        String del_sql = sqlsb.toString();
        Connection conn = null;
        Statement stmt = null;
        int res_count = -1;
        M3ModelFieldsException mfex = new M3ModelFieldsException(forModel, "collecting SQL INSERT exceptions");
        try {
            conn = getConnection(forModel);
            stmt = conn.createStatement();
            res_count = stmt.executeUpdate(del_sql);
        } catch (SQLException ex) {
            // TODO Handle exception
        } finally {
            try {
                if (stmt != null && !stmt.isClosed())
                    stmt.close();
                if (conn != null && !conn.isClosed())
                    conn.close();
            } catch (SQLException sqle2) {
                // Ignore
            }
        }
        // TODO Log res_count deleted
		if (mfex.getExceptions().length > 0) { // row was inserted so we can go out and retrieve it
	        // TODO Check and do something with mfex
		}
    }

	private M3ComponentType addNew(M3ComponentType ct) {
        String ctname = ct.getName();
        String forModel = M3ComponentType.class.getSimpleName();
        StringBuilder valuesb = new StringBuilder();
        StringBuilder colsb = new StringBuilder();
        buildBasicColumnList(colsb);
        valuesb.append("'" + ctname + "'");
        valuesb.append(", '" + ct.getDescription() + "'");
        valuesb.append(", '" + ct.getVersion() + "'");
        valuesb.append(", '" + ct.getContext() + "'");
        valuesb.append(", '" + ct.getArchitecturalLayer() + "'");
        valuesb.append(", '" + ct.getStrConstructCosts() + "'");
        String col_names = colsb.toString();
        StringBuilder sqlsb = new StringBuilder("INSERT INTO " + _TABLE_NAME + " (");
        sqlsb.append(col_names);
        sqlsb.append(") VALUES (");
        sqlsb.append(valuesb.toString());
        sqlsb.append(")");
        String ins_sql = sqlsb.toString();
        Connection conn = null;
        Statement stmt = null;
        int res_count = -1;
        M3ModelFieldsException mfex = new M3ModelFieldsException(forModel, "collecting SQL INSERT exceptions");
        try {
            conn = getConnection(forModel);
            stmt = conn.createStatement();
            res_count = stmt.executeUpdate(ins_sql);
        } catch (SQLException ex) {
            // TODO Handle exception
        } finally {
            try {
                if (stmt != null && !stmt.isClosed())
                    stmt.close();
                if (conn != null && !conn.isClosed())
                    conn.close();
            } catch (SQLException sqle2) {
                // Ignore
            }
        }
		if (res_count > 0 && mfex.getExceptions().length == 0) { // row was inserted so we can go out and retrieve it
            checkAndRefresh(conn, ct, col_names, mfex);
		}
        // TODO Check and do something with mfex
        return ct;
    }

    private M3ComponentType update(M3ComponentType ct) {
	    // TODO Auto-generated method stub
	    return ct;
    }

    @Override
    protected void populateFieldSpecs() {
        registerBaseFieldSpecs();
        registerFieldSpec("context", _CONTEXT_COLUMN, ComponentContext.class, M3DaoFieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("architecturalLayer", _LAYER_COLUMN, Layer.class, M3DaoFieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("strConstructCosts", _COST_COLUMN, String.class, null, null, null, null);
    }

	@Override
	protected void buildBasicColumnList(StringBuilder colsb) {
        colsb.append(_NAME_COLUMN);
        colsb.append(", " + _DESC_COLUMN);
        colsb.append(", " + _VERSION_COLUMN);
        colsb.append(", " + _CONTEXT_COLUMN);
        colsb.append(", " + _LAYER_COLUMN);
        colsb.append(", " + _COST_COLUMN);
	}

    @Override
    protected void buildExtendedColumnList(StringBuilder colsb) {
        throw new UnsupportedOperationException("EstimatorComponentTypeDao.buildExtendedColumnList");
    }

    private void checkAndRefresh(Connection conn, M3ComponentType ct, String col_names, M3ModelFieldsException modelException) {
        StringBuilder sqlsb = new StringBuilder("SELECT ");
	    sqlsb.append(col_names);
	    sqlsb.append(" FROM ");
	    sqlsb.append(_TABLE_NAME);
	    sqlsb.append(" WHERE NAME = ");
	    sqlsb.append(ct.getName());
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlsb.toString());
    	    if (rs.first()) {
    	        ResultSetMetaData rsmd = rs.getMetaData();
    	        int colCnt = rsmd.getColumnCount();
    	        List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
    	        for (int colIx = 0; colIx < colCnt; colIx++) {
    	            M3DaoFieldState state = null;
    	            String columnName = rsmd.getColumnLabel(colIx);
    	            if (_DESC_COLUMN.equals(columnName)) {
    	                state = isSameStringValue(rs, "description", _DESC_COLUMN, ct.getDescription(), modelException);
    	                if (!state.same) {
        	                ct.setDescription((String) state.newValue);
    	                }
    	            } else if (_CONTEXT_COLUMN.equals(columnName)) {
    	                state = isSameStringValue(rs, "context", _CONTEXT_COLUMN, ct.getContext().name(), modelException);
    	                if (state != null && !state.same) {
                            ComponentContext dbVal = null;
                            String objval = (String) state.newValue;
                            try {
                                dbVal = ComponentContext.valueOf(objval);
                            } catch (Throwable t1) {
                                M3FieldException ex = modelException.addException(state.fieldName, "converting value from DB, invalid value", objval, t1);
                                state.exception = ex;
                            }
                            if (dbVal == null) {
                                M3FieldException ex = modelException.addException(state.fieldName, "converting value from DB, invalid value", objval);
                                state.exception = ex;
                            }
                        	ct.setContext(dbVal);
    	                }
    	            } else if (_LAYER_COLUMN.equals(columnName)) {
    	                state = isSameStringValue(rs, "architecturalLayer", _LAYER_COLUMN, ct.getArchitecturalLayer().name(), modelException);
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
                        	ct.setArchitecturalLayer(dbVal);
    	                }
	                } else if (_COST_COLUMN.equals(columnName)) {
    	                state = null;
	                } else
    	                modelException.addException(columnName, "not associated with any field for column");
    	            if (state != null) {
    	                if (!state.same || state.exception != null)
    	                    fieldsState.add(state);
    	            }
    	        }
		        // TODO Do something with fieldsState
    	    }
        } catch (SQLException ex) {
            // TODO Handle exception
        } finally {
            try {
                if (rs != null && !rs.isClosed())
                    rs.close();
                if (stmt != null && !stmt.isClosed())
                    stmt.close();
            } catch (SQLException sqle2) {
                // Ignore
            }
        }
    }

    private M3ComponentType retrieveValues(ResultSet rs, ResultSetMetaData rsmd, int colCnt,
                List<M3DaoFieldState> fieldsState, M3ModelFieldsException mfex) {
        M3ComponentType ct = new M3ComponentType();
        for (int colIx = 0; colIx < colCnt; colIx++) {
            M3DaoFieldState state = null;
            String columnName = getColumnName(rsmd, colIx, mfex);
            if (columnName == null) {
                state = new M3DaoFieldState();
                state.fieldName = "UNKNOWN";
                M3FieldException ex = mfex.addException(state.fieldName, "extracting column name from DB");
                state.exception = ex;
                fieldsState.add(state);
                continue;
            }
            if (_DESC_COLUMN.equals(columnName)) {
                state = retrieveStringValue(rs, "description", columnName, mfex);
                if (state != null && !state.same && state.newValue != null) {
                    ct.setDescription((String) state.newValue);
                }
            } else if (_CONTEXT_COLUMN.equals(columnName)) {
                state = retrieveStringValue(rs, "context", columnName, mfex);
                retrieveContext(state, mfex);
                if (state != null && !state.same && state.newValue != null) {
                    ct.setContext( (ComponentContext)state.newValue);
                }
            } else if (_LAYER_COLUMN.equals(columnName)) {
                state = retrieveStringValue(rs, "architecturalLayer", columnName, mfex);
                retrieveLayer(state, mfex);
                if (state != null && !state.same && state.newValue != null) {
                    ct.setArchitecturalLayer( (Layer)state.newValue);
                }
            } else if (_COST_COLUMN.equals(columnName)) {
                state = null;
            } else {
                mfex.addException(columnName, "not associated with any field for column");
            }
            if (state != null) {
                if (!state.same || state.exception != null)
                    fieldsState.add(state);
            }
        }
	    return ct;
    }

    private class M3ComponentTypeHandlerResult {
        final Set<List<M3DaoFieldState>> fieldsStates = Collections.synchronizedSet(new ConcurrentSkipListSet<List<M3DaoFieldState>>());
        final Set<M3ComponentType> artifacts = Collections.synchronizedSet(new ConcurrentSkipListSet<M3ComponentType>());
        final M3ModelFieldsException mfex;

        M3ComponentTypeHandlerResult(M3ModelFieldsException mfe) {
            mfex = mfe;
        }

        void add(List<M3DaoFieldState> fs) {
            fieldsStates.add(fs);
        }

        void add(M3ComponentType sa) {
            artifacts.add(sa);
        }
    }

    private void performQuery(String sqlstr, List<M3ComponentType> results, 
                Function<M3DaoHandlerParam, M3ComponentTypeHandlerResult> handler) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection(M3ComponentType.class.getSimpleName());
            stmt = conn.createStatement();
		    rs = stmt.executeQuery(sqlstr);
		    M3DaoHandlerParam param = new M3DaoHandlerParam(rs);
		    M3ComponentTypeHandlerResult result = handler.apply(param);
		    if (result.mfex != null && (result.mfex.getExceptions().length > 0 
		            || result.mfex.getCause() != null)) {
		        throw result.mfex;
		    }
            result.artifacts.forEach(sa1 -> results.add(sa1));
        } catch (SQLException ex) {
            throw new M3ModelException("querying [" + sqlstr + "]", M3ComponentType.class.getSimpleName());
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
    }

    static void retrieveContext(M3DaoFieldState state, M3ModelFieldsException mfex) {
        if (state != null && !state.same && state.newValue != null) {
            ComponentContext dbVal = null;
            String objval = (String) state.newValue;
            try {
                dbVal = ComponentContext.valueOf(objval);
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
