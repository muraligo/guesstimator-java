package m3.guesstimator.internal.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.M3ModelFieldsException.M3FieldException;
import m3.guesstimator.model.functional.AbstractSolutionArtifact;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.reference.BasicCriterionOperator;
import m3.guesstimator.model.reference.Layer;
import m3.guesstimator.model.reference.LogicCriterionOperator;

public abstract class AbstractDao {
    protected static final String _NAME_COLUMN = "NAME";
    protected static final String _DESC_COLUMN = "DESCRIPTION";
    protected static final String _VERSION_COLUMN = "VERSION";
    protected static final String _PARENT_COLUMN = "PARENT";
    protected static final Map<String, M3DaoFieldMappingSpec> _fieldsMap = Collections.synchronizedMap(
            new ConcurrentHashMap<String, M3DaoFieldMappingSpec>(40));
    protected static final Map<String, M3DaoEntityState> _entities = Collections.synchronizedMap(
            new ConcurrentHashMap<String, M3DaoEntityState>(2000));
    protected static ComboPooledDataSource cpds = null;
    protected final M3DaoContext context;

    public AbstractDao() {
        context = M3DaoContext.ARTIFACT;
        initializeConnectionPooling();
    }

    public AbstractDao(M3DaoContext context) {
        this.context = context;
        initializeConnectionPooling();
    }

    public Object lockForUpdate(Object o) {
        ensureObjectOfType(o);
        Class<?> typ = getTypeOfObject(o);
        String name = getNameOfObject(o);
        M3DaoEntityState es = _entities.putIfAbsent(name, new M3DaoEntityState(M3DaoEntityOp.NONE, typ, name));
        if (es == null)
            es = _entities.get(name);
        es.lockForModification();
        return o;
    }

    public M3DaoEntityState getState(Object o) {
        ensureObjectOfType(o);
        Class<?> typ = getTypeOfObject(o);
        String name = getNameOfObject(o);
        M3DaoEntityState es = _entities.putIfAbsent(name, new M3DaoEntityState(M3DaoEntityOp.NEW, typ, name));
        if (es == null) {
            es = _entities.get(name);
        } else {
            es.setUpdate();
        }
        return es;
    }

    /**
     * @param o the Object whose type needs to be ensured
     * @throws IllegalArgumentException if the type is not valid
     */
    abstract protected void ensureObjectOfType(Object o);

    abstract protected Class<?> getTypeOfObject(Object o);

    abstract protected String getNameOfObject(Object o);

    protected M3DaoFieldState isSameStringValue(ResultSet rs, String fldName, String colName, String value, M3ModelFieldsException modelException) {
        M3DaoFieldState state = new M3DaoFieldState();
        state.fieldName = fldName;
        state.same = true;
        try {
            String val = rs.getString(colName);
            if (val == null || rs.wasNull()) {
                state.same = (value == null);
                if (! state.same) {
                    // TODO Handle the fact that value was not updated
                }
            } else { // Neither is null
                if (value == null) {
                    state.same = false;
                    state.newValue = val;
                } else if (! value.equals(val)) {
                    // TODO How do we handle this case?
                    state.same = false;
                } else
                    state.same = true;
            }
        } catch (SQLException sqle) {
            M3FieldException ex = modelException.addException(state.fieldName, "retrieving value from DB after insert of", value, sqle);
            state.exception = ex;
        }
        return state;
    }

    protected M3DaoFieldState isSameLongValue(ResultSet rs, String fldName, String colName, Long value, M3ModelFieldsException modelException) {
        M3DaoFieldState state = new M3DaoFieldState();
        state.fieldName = fldName;
        state.same = true;
        try {
            Long val = rs.getLong(colName);
            if (val == null || rs.wasNull()) {
                state.same = (value == null);
                if (! state.same) {
                    // TODO Handle the fact that value was not updated
                }
            } else { // Neither is null
                if (value == null) {
                    state.same = false;
                    state.newValue = val;
                } else if (! value.equals(val)) {
                    // TODO How do we handle this case?
                    state.same = false;
                } else
                    state.same = true;
            }
        } catch (SQLException sqle) {
            M3FieldException ex = modelException.addException(state.fieldName, "retrieving value from DB after insert of", value, sqle);
            state.exception = ex;
        }
        return state;
    }

    protected M3DaoFieldState retrieveStringValue(ResultSet rs, String fldNm, String colNm, M3ModelFieldsException mfex) {
        M3DaoFieldState state = new M3DaoFieldState();
        state.fieldName = fldNm;
        state.same = false;
        try {
            String val = rs.getString(colNm);
            if (rs.wasNull()) {
                // TODO Handle the fact that value was not retrieved
            } else { // Not null
                state.newValue = val;
            }
        } catch (SQLException sqle) {
            M3FieldException ex = mfex.addException(state.fieldName, "retrieving value from DB on query", null, sqle);
            state.exception = ex;
        }
        return state;
    }

    protected M3DaoFieldState retrieveLongValue(ResultSet rs, String fldNm, String colNm, M3ModelFieldsException mfex) {
        M3DaoFieldState state = new M3DaoFieldState();
        state.fieldName = fldNm;
        state.same = false;
        try {
            String val = rs.getString(colNm);
            if (rs.wasNull()) {
                // TODO Handle the fact that value was not retrieved
            } else { // Not null
                Long lval = Long.valueOf(val);
                state.newValue = lval;
            }
        } catch (SQLException sqle) {
            M3FieldException ex = mfex.addException(state.fieldName, "retrieving value from DB on query", null, sqle);
            state.exception = ex;
        } catch (NumberFormatException nfe) {
            M3FieldException ex = mfex.addException(state.fieldName, "converting value from DB, invalid value", null, nfe);
            state.exception = ex;
        }
        return state;
    }

    static void retrieveLayer(M3DaoFieldState state, M3ModelFieldsException mfex) {
        if (state != null && !state.same && state.newValue != null) {
            Layer dbVal = null;
            String objval = (String) state.newValue;
            try {
                dbVal = Layer.valueOf(objval);
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

    static int getColumnCount(ResultSetMetaData rsmd, M3ModelFieldsException mfex) {
    	int col_cnt = -1;
        try {
        	col_cnt = rsmd.getColumnCount();
        } catch (SQLException sqle) {
            col_cnt = -1;
            mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData", sqle);
        }
        return col_cnt;
    }

    static String getColumnName(ResultSetMetaData rsmd, int colIx, M3ModelFieldsException mfex) {
    	String col_nm = null;
        try {
        	col_nm = rsmd.getColumnLabel(colIx);
        } catch (SQLException sqle) {
            col_nm = null;
            mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData", sqle);
        }
        return col_nm;
    }

    public enum M3DaoContext {
        ARTIFACT, 
        COMPONENT_TYPE
    }

    protected enum M3DaoEntityOp {
        NONE, 
        NEW,
        UPDATE,
        DELETE
    }

    protected class M3DaoEntityState {
        private AtomicReference<M3DaoEntityOp> _op;
        private AtomicBoolean _deleted;
        private AtomicBoolean _lockedForMod;
        private final Class<?> _entityClass;
        private final String _entityName;

        public M3DaoEntityState(M3DaoEntityOp op, Class<?> entityClazz, String entityName) {
            if (op != M3DaoEntityOp.NEW && op != M3DaoEntityOp.NONE) {
                throw new IllegalArgumentException("Entity " + entityName + " of type " + entityClazz.getSimpleName() + " must be new or unmodified to be introduced into the cache!!!");
            }
            _entityClass = entityClazz;
            _entityName = entityName;
            _op = new AtomicReference<M3DaoEntityOp>(op);
            _deleted = new AtomicBoolean(false);
            _lockedForMod = new AtomicBoolean((op == M3DaoEntityOp.NEW));
        }

        public void lockForModification() {
            if (!_lockedForMod.compareAndSet(false, true)) {
                throw new IllegalStateException("Entity " + _entityName + " of type " + _entityClass.getSimpleName() + " is already locked!!!");
            }
        }

        public void unlockAfterModification() {
            synchronized (this) {
                if (! _lockedForMod.get()) {
                    System.err.println("[WARNING] Entity " + _entityName + " of type " + _entityClass.getSimpleName() + " is already unlocked!!!");
                }
                _lockedForMod.compareAndSet(true, false);
            }
        }

        public void unlockAndReset() {
            synchronized (this) {
                if (! _lockedForMod.get()) {
                    System.err.println("[WARNING] Entity " + _entityName + " of type " + _entityClass.getSimpleName() + " is already unlocked!!!");
                }
                _lockedForMod.compareAndSet(true, false);
                _op.set(M3DaoEntityOp.NONE);
            }
        }

        public boolean isDeleted() {
            return _deleted.get();
        }

        public boolean isNew() {
            return _op.get() == M3DaoEntityOp.NEW;
        }

        public boolean needsDeleting() {
            return _op.get() == M3DaoEntityOp.DELETE;
        }

        public boolean isDirty() {
            return _op.get() != M3DaoEntityOp.NONE;
        }

        public void setUpdate() {
            synchronized (this) {
                if (_lockedForMod.get()) {
                    throw new IllegalStateException("Entity " + _entityName + " of type " + _entityClass.getSimpleName() + " is already locked!!!");
                }
                _lockedForMod.set(true);
                _op.set(M3DaoEntityOp.UPDATE);
            }
        }
    }

    protected class M3DaoHandlerResult {
        final Set<List<M3DaoFieldState>> fieldsStates = Collections.synchronizedSet(new ConcurrentSkipListSet<List<M3DaoFieldState>>());
        final Set<SolutionArtifact> artifacts = Collections.synchronizedSet(new ConcurrentSkipListSet<SolutionArtifact>());
        final M3ModelFieldsException mfex;

        M3DaoHandlerResult(M3ModelFieldsException mfe) {
            mfex = mfe;
        }

        void add(List<M3DaoFieldState> fs) {
            fieldsStates.add(fs);
        }

        void add(SolutionArtifact sa) {
            artifacts.add(sa);
        }
    }

    protected class M3DaoHandlerParam {
        final ResultSet rs;

        M3DaoHandlerParam(ResultSet rs) {
            this.rs = rs;
        }
    }

    class M3DaoFieldState {
        String fieldName;
        boolean same;
        M3FieldException exception;
        Object newValue;
    }

    protected Connection getConnection(String forModel) {
        try {
            return cpds.getConnection();
        } catch (SQLException sqle) {
            throw new M3ModelException(forModel, "getting connection", sqle);
        }
    }

    protected static void initializeConnectionPooling() {
    	if (cpds == null) {
            cpds = new ComboPooledDataSource();
            try {
                cpds.setDriverClass("org.postgresql.Driver"); //loads the jdbc driver
                cpds.setJdbcUrl("jdbc:postgresql://localhost:5432/oracloudissues");
            } catch (Exception ex) {
                throw new RuntimeException("Exception initializing PooledDataSource for PostgreSQL DB oraclecloudissues", ex);
            }
            cpds.setUser("mugopala");
            cpds.setPassword("myun1xu53r");
            // the settings below are optional -- c3p0 can work with defaults
            cpds.setMinPoolSize(5);
            cpds.setAcquireIncrement(5); 
            cpds.setMaxPoolSize(20);
            // The DataSource cpds is now a fully configured and usable pooled DataSource
    	}
    }

    protected static void terminateConnectionPool() {
        try {
            DataSources.destroy(cpds);
        } catch (SQLException sqle) {
            throw new RuntimeException("Exception terminating PooledDataSource for PostgreSQL DB oraclecloudissues", sqle);
        }
    }

    protected void performQuery(AbstractSolutionArtifact adum, String sqlstr, 
            List<SolutionArtifact> results, Function<M3DaoHandlerParam, M3DaoHandlerResult> handler) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection(adum.getClass().getSimpleName());
            stmt = conn.createStatement();
		    rs = stmt.executeQuery(sqlstr);
		    M3DaoHandlerParam param = new M3DaoHandlerParam(rs);
		    M3DaoHandlerResult result = handler.apply(param);
		    if (result.mfex != null && (result.mfex.getExceptions().length > 0 
		            || result.mfex.getCause() != null)) {
		        throw result.mfex;
		    }
            result.artifacts.forEach(sa1 -> results.add(sa1));
        } catch (SQLException ex) {
            throw new M3ModelException("querying [" + sqlstr + "]", adum.getClass().getSimpleName());
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

    void registerFieldSpec(String fldNm, String colNm, Class<?> fldCls, M3DaoFieldReferenceKind fldRefTo, 
    	    Class<?> fldRefCls, String fldRefTbl, String fldRefNm) {
        M3DaoFieldMappingSpec fspec = new M3DaoFieldMappingSpec();
        fspec.fieldName = fldNm;
        fspec.columnName = colNm;
        fspec.fieldClass = fldCls;
        fspec.fieldRefersTo = fldRefTo;
        fspec.targetClass = fldRefCls;
        fspec.targetTableName = fldRefTbl;
        fspec.targetField = fldRefNm;
        _fieldsMap.put(fldNm,  fspec);
    }

    M3DaoFieldMappingSpec getFieldSpec(String name) {
        return _fieldsMap.get(name);
    }

    protected void registerBaseFieldSpecs() {
        registerFieldSpec("name", _NAME_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("description", _DESC_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("version", _VERSION_COLUMN, String.class, null, null, null, null);
    }

    abstract protected void populateFieldSpecs();

    abstract protected void buildBasicColumnList(StringBuilder colsb);

    abstract protected void buildExtendedColumnList(StringBuilder colsb);

    public class M3DaoBasicCriteria {
        public final String fieldName;
        public final BasicCriterionOperator op;
        public final Object value;

        public M3DaoBasicCriteria(String fldNm, BasicCriterionOperator opr, Object val) {
            fieldName = fldNm;
            op = opr;
            value = val;
        }
    }

    public class M3DaoLogicCriteria {
        public final M3DaoBasicCriteria criterion1;
        public final LogicCriterionOperator op;
        public final M3DaoBasicCriteria criterion2;

        public M3DaoLogicCriteria(M3DaoBasicCriteria c1, LogicCriterionOperator opr, M3DaoBasicCriteria c2) {
            criterion1 = c1;
            op = opr;
            criterion2 = c2;
        }

        public M3DaoLogicCriteria(String fldNm, BasicCriterionOperator opr, Object val, boolean simple, boolean negate) {
        	if (!simple && !negate)
        	    throw new IllegalArgumentException("Single basic criterion should be single or negate!!!");
            criterion1 = new M3DaoBasicCriteria(fldNm, opr, val);
            op = simple ? LogicCriterionOperator.ASIS : LogicCriterionOperator.NOT;
            criterion2 = null;
        }
    }

    public class M3DaoCriteriaBuilder {
        public final List<M3DaoLogicCriteria> criteria = new ArrayList<M3DaoLogicCriteria>();

        public M3DaoCriteriaBuilder add(M3DaoLogicCriteria c) {
            criteria.add(c);
            return this;
        }

        public M3DaoCriteriaBuilder add(M3DaoBasicCriteria c1, LogicCriterionOperator opr, M3DaoBasicCriteria c2) {
            M3DaoLogicCriteria c = new M3DaoLogicCriteria(c1, opr, c2);
            criteria.add(c);
            return this;
        }

        public M3DaoCriteriaBuilder add(String fldNm, BasicCriterionOperator opr, Object val, boolean simple, boolean negate) {
            M3DaoLogicCriteria c = new M3DaoLogicCriteria(fldNm, opr, val, simple, negate);
            criteria.add(c);
            return this;
        }
    }

    protected void buildWhereClause(M3DaoCriteriaBuilder builder, StringBuilder sb) {
        sb.append("WHERE ");
        final AtomicBoolean notfirst = new AtomicBoolean(false);
        builder.criteria.forEach(lc -> {
            M3DaoBasicCriteria bc1;
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
                M3DaoBasicCriteria bc2 = lc.criterion2;
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
    }

    protected void getWhereConditionFor(String fldName, BasicCriterionOperator op, Object value, StringBuilder sb) {
        M3DaoFieldMappingSpec spec = _fieldsMap.get(fldName);
        if (spec.fieldRefersTo == M3DaoFieldReferenceKind.IdForType || spec.fieldRefersTo == M3DaoFieldReferenceKind.ForeignTable) {
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
}
