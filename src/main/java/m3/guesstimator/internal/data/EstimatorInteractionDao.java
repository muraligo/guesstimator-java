package m3.guesstimator.internal.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import m3.guesstimator.model.ContainingSolutionArtifact;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.SolutionArtifact;
import m3.guesstimator.model.M3ModelFieldsException.M3FieldException;
import m3.guesstimator.model.functional.M3Interaction;
import m3.guesstimator.model.functional.M3Service;
import m3.guesstimator.model.functional.M3Subsystem;
import m3.guesstimator.model.reference.M3MessageActionSubtype;
import m3.guesstimator.model.reference.M3MessageSubtype;
import m3.guesstimator.model.reference.M3MessageType;

public class EstimatorInteractionDao extends AbstractDao {
    private static final Class<?> ARTIFACT_TYPE = M3Interaction.class;
    private static final Class<?> CONTAINING_ARTIFACT_TYPE = ContainingSolutionArtifact.class;
    static final String _TABLE_NAME = "INTERACTION";
    static final String _SOURCE_COLUMN = "SOURCE";
    static final String _TARGET_COLUMN = "TARGET";
    static final String _FROMKIND_COLUMN = "FROMKIND";
    static final String _TOKIND_COLUMN = "TOKIND";
    static final String _MESSAGE_COLUMN = "MESSAGE";
    static final String _API_COLUMN = "API";
    static final String _CONSTRUCTCOST_COLUMN = "CONSTRUCTCOSTS";
    static final String _VERIFYCOST_COLUMN = "VERIFYCOSTS";
    static final String _MSG_TYPE_COLUMN = "TYPE";
    static final String _MSG_SUBTYPE_COLUMN = "SUBTYPE";
    static final String _MSG_DATATYPNAME_COLUMN = "DATATYPNAME";
    static final String _MSG_DATATYPFACTOR_COLUMN = "DATATYPFACT";
    static final String _MSG_DATATYPE_COLUMN = "DATATYPE";

    public EstimatorInteractionDao() {
        super();
        populateFieldSpecs();
    }

    public M3Interaction get(String name) {
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
            M3ModelFieldsException mfex = new M3ModelFieldsException(M3Interaction.class.getSimpleName(), "collecting SQL SELECT exceptions");
	        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
            try {
                if (param.rs.first()) {
                    ResultSetMetaData rsmd = param.rs.getMetaData();
                    int colCnt = rsmd.getColumnCount();
                    List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
                    M3Interaction intn = new M3Interaction();
                    for (int colIx = 0; colIx < colCnt; colIx++) {
                        retrieveValues(param.rs, intn, rsmd.getColumnLabel(colIx), fieldsState, mfex);
                    }
                    // TODO Do something with fieldsState
                    // TODO Following should be called from parent scanning states for source or target and getting that object set here
                    retrieveDependentFields(intn, fieldsState, mfex);
                    result.add(fieldsState);
                    result.add(intn);
                }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
			return result;
        };
        performQuery(new M3Interaction(), sqlsb.toString(), perfqres, qfunc);
        // TODO Handle Exception
        return (M3Interaction)perfqres.get(0);
    }

    public List<M3Interaction> find(M3DaoCriteriaBuilder cb) {
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
            M3ModelFieldsException mfex = new M3ModelFieldsException(M3Interaction.class.getSimpleName(), "collecting SQL SELECT exceptions");
	        M3DaoHandlerResult result = new M3DaoHandlerResult(mfex);
            try {
                if (param.rs.first()) {
                    ResultSetMetaData rsmd = param.rs.getMetaData();
                    int colCnt = rsmd.getColumnCount();
                    List<M3DaoFieldState> fieldsState = new ArrayList<M3DaoFieldState>();
                    M3Interaction intn = new M3Interaction();
                    for (int colIx = 0; colIx < colCnt; colIx++) {
                        retrieveValues(param.rs, intn, rsmd.getColumnLabel(colIx), fieldsState, mfex);
                    }
                    // TODO Do something with fieldsState
                    // TODO Following should be called from parent scanning states for source or target and getting that object set here
                    retrieveDependentFields(intn, fieldsState, mfex);
                    result.add(fieldsState);
                    // TODO Do something with fieldsState
                    result.add(intn);
                    while (param.rs.next()) {
                        fieldsState = new ArrayList<M3DaoFieldState>();
                        intn = new M3Interaction();
                        for (int colIx = 0; colIx < colCnt; colIx++) {
                            retrieveValues(param.rs, intn, rsmd.getColumnLabel(colIx), fieldsState, mfex);
                        }
                        // TODO Do something with fieldsState
                        // TODO Following should be called from parent scanning states for source or target and getting that object set here
                        retrieveDependentFields(intn, fieldsState, mfex);
                        result.add(fieldsState);
                        // TODO Do something with fieldsState
                        result.add(intn);
    		        }
                }
            } catch (SQLException ex) {
                mfex.addException(null, "accessing SQL ResultSet or ResultSetMetaData");
            }
			return result;
        };
        performQuery(new M3Interaction(), sqlsb.toString(), perfqres, qfunc);
        // TODO Handle Exception
        List<M3Interaction> results = new ArrayList<M3Interaction>();
        perfqres.forEach(sa1 -> results.add((M3Interaction)sa1));
        return results;
    }

    public M3Interaction put(M3Interaction intn) {
        M3DaoEntityState es = getState(intn);
        M3Interaction resVal = null;
        if (es.isNew()) {
            resVal = addNew(intn);
        } else {
            resVal = update(intn);
        }
        es.unlockAndReset();
        return resVal;
    }

    // TODO Lock before executing update and verifying
   private M3Interaction addNew(M3Interaction intn) {
        // TODO Auto-generated method stub
        return null;
    }

   // TODO Lock before executing update and verifying
   private M3Interaction update(M3Interaction intn) {
        // TODO Auto-generated method stub
        return null;
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

    @Override
    protected void populateFieldSpecs() {
        registerBaseFieldSpecs();
        registerFieldSpec("source", _SOURCE_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("target", _TARGET_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("fromKind", _FROMKIND_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("toKind", _TOKIND_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("message", _MESSAGE_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("api", _API_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("messagetype", _MSG_TYPE_COLUMN, M3MessageType.class, M3DaoFieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("messagesubtype", _MSG_SUBTYPE_COLUMN, M3MessageSubtype.class, M3DaoFieldReferenceKind.EnumType, null, null, null);
        registerFieldSpec("msgdatatypname", _MSG_DATATYPNAME_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("msgdatatypfactor", _MSG_DATATYPFACTOR_COLUMN, Long.class, null, null, null, null);
        registerFieldSpec("strConstructCosts", _CONSTRUCTCOST_COLUMN, String.class, null, null, null, null);
        registerFieldSpec("strVerifications", _VERIFYCOST_COLUMN, String.class, null, null, null, null);
        // TODO Foll is a BLOB
        registerFieldSpec("datatype", _MSG_DATATYPE_COLUMN, String.class, null, null, null, null);
    }

    @Override
    protected void buildBasicColumnList(StringBuilder colsb) {
        colsb.append(_NAME_COLUMN);
        colsb.append(", " + _DESC_COLUMN);
        colsb.append(", " + _VERSION_COLUMN);
        colsb.append(", " + _SOURCE_COLUMN);
        colsb.append(", " + _TARGET_COLUMN);
        colsb.append(", " + _FROMKIND_COLUMN);
        colsb.append(", " + _TOKIND_COLUMN);
        colsb.append(", " + _API_COLUMN);
        colsb.append(", " + _MSG_TYPE_COLUMN);
        colsb.append(", " + _MSG_SUBTYPE_COLUMN);
        colsb.append(", " + _MSG_DATATYPNAME_COLUMN);
        colsb.append(", " + _MSG_DATATYPFACTOR_COLUMN);
        colsb.append(", " + _CONSTRUCTCOST_COLUMN);
        colsb.append(", " + _VERIFYCOST_COLUMN);
        colsb.append(", " + _MSG_DATATYPE_COLUMN);
    }

    @Override
    protected void buildExtendedColumnList(StringBuilder colsb) {
        buildBasicColumnList(colsb);
    }

    private void retrieveValues(ResultSet rs, M3Interaction intn, String colName, List<M3DaoFieldState> fieldsState, M3ModelFieldsException mfex) {
        M3DaoFieldState state = null;
        if (_DESC_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "description", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                intn.setDescription((String) state.newValue);
            }
        } else if (_SOURCE_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "source", colName, mfex);
        } else if (_TARGET_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "target", colName, mfex);
        } else if (_FROMKIND_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "fromKind", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                intn.setFromKind((String) state.newValue);
            }
        } else if (_TOKIND_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "toKind", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                intn.setToKind((String) state.newValue);
            }
        } else if (_API_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "api", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                intn.setApi((String) state.newValue);
            }
        } else if (_MSG_TYPE_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "type", colName, mfex);
            retrieveMessageType(state, mfex);
            if (state != null && !state.same && state.newValue != null) {
                intn.setMessageType((M3MessageType)state.newValue);
            }
        } else if (_MSG_SUBTYPE_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "subtype", colName, mfex);
        } else if (_MSG_DATATYPNAME_COLUMN.equals(colName)) {
            state = retrieveStringValue(rs, "datatypname", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                intn.setMessageDatatypname((String) state.newValue);
            }
        } else if (_MSG_DATATYPFACTOR_COLUMN.equals(colName)) {
            state = retrieveLongValue(rs, "datatypfactor", colName, mfex);
            if (state != null && !state.same && state.newValue != null) {
                intn.setMessageDatatypfactor((Long) state.newValue);
            }
        } else if (_CONSTRUCTCOST_COLUMN.equals(colName)) {
            state = null;
            // TODO We really do need to retrieve the value
        } else if (_VERIFYCOST_COLUMN.equals(colName)) {
            state = null;
            // TODO We really do need to retrieve the value
        } else if (_MSG_DATATYPE_COLUMN.equals(colName)) {
            // TODO state = retrieveClobValue(rs, "datatype", colName, mfex);
        } else {
            mfex.addException(colName, "not associated with any field for column");
        }
        if (state != null) {
            if (!state.same || state.exception != null)
                fieldsState.add(state);
        }
	}

	static void retrieveMessageType(M3DaoFieldState state, M3ModelFieldsException mfex) {
        if (state != null && !state.same && state.newValue != null) {
            M3MessageType dbVal = null;
            String objval = (String) state.newValue;
            try {
                dbVal = M3MessageType.valueOf(objval);
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

    private void retrieveDependentFields(M3Interaction intn, List<M3DaoFieldState> fieldsState, M3ModelFieldsException mfex) {
        Map<String, M3DaoFieldState> fldstatesmap = new HashMap<String, M3DaoFieldState>();
        fieldsState.forEach(fs -> fldstatesmap.put(fs.fieldName, fs));
        M3DaoFieldState sourcefldstate = fldstatesmap.get("source");
        M3DaoFieldState targetfldstate = fldstatesmap.get("target");
        M3DaoFieldState srckindfldstate = fldstatesmap.get("fromKind");
        M3DaoFieldState tgtkindfldstate = fldstatesmap.get("toKind");
        M3DaoFieldState subtypefldstate = fldstatesmap.get("subtype");
        if (sourcefldstate != null && sourcefldstate.exception == null && sourcefldstate.newValue != null && 
                srckindfldstate != null && srckindfldstate.exception == null && srckindfldstate.newValue != null) {
            retrieveLinkedArtifact(sourcefldstate, (String)srckindfldstate.newValue, mfex);
            intn.setSource((ContainingSolutionArtifact)sourcefldstate.newValue);
        }
        if (targetfldstate != null && targetfldstate.exception == null && targetfldstate.newValue != null && 
                tgtkindfldstate != null && tgtkindfldstate.exception == null && tgtkindfldstate.newValue != null) {
            retrieveLinkedArtifact(targetfldstate, (String)tgtkindfldstate.newValue, mfex);
            intn.setTarget((ContainingSolutionArtifact)targetfldstate.newValue);
        }
        if (subtypefldstate != null && subtypefldstate.exception == null && subtypefldstate.newValue != null) {
            if (M3MessageType.ACTION == intn.getMessageType()) {
                M3MessageActionSubtype dbVal = null;
                String objval = (String) subtypefldstate.newValue;
                try {
                    dbVal = M3MessageActionSubtype.valueOf(objval);
                } catch (Throwable t1) {
                    M3FieldException ex = mfex.addException(subtypefldstate.fieldName, "converting value from DB, invalid value", objval, t1);
                    subtypefldstate.exception = ex;
                }
                if (dbVal == null) {
                    M3FieldException ex = mfex.addException(subtypefldstate.fieldName, "converting value from DB, invalid value", objval);
                    subtypefldstate.exception = ex;
                } else {
                    subtypefldstate.newValue = dbVal;
                }
            }
            if (subtypefldstate.exception == null && subtypefldstate.newValue != null && (subtypefldstate.newValue instanceof M3MessageActionSubtype)) {
                intn.setMessageSubtype((M3MessageActionSubtype)subtypefldstate.newValue);
            }
        }
    }

    private static void retrieveLinkedArtifact(M3DaoFieldState fldstate, String kind, M3ModelFieldsException mfex) {
        ContainingSolutionArtifact csa = null;
        if (M3Subsystem.class.getSimpleName().equals(kind)) {
            // TODO use subsystem dao and do a get with fldstate.newValue as the String name
            // TODO put an if around the below to ensure the CSA is not null and of the type and there is no exception in its retrieval
            fldstate.newValue = csa;
        } else if (M3Service.class.getSimpleName().equals(kind)) {
            // TODO use subsystem dao and do a get with fldstate.newValue as the String name
            // TODO put an if around the below to ensure the CSA is not null and of the type and there is no exception in its retrieval
            fldstate.newValue = csa;
        }
    }
}
