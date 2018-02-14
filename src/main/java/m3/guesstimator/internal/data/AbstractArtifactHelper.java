package m3.guesstimator.internal.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import m3.guesstimator.internal.data.AbstractDao.M3DaoFieldState;
import m3.guesstimator.model.M3ModelFieldsException;
import m3.guesstimator.model.M3ModelFieldsException.M3FieldException;
import m3.guesstimator.model.SolutionArtifact;

public abstract class AbstractArtifactHelper {
    public void checkAndRefresh(AbstractDao dao, ResultSet rs, SolutionArtifact sa, String columnName, List<M3DaoFieldState> fieldsState, M3ModelFieldsException modelException) {
        if ("DESCRIPTION".equals(columnName)) {
            M3DaoFieldState state = isSameDescription(dao, rs, sa, modelException);
            if (!state.same || state.exception != null) {
                fieldsState.add(state);
            }
        } else if ("VERSION".equals(columnName)) {
            M3DaoFieldState state = isSameVersion(dao, rs, sa, modelException);
            if (!state.same || state.exception != null) {
                fieldsState.add(state);
            }
        } else
            checkAndRefreshSubClassField(dao, rs, sa, columnName, fieldsState, modelException);
    }

    protected M3DaoFieldState isSameDescription(AbstractDao dao, ResultSet rs, SolutionArtifact sa, M3ModelFieldsException modelException) {
        String objval = sa.getDescription();
        M3DaoFieldState state = dao.new M3DaoFieldState();
        state.fieldName = "description";
        state.same = true;
        try {
            String val = rs.getString("DESCRIPTION");
            if (val == null || rs.wasNull()) {
                state.same = (objval == null);
                if (! state.same) {
                    // TODO Handle the fact that value was not updated
                }
            } else { // Neither is null
                if (objval == null) {
                    state.same = false;
                	sa.setDescription(val);
                } else if (! objval.equals(val)) {
                    // TODO Handle the fact that values are different
                    state.same = false;
                } else
                    state.same = true;
            }
        } catch (SQLException sqle) {
            M3FieldException ex = modelException.addException(state.fieldName, "retrieving value from DB after insert of", objval, sqle);
            state.exception = ex;
        }
        return state;
    }

    protected M3DaoFieldState isSameVersion(AbstractDao dao, ResultSet rs, SolutionArtifact sa, M3ModelFieldsException modelException) {
        M3DaoFieldState state = dao.new M3DaoFieldState();
        String objval = sa.getVersion();
        state.fieldName = "version";
        state.same = true;
        try {
            String val = rs.getString("VERSION");
            if (val == null || rs.wasNull()) {
                state.same = (objval == null);
                if (! state.same) {
                    // TODO Handle the fact that value was not updated
                }
            } else { // Neither is null
                if (objval == null) {
                    state.same = false;
                	sa.setVersion(val);
                } else if (! objval.equals(val)) {
                    // TODO How do we handle this case?
                    state.same = false;
                } else
                    state.same = true;
            }
        } catch (SQLException sqle) {
            M3FieldException ex = modelException.addException(state.fieldName, "retrieving value from DB after insert of", objval, sqle);
            state.exception = ex;
        }
        return state;
    }

    abstract protected void checkAndRefreshSubClassField(AbstractDao dao, ResultSet rs, SolutionArtifact sa, String columnName, List<M3DaoFieldState> fieldsState, M3ModelFieldsException modelException);

	public SolutionArtifact retrieveValues(AbstractDao dao, ResultSet rs, SolutionArtifact sa, String columnLabel, List<M3DaoFieldState> fieldsState, M3ModelFieldsException mfex) {
		// TODO Auto-generated method stub
	    return null;
	}

	abstract protected SolutionArtifact retrieveSubclassField(AbstractDao dao, ResultSet rs, String columnLabel, List<M3DaoFieldState> fieldsState, M3ModelFieldsException mfex);
}
