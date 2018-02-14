package m3.guesstimator.internal.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.M3ModelFieldsException.M3FieldException;
import m3.guesstimator.model.reference.ComponentContext;
import m3.guesstimator.model.reference.ComponentType;
import m3.guesstimator.model.reference.Layer;

public class EstimatorComponentTypeDao extends AbstractDao {
    private static final Class<?> ARTIFACT_TYPE = ComponentType.class;
    static final String _TABLE_NAME = "COMPONENT_TYPE";
    static final String _CONTEXT_COLUMN = "CONTEXT";
    static final String _LAYER_COLUMN = "LAYER";
    static final String _COST_COLUMN = "COSTS";

    public EstimatorComponentTypeDao() {
        super(DaoContext.COMPONENT_TYPE);
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
        ComponentType ct = (ComponentType)o;
        return ct.getName();
    }

    public ComponentType put(ComponentType ct) {
        EntityState es = getState(ct);
        ComponentType resVal = null;
        if (es.isNew()) {
            resVal = addNew(ct);
        } else {
            resVal = update(ct);
        }
        es.unlockAndReset();
        return resVal;
    }

	public ComponentType get(String name) {
        return null; // TODO Implement
    }

    public List<ComponentType> find(CriteriaBuilder cb) {
        return null; // TODO Implement
    }

    private ComponentType addNew(ComponentType ct) {
        String ctname = ct.getName();
        String forModel = ComponentType.class.getSimpleName();
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

    private ComponentType update(ComponentType ct) {
	    // TODO Auto-generated method stub
	    return ct;
    }

    private void checkAndRefresh(Connection conn, ComponentType ct, String col_names, M3ModelFieldsException modelException) {
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

    @Override
    protected void populateFieldSpecs() {
        registerBaseFieldSpecs();
        registerFieldSpec("context", _CONTEXT_COLUMN, ComponentContext.class, FieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("architecturalLayer", _LAYER_COLUMN, Layer.class, FieldReferenceKind.EnumType, null, null, null);
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
		// TODO Auto-generated method stub
		
	}
}
