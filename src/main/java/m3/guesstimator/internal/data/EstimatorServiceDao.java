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
import m3.guesstimator.model.functional.M3Service;

public class EstimatorServiceDao extends AbstractDao {
    private static final Class<?> ARTIFACT_TYPE = ContainingSolutionArtifact.class;
    private static final String _TABLE_NAME = "SUBSYSTEM";
    private static final String _OVERHEAD_COLUMN = "OVERHEAD";
    private static final String _COMPOSITION_COLUMN = "COMPOSITION";
    private static final String _NFR_BUILD_COLUMN = "NFR_BUILD";
    private static final String _VERIFICATION_COLUMN = "VERIFICATION";

    public EstimatorServiceDao() {
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

    public M3Service get(String name) {
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
            M3ModelFieldsException mfex = new M3ModelFieldsException(M3Service.class.getSimpleName(), "collecting SQL SELECT exceptions");
            M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
            try {
                if (param.rs.first()) {
                    ResultSetMetaData rsmd = param.rs.getMetaData();
                    int colCnt = rsmd.getColumnCount();
                    List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
                    M3Service subsys = new M3Service();
                    for (int colIx = 0; colIx < colCnt; colIx++) {
                        retrieveValues(param.rs, subsys, rsmd.getColumnLabel(colIx), fieldsState, mfex);
                    }
                    // TODO Do something with fieldsState
                    result.add(fieldsState);
                    result.add(subsys);
                }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
            return result;
        };
        performQuery(new M3Service(), sqlsb.toString(), perfqres, qfunc);
        // TODO Handle exceptions
        return (M3Service)perfqres.get(0);
    }

    public List<M3Service> find(M3DaoCriteriaBuilder cb) {
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
            M3ModelFieldsException mfex = new M3ModelFieldsException(M3Service.class.getSimpleName(), "collecting SQL SELECT exceptions");
            M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
            try {
                if (param.rs.first()) {
                    ResultSetMetaData rsmd = param.rs.getMetaData();
                    int colCnt = rsmd.getColumnCount();
                    List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
                    M3Service subsys = new M3Service();
                    for (int colIx = 0; colIx < colCnt; colIx++) {
                        retrieveValues(param.rs, subsys, rsmd.getColumnLabel(colIx), fieldsState, mfex);
                    }
                    // TODO Do something with fieldsState
                    result.add(fieldsState);
                    result.add(subsys);
                    while (param.rs.next()) {
                        fieldsState = new ArrayList<M3DaoFieldState>();
                        subsys = new M3Service();
                        for (int colIx = 0; colIx < colCnt; colIx++) {
                            retrieveValues(param.rs, subsys, rsmd.getColumnLabel(colIx), fieldsState, mfex);
                        }
                        // TODO Do something with fieldsState
                        result.add(fieldsState);
                        result.add(subsys);
                    }
                }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
            return result;
        };
        performQuery(new M3Service(), sqlsb.toString(), perfqres, qfunc);
        // TODO Handle exceptions
        List<M3Service> results = new ArrayList<M3Service>();
        perfqres.forEach(sa1 -> results.add((M3Service)sa1));
        return results;
    }

    public M3Service put(M3Service csa) {
        M3DaoEntityState es = getState(csa);
        // TODO What about parent - child hierarchy checks?
        // TODO Build the JDBC
        // TODO Get a Connection from the Pool
        // TODO Execute the JDBC
        // TODO Retrieve the object
        // TODO Reset state.op to NONE and unlock
        M3Service resVal = null;
        if (es.isNew()) {
            resVal = addNew(csa);
        } else {
            resVal = update(csa);
        }
        es.unlockAndReset();
        return resVal;
    }

    protected M3Service addNew(M3Service subsys) {
        String saname = subsys.getName();
        StringBuilder valuesb = new StringBuilder();
        StringBuilder colsb = new StringBuilder();
        buildBasicColumnList(colsb);
        valuesb.append("'" + saname + "'");
        valuesb.append(", '" + subsys.getDescription() + "'");
        valuesb.append(", '" + subsys.getVersion() + "'");
        valuesb.append(", '" + subsys.getStrConstructionOverheads() + "'");
        valuesb.append(", '" + subsys.getStrCompositionFactors() + "'");
        valuesb.append(", '" + subsys.getStrNfrBuildFactors() + "'");
        valuesb.append(", '" + subsys.getStrVerifications() + "'");
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
        M3ModelFieldsException mfex = new M3ModelFieldsException(subsys.getClass().getSimpleName(), "collecting SQL INSERT exceptions");
        try {
            conn = getConnection(subsys.getClass().getSimpleName());
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
                            checkAndRefresh(param.rs, subsys, rsmd.getColumnLabel(colIx), fieldsState, mfex);
                        }
                        // TODO Do something with fieldsState
                        result.add(fieldsState);
                        result.add(subsys);
                    }
                } catch (SQLException ex) {
                    mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData", ex);
                }
                return result;
            };
            performQuery(subsys, sqlsb.toString(), results, qfunc);
        }
        // TODO For each of the children retrieve the child, compare field values and if different add to list for updates and if new add to list for adds
        // TODO For the adds use child dao to insert each
        // TODO For the updates use child dao to update each
        // TODO Check and do something with mfex
        return subsys;
    }

    private M3Service update(M3Service subsys) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void populateFieldSpecs() {
        registerBaseFieldSpecs();
        registerFieldSpec("strConstructionOverheads", _OVERHEAD_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("strCompositionFactors", _COMPOSITION_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("strNfrBuildFactors", _NFR_BUILD_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("strVerifications", _VERIFICATION_COLUMN, String.class, null, null, null, null);
    }

    @Override
    protected void buildBasicColumnList(StringBuilder colsb) {
        colsb.append(_NAME_COLUMN);
        colsb.append(", " + _DESC_COLUMN);
        colsb.append(", " + _VERSION_COLUMN);
        colsb.append(", " + _OVERHEAD_COLUMN);
        colsb.append(", " + _COMPOSITION_COLUMN);
        colsb.append(", " + _NFR_BUILD_COLUMN);
        colsb.append(", " + _VERIFICATION_COLUMN);
    }

    @Override
    protected void buildExtendedColumnList(StringBuilder colsb) {
        buildBasicColumnList(colsb);
    }

    private void checkAndRefresh(ResultSet rs, M3Service subsys, String colName, List<M3DaoFieldState> fieldsState, M3ModelFieldsException modelException) {
        M3DaoFieldState state = null;
        if (_DESC_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "description", colName, subsys.getDescription(), modelException);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setDescription((String) state.newValue);
            }
        } else if (_VERSION_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "version", colName, subsys.getVersion(), modelException);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setVersion((String) state.newValue);
            }
        } else if (_OVERHEAD_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "strConstructionOverheads", colName, subsys.getStrConstructionOverheads(), modelException);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setStrConstructionOverheads((String) state.newValue);
            }
        } else if (_COMPOSITION_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "strCompositionFactors", colName, subsys.getStrCompositionFactors(), modelException);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setStrCompositionFactors((String) state.newValue);
            }
        } else if (_NFR_BUILD_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "strNfrBuildFactors", colName, subsys.getStrNfrBuildFactors(), modelException);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setStrNfrBuildFactors((String) state.newValue);
            }
        } else if (_VERIFICATION_COLUMN.equals(colName)) {
            state = isSameStringValue(rs, "strVerifications", colName, subsys.getStrVerifications(), modelException);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setStrVerifications((String) state.newValue);
            }
        } else {
            modelException.addException(colName, "not associated with any field for column");
        }
        if (state != null) {
            if (!state.same || state.exception != null)
                fieldsState.add(state);
        }
    }

    private void retrieveValues(ResultSet rs, M3Service subsys, String colName, List<M3DaoFieldState> fieldsState, M3ModelFieldsException mfex) {
        M3DaoFieldState state = null;
        if (_NAME_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "name", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setName((String) state.newValue);
            }
        } else if (_DESC_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "description", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setDescription((String) state.newValue);
            }
        } else if (_VERSION_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "version", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setVersion((String) state.newValue);
            }
        } else if (_OVERHEAD_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "strConstructionOverheads", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setStrConstructionOverheads((String) state.newValue);
            }
        } else if (_COMPOSITION_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "strCompositionFactors", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setStrCompositionFactors((String) state.newValue);
            }
        } else if (_NFR_BUILD_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "strNfrBuildFactors", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setStrNfrBuildFactors((String) state.newValue);
            }
        } else if (_VERIFICATION_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "strVerifications", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                subsys.setStrVerifications((String) state.newValue);
            }
        } else {
            mfex.addException(colName, "not associated with any field for column");
        }
        if (state != null) {
            if (!state.same || state.exception != null)
                fieldsState.add(state);
        }
    }

}