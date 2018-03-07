package m3.guesstimator.internal.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.SolutionArtifact;

public class EstimatorResponse<E extends SolutionArtifact> {
    private List<E> _resultData;
    private M3ModelException _exception;
    private int _status;
    private boolean _isSingleResult = false;

    public EstimatorResponse(List<E> data) {
        _resultData = Collections.unmodifiableList(data);
        _status = 200;
    }

    public EstimatorResponse(E data) {
        if (_resultData == null) {
            _resultData = new ArrayList<E>(1);
        }
        _resultData.add(data);
        _isSingleResult = true;
        _status = 200;
    }

    public EstimatorResponse(M3ModelException ex, int status) {
        _exception = ex;
        _status = status;
    }

    public int status() {
        return _status;
    }

    public boolean isSingle() {
        return _isSingleResult;
    }

    public List<E> model() {
        return _resultData;
    }

    public String error() {
        String res = null;
        // TODO convert exception to error text
        return res;
    }
}
