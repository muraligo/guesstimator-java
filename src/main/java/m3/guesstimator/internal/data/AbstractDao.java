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
import m3.guesstimator.model.reference.LogicCriterionOperator;

public abstract class AbstractDao {
    protected static final String _NAME_COLUMN = "NAME";
    protected static final String _DESC_COLUMN = "DESCRIPTION";
    protected static final String _VERSION_COLUMN = "VERSION";
    protected static final String _PARENT_COLUMN = "PARENT";
    protected static final Map<String, FieldMappingSpec> _fieldsMap = Collections.synchronizedMap(
            new ConcurrentHashMap<String, FieldMappingSpec>(40));
    // TODO Figure out if we can drop this
    protected static final Map<String, AbstractArtifactHelper> _helpers = Collections.synchronizedMap(
            new ConcurrentHashMap<String, AbstractArtifactHelper>(20));
    protected static final Map<String, EntityState> _entities = Collections.synchronizedMap(
            new ConcurrentHashMap<String, EntityState>(2000));
    protected static ComboPooledDataSource cpds = null;
    protected final DaoContext context;

    public AbstractDao() {
        context = DaoContext.ARTIFACT;
        initializeConnectionPooling();
    }

    public AbstractDao(DaoContext context) {
        this.context = context;
        initializeConnectionPooling();
    }

    public Object lockForUpdate(Object o) {
        ensureObjectOfType(o);
        Class<?> typ = getTypeOfObject(o);
        String name = getNameOfObject(o);
        EntityState es = _entities.putIfAbsent(name, new EntityState(EntityOp.NONE, typ, name));
        if (es == null)
            es = _entities.get(name);
        es.lockForModification();
        return o;
    }

    public EntityState getState(Object o) {
        ensureObjectOfType(o);
        Class<?> typ = getTypeOfObject(o);
        String name = getNameOfObject(o);
        EntityState es = _entities.putIfAbsent(name, new EntityState(EntityOp.NEW, typ, name));
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


    public enum DaoContext {
        ARTIFACT, 
        COMPONENT_TYPE
    }

    protected enum EntityOp {
        NONE, 
        NEW,
        UPDATE,
        DELETE
    }

    protected class EntityState {
        private AtomicReference<EntityOp> _op;
        private AtomicBoolean _deleted;
        private AtomicBoolean _lockedForMod;
        private final Class<?> _entityClass;
        private final String _entityName;

        public EntityState(EntityOp op, Class<?> entityClazz, String entityName) {
            if (op != EntityOp.NEW && op != EntityOp.NONE) {
                throw new IllegalArgumentException("Entity " + entityName + " of type " + entityClazz.getSimpleName() + " must be new or unmodified to be introduced into the cache!!!");
            }
            _entityClass = entityClazz;
            _entityName = entityName;
            _op = new AtomicReference<EntityOp>(op);
            _deleted = new AtomicBoolean(false);
            _lockedForMod = new AtomicBoolean((op == EntityOp.NEW));
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
                _op.set(EntityOp.NONE);
            }
        }

        public boolean isDeleted() {
            return _deleted.get();
        }

        public boolean isNew() {
            return _op.get() == EntityOp.NEW;
        }

        public boolean needsDeleting() {
            return _op.get() == EntityOp.DELETE;
        }

        public boolean isDirty() {
            return _op.get() != EntityOp.NONE;
        }

        public void setUpdate() {
            synchronized (this) {
                if (_lockedForMod.get()) {
                    throw new IllegalStateException("Entity " + _entityName + " of type " + _entityClass.getSimpleName() + " is already locked!!!");
                }
                _lockedForMod.set(true);
                _op.set(EntityOp.UPDATE);
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
	        // TODO Check and do something with result.mfex
		    result.artifacts.forEach(sa1 -> results.add(sa1));
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
        // TODO Check and do something with mfex
    }

    public void registerHelperForType(Class<? extends SolutionArtifact> clazz, AbstractArtifactHelper helper) {
        _helpers.putIfAbsent(clazz.getSimpleName(), helper);
    }

    public AbstractArtifactHelper getHelperForType(Class<? extends SolutionArtifact> clazz) {
        return _helpers.get(clazz.getSimpleName());
    }

    void registerFieldSpec(String fldNm, String colNm, Class<?> fldCls, FieldReferenceKind fldRefTo, 
    	    Class<?> fldRefCls, String fldRefTbl, String fldRefNm) {
        FieldMappingSpec fspec = new FieldMappingSpec();
        fspec.fieldName = fldNm;
        fspec.columnName = colNm;
        fspec.fieldClass = fldCls;
        fspec.fieldRefersTo = fldRefTo;
        fspec.targetClass = fldRefCls;
        fspec.targetTableName = fldRefTbl;
        fspec.targetField = fldRefNm;
        _fieldsMap.put(fldNm,  fspec);
    }

    FieldMappingSpec getFieldSpec(String name) {
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

    public class BasicCriteria {
        public final String fieldName;
        public final BasicCriterionOperator op;
        public final Object value;

        public BasicCriteria(String fldNm, BasicCriterionOperator opr, Object val) {
            fieldName = fldNm;
            op = opr;
            value = val;
        }
    }

    public class LogicCriteria {
        public final BasicCriteria criterion1;
        public final LogicCriterionOperator op;
        public final BasicCriteria criterion2;

        public LogicCriteria(BasicCriteria c1, LogicCriterionOperator opr, BasicCriteria c2) {
            criterion1 = c1;
            op = opr;
            criterion2 = c2;
        }

        public LogicCriteria(String fldNm, BasicCriterionOperator opr, Object val, boolean simple, boolean negate) {
        	if (!simple && !negate)
        	    throw new IllegalArgumentException("Single basic criterion should be single or negate!!!");
            criterion1 = new BasicCriteria(fldNm, opr, val);
            op = simple ? LogicCriterionOperator.ASIS : LogicCriterionOperator.NOT;
            criterion2 = null;
        }
    }

    public class CriteriaBuilder {
        public final List<LogicCriteria> criteria = new ArrayList<LogicCriteria>();

        public CriteriaBuilder add(LogicCriteria c) {
            criteria.add(c);
            return this;
        }

        public CriteriaBuilder add(BasicCriteria c1, LogicCriterionOperator opr, BasicCriteria c2) {
            LogicCriteria c = new LogicCriteria(c1, opr, c2);
            criteria.add(c);
            return this;
        }

        public CriteriaBuilder add(String fldNm, BasicCriterionOperator opr, Object val, boolean simple, boolean negate) {
            LogicCriteria c = new LogicCriteria(fldNm, opr, val, simple, negate);
            criteria.add(c);
            return this;
        }
    }
}
