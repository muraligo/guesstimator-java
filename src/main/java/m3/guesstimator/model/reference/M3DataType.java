package m3.guesstimator.model.reference;

public class M3DataType implements Comparable<M3DataType> {
    private final String _name;
    private final Class<?> _data_type;
    private final long _factor;

    public M3DataType(String nm, Class<?> clz, long fac) {
        _name = nm;
        _data_type = clz;
        _factor = fac;
    }

    public String name() {
        return _name;
    }

    public Class<?> dataType() {
        return _data_type;
    }

    public long factor() {
        return _factor;
    }

    @Override
    public int compareTo(M3DataType o) {
        return _name.compareTo(o._name);
	}

    @Override
    public int hashCode() {
        return _name.hashCode();
    }

    @Override
    public String toString() {
        return "data_type:" + _name;
    }
}
