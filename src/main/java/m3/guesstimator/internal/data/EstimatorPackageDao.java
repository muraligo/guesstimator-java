package m3.guesstimator.internal.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.functional.M3Component;
import m3.guesstimator.model.functional.M3Package;
import m3.guesstimator.model.reference.Complexity;
import m3.guesstimator.model.reference.Language;
import m3.guesstimator.model.reference.Layer;

public class EstimatorPackageDao extends AbstractDao {
    private static final Class<?> ARTIFACT_TYPE = ContainingSolutionArtifact.class;
    private static final String _TABLE_NAME = "PACKAGE";
    private static final String _OVERHEAD_COLUMN = "OVERHEAD";
    private static final String _COMPOSITION_COLUMN = "COMPOSITION";

    private EstimatorComponentDao _componentDao;

    public EstimatorPackageDao() {
        super();
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
        ContainingSolutionArtifact csa = (ContainingSolutionArtifact)o;
        return csa.getName();
    }

    public M3Package get(String name) {
        StringBuilder colsb = new StringBuilder();
        buildExtendedColumnList(colsb);
        StringBuilder sqlsb = new StringBuilder("SELECT ");
        sqlsb.append(colsb.toString());
        sqlsb.append(" FROM ");
	    sqlsb.append(_TABLE_NAME);
	    sqlsb.append(" WHERE NAME = ");
        sqlsb.append(name);
        List<SolutionArtifact> perfqres = new ArrayList<SolutionArtifact>();
        Function<M3DaoHandlerParam, M3DaoHandlerResult> qfunc = (param) ->
        {
            M3ModelFieldsException mfex = new M3ModelFieldsException(M3Package.class.getSimpleName(), "collecting SQL SELECT exceptions");
	        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
            try {
    		    if (param.rs.first()) {
    		        ResultSetMetaData rsmd = param.rs.getMetaData();
    		        int colCnt = rsmd.getColumnCount();
    		        List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
    		        M3Package pkg = new M3Package();
    		        for (int colIx = 0; colIx < colCnt; colIx++) {
    		            retrieveValues(param.rs, pkg, rsmd.getColumnLabel(colIx), fieldsState, mfex);
    		        }
    		        // TODO Do something with fieldsState
    		        result.add(fieldsState);
    		        result.add(pkg);
    		    }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
			return result;
        };
        performQuery(new M3Package(), sqlsb.toString(), perfqres, qfunc);
        // TODO Handle exceptions
        return (M3Package)perfqres.get(0);
    }

    public List<M3Package> find(M3DaoCriteriaBuilder cb) {
        StringBuilder colsb = new StringBuilder();
        buildExtendedColumnList(colsb);
        StringBuilder sqlsb = new StringBuilder("SELECT ");
        sqlsb.append(colsb.toString());
        sqlsb.append(" FROM ");
	    sqlsb.append(_TABLE_NAME);
	    if (cb != null) {
	    	sqlsb.append(" ");
	    	buildWhereClause(cb, sqlsb);
	    }
        List<SolutionArtifact> perfqres = new ArrayList<SolutionArtifact>();
        Function<M3DaoHandlerParam, M3DaoHandlerResult> qfunc = (param) ->
        {
            M3ModelFieldsException mfex = new M3ModelFieldsException(M3Package.class.getSimpleName(), "collecting SQL SELECT exceptions");
	        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
            try {
    		    if (param.rs.first()) {
    		        ResultSetMetaData rsmd = param.rs.getMetaData();
    		        int colCnt = rsmd.getColumnCount();
    		        List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
    		        M3Package pkg = new M3Package();
    		        for (int colIx = 0; colIx < colCnt; colIx++) {
    		            retrieveValues(param.rs, pkg, rsmd.getColumnLabel(colIx), fieldsState, mfex);
    		        }
    		        // TODO Do something with fieldsState
    		        result.add(fieldsState);
    		        result.add(pkg);
    		        while (param.rs.next()) {
        		        fieldsState = new ArrayList<M3DaoFieldState>();
        		        pkg = new M3Package();
        		        for (int colIx = 0; colIx < colCnt; colIx++) {
        		            retrieveValues(param.rs, pkg, rsmd.getColumnLabel(colIx), fieldsState, mfex);
        		        }
        		        // TODO Do something with fieldsState
        		        result.add(fieldsState);
        		        result.add(pkg);
    		        }
    		    }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
			return result;
        };
        performQuery(new M3Package(), sqlsb.toString(), perfqres, qfunc);
        // TODO Handle exceptions
        List<M3Package> results = new ArrayList<M3Package>();
        perfqres.forEach(sa1 -> results.add((M3Package)sa1));
        return results;
    }

    public M3Package put(M3Package csa) {
        M3DaoEntityState es = getState(csa);
        // TODO What about parent - child hierarchy checks?
        // TODO Build the JDBC
        // TODO Get a Connection from the Pool
        // TODO Execute the JDBC
        // TODO Retrieve the object
        // TODO Reset state.op to NONE and unlock
        M3Package resVal = null;
        if (es.isNew()) {
            resVal = addNew(csa);
        } else {
            resVal = update(csa);
        }
        es.unlockAndReset();
        return resVal;
    }

    protected M3Package addNew(M3Package pkg) {
        String saname = pkg.getName();
        StringBuilder valuesb = new StringBuilder();
        StringBuilder colsb = new StringBuilder();
        buildBasicColumnList(colsb);
        valuesb.append("'" + saname + "'");
        valuesb.append(", '" + pkg.getDescription() + "'");
        valuesb.append(", '" + pkg.getVersion() + "'");
        valuesb.append(", '" + pkg.getStrCompositionFactors() + "'");
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
        M3ModelFieldsException mfex = new M3ModelFieldsException(pkg.getClass().getSimpleName(), "collecting SQL INSERT exceptions");
        try {
            conn = getConnection(pkg.getClass().getSimpleName());
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
		    sqlsb.append(_TABLE_NAME);
		    sqlsb.append(" WHERE NAME = ");
		    sqlsb.append(saname);
	        List<SolutionArtifact> results = new ArrayList<SolutionArtifact>();
	        Function<M3DaoHandlerParam, M3DaoHandlerResult> qfunc = (param) ->
	        {
		        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
	            try {
	    		    if (param.rs.first()) {
	    		        ResultSetMetaData rsmd = param.rs.getMetaData();
	    		        int colCnt = rsmd.getColumnCount();
	    		        List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
	    		        for (int colIx = 0; colIx < colCnt; colIx++) {
	    		            checkAndRefresh(param.rs, pkg, rsmd.getColumnLabel(colIx), fieldsState, mfex);
	    		        }
	    		        // TODO Do something with fieldsState
	    		        result.add(fieldsState);
	    		        result.add(pkg);
	    		    }
	            } catch (SQLException ex) {
	                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData", ex);
	            }
				return result;
	        };
	        performQuery(pkg, sqlsb.toString(), results, qfunc);
		}
        List<M3Component> addlist = new ArrayList<M3Component>();
        List<M3Component> updlist = new ArrayList<M3Component>();
        Map<String, M3Component> updmap = new HashMap<String, M3Component>();
        int constIx = 0;
        pkg.getConstituents().forEach(sa1 -> {
            String fldnm = "constituents[" + Integer.toString(constIx) + "#" + sa1.getName() + "]";
            if (sa1 instanceof M3Component) {
                M3Component ct1 = null;
                try {
                    ct1 = _componentDao.get(sa1.getName());
                    if (ct1 == null) { // does not exist
                        addlist.add((M3Component)sa1);
                    } else {
                        updlist.add((M3Component)sa1);
                        updmap.put(sa1.getName(), ct1);
                    }
                } catch (M3ModelFieldsException mfex2) {
                    // TODO handle exception; it does not mean it doesn't exist
                }
            } else {
                mfex.addException(fldnm, "constituent must be a Component");
            }
        });
        compareValues(updlist, updmap, mfex);
		// TODO For the adds use child dao to insert each
		// TODO For the updates (in update map) use child dao to update each
        // TODO Check and do something with mfex
        return pkg;
    }
    /*
    _TABLE_NAME
    _COMPOSITION_COLUMN
    "strCompositionFactors"
    getStrCompositionFactors
    setStrCompositionFactors
     */

    private M3Package update(M3Package pkg) {
	    // TODO Auto-generated method stub
	    return null;
    }

    @Override
    protected void populateFieldSpecs() {
        registerBaseFieldSpecs();
        registerFieldSpec("strCompositionFactors", _COMPOSITION_COLUMN, String.class, null, null, null, null);
    }

    @Override
    protected void buildBasicColumnList(StringBuilder colsb) {
        colsb.append(_NAME_COLUMN);
        colsb.append(", " + _DESC_COLUMN);
        colsb.append(", " + _VERSION_COLUMN);
        colsb.append(", " + _COMPOSITION_COLUMN);
    }

    @Override
    protected void buildExtendedColumnList(StringBuilder colsb) {
        buildBasicColumnList(colsb);
    }

    void setComponentDao(EstimatorComponentDao dao) {
        _componentDao = dao;
    }

    private void checkAndRefresh(ResultSet rs, M3Package pkg, String colName, List<M3DaoFieldState> fieldsState, M3ModelFieldsException modelException) {
        M3DaoFieldState state = null;
        if (_DESC_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "description", colName, pkg.getDescription(), modelException);
            if (state != null && !state.same && state.newValue != null) {
                pkg.setDescription((String) state.newValue);
            }
        } else if (_VERSION_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "version", colName, pkg.getVersion(), modelException);
            if (state != null && !state.same && state.newValue != null) {
            	pkg.setVersion((String) state.newValue);
            }
        } else if (_COMPOSITION_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "strCompositionFactors", colName, pkg.getStrCompositionFactors(), modelException);
            if (state != null && !state.same && state.newValue != null) {
            	pkg.setStrCompositionFactors((String) state.newValue);
            }
        } else {
            modelException.addException(colName, "not associated with any field for column");
        }
        if (state != null) {
            if (!state.same || state.exception != null)
                fieldsState.add(state);
        }
    }

    private void retrieveValues(ResultSet rs, M3Package pkg, String colName, List<M3DaoFieldState> fieldsState, M3ModelFieldsException mfex) {
        M3DaoFieldState state = null;
        if (_NAME_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "name", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                pkg.setName((String) state.newValue);
            }
        } else if (_DESC_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "description", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
            	pkg.setDescription((String) state.newValue);
            }
        } else if (_VERSION_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "version", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
            	pkg.setVersion((String) state.newValue);
            }
        } else if (_COMPOSITION_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "strCompositionFactors", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
            	pkg.setStrCompositionFactors((String) state.newValue);
            }
        } else {
            mfex.addException(colName, "not associated with any field for column");
        }
        if (state != null) {
            if (!state.same || state.exception != null)
                fieldsState.add(state);
        }
	}

    private void compareValues(List<M3Component> updlist, Map<String, M3Component> updmap, M3ModelFieldsException modelException) {
        // For each of the children in the update list, compare field values and if same drop from update map
        updlist.forEach(ct2 -> {
            M3Component ct3 = updmap.get(ct2.getName());
            M3DaoFieldState state = isSameStringValue("description", ct2.getDescription(), ct3.getDescription(), modelException);
            if (state != null && state.same) {
            	// TODO remove from map
            } else {
                state = isSameStringValue("complexity", ct2.getComplexity().name(), ct3.getComplexity().name(), modelException);
                if (state != null && state.same) {
                    // TODO remove from map
                } else {
                    state = isSameStringValue("layer", ct2.getLayer().name(), ct3.getLayer().name(), modelException);
                    if (state != null && state.same) {
                        // TODO remove from map
                    } else {
                        state = isSameStringValue("language", ct2.getLanguage().name(), ct3.getLanguage().name(), modelException);
                        if (state != null && state.same) {
                            // TODO remove from map
                        } else {
                            state = isSameLongValue("count", ct2.getCount(), ct3.getCount(), modelException);
                            if (state != null && state.same) {
                                // TODO remove from map
                            } else {
                                state = isSameStringValue("type", ct2.getType().getName(), ct3.getType().getName(), modelException);
                                if (state != null && state.same) {
                                    // TODO remove from map
                                }
                            }
                        }
                    }
                }
            }
        });
    }

}
