package m3.guesstimator.model;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * TODO ConcurrentModificationException must be thrown in this condition
 * 
 * @author mugopala
 *
 * @param <T>
 */
public class ParseablePrimaryCollection<E extends Enum<E>, T> {
    protected T[] _collection;
    protected String strCollection;
    protected LocalDateTime parsedAt;
    protected LocalDateTime updatedAt;
    protected final String modelName;
    protected final String fieldName;
    protected final Class<E> _nameClass;
    protected final Class<T> _valueClass;
    protected final T _initialValue;
    protected final int _size;
    protected final E[] _names;
    protected final Gson _gson;

	public ParseablePrimaryCollection(String modelName, String fld, Class<E> clsNm, Class<T> clsVal, T initValue) {
        this(modelName, fld, clsNm, clsVal, initValue, LocalDateTime.now());
    }

    @SuppressWarnings("unchecked")
	public ParseablePrimaryCollection(String modelName, String fld, Class<E> clsNm, Class<T> clsVal, T initValue, LocalDateTime currTime) {
        super();
        parsedAt = currTime;
        updatedAt = currTime;
        this.modelName = modelName;
        fieldName = fld;
        _valueClass = clsVal;
        _initialValue = initValue;
        _nameClass = clsNm;
        _names = clsNm.getEnumConstants();
        _size = _names.length;
        _collection = (T[]) Array.newInstance(_valueClass, _size);
        Arrays.fill(_collection, _initialValue);
        _gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public String getStrCollection() {
        return strCollection;
    }
    public void setStrCollection(String value) {
        strCollection = value;
        updatedAt = LocalDateTime.now();
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param  index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public T get(int index) {
        if (index < 0 || index >= _size || index >= _collection.length) {
            throw new M3ModelException(modelName, fieldName, String.valueOf(index), "index in");
        }
        updateCollection();
        return _collection[index];
    }
    public T get(E name) {
        Objects.requireNonNull(name);
        return _collection[name.ordinal()];
    }

    /**
     * Performs the given action for each element of the internal array 
     * representation of the collection until all elements have been 
     * processed or the action throws an exception.
     * Unless otherwise specified by the implementing class, actions are 
     * performed in the order of iteration (if an iteration order is specified).
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @implSpec
     * <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this._collection)
     *         action.accept(t);
     * }</pre>
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     */
    final public void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        updateCollection();
        for (T t : _collection) {
            action.accept(t);
        }
    }

    /**
     * Creates a {@link Spliterator} over the elements in this collection.
     *
     * Implementations should document characteristic values reported by the
     * spliterator.  Such characteristic values are not required to be reported
     * if the spliterator reports {@link Spliterator#SIZED} and this collection
     * contains no elements.
     *
     * <p>The default implementation should be overridden by subclasses that
     * can return a more efficient spliterator.  In order to
     * preserve expected laziness behavior for the {@link #stream()} and
     * {@link #parallelStream()}} methods, spliterators should either have the
     * characteristic of {@code IMMUTABLE} or {@code CONCURRENT}, or be
     * <em><a href="Spliterator.html#binding">late-binding</a></em>.
     * If none of these is practical, the overriding class should describe the
     * spliterator's documented policy of binding and structural interference,
     * and should override the {@link #stream()} and {@link #parallelStream()}
     * methods to create streams using a {@code Supplier} of the spliterator,
     * as in:
     * <pre>{@code
     *     Stream<E> s = StreamSupport.stream(() -> spliterator(), spliteratorCharacteristics)
     * }</pre>
     * <p>These requirements ensure that streams produced by the
     * {@link #stream()} and {@link #parallelStream()} methods will reflect the
     * contents of the collection as of initiation of the terminal stream
     * operation.
     *
     * @implSpec
     * The default implementation creates a
     * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
     * from the collections's {@code Iterator}.  The spliterator inherits the
     * <em>fail-fast</em> properties of the collection's iterator.
     * <p>
     * The created {@code Spliterator} reports {@link Spliterator#SIZED}.
     *
     * @implNote
     * The created {@code Spliterator} additionally reports
     * {@link Spliterator#SUBSIZED}.
     *
     * <p>If a spliterator covers no elements then the reporting of additional
     * characteristic values, beyond that of {@code SIZED} and {@code SUBSIZED},
     * does not aid clients to control, specialize or simplify computation.
     * However, this does enable shared use of an immutable and empty
     * spliterator instance (see {@link Spliterators#emptySpliterator()}) for
     * empty collections, and enables clients to determine if such a spliterator
     * covers no elements.
     *
     * @return a {@code Spliterator} over the elements in this collection
     */
    public Spliterator<T> spliterator() {
        return Spliterators.spliterator(_collection, (Spliterator.IMMUTABLE | Spliterator.ORDERED));
    }

    /**
     * Returns a sequential {@code Stream} with this collection as its source.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @implSpec
     * The default implementation creates a sequential {@code Stream} from the
     * collection's {@code Spliterator}.
     *
     * @return a sequential {@code Stream} over the elements in this collection
     */
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns a possibly parallel {@code Stream} with this collection as its
     * source.  It is allowable for this method to return a sequential stream.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @implSpec
     * The default implementation creates a parallel {@code Stream} from the
     * collection's {@code Spliterator}.
     *
     * @return a possibly parallel {@code Stream} over the elements in this
     * collection
     */
    public Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    private void updateCollection() {
        if (parsedAt == null || updatedAt.isAfter(parsedAt)) {
            try {
                parseNameValues();
            } catch (M3JsonException|M3ModelException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public class NameValue {
        private String _name;
        private String _value;

        public String getName() {
            return _name;
        }
        public void setName(String nm) {
            _name = nm;
        }

        public Object getValue() {
            return _value;
        }
        public void setValue(String val) {
            _value = val;
        }
    }

    private void parseNameValues() {
        Type collectionType = new TypeToken<List<List<NameValue>>>(){}.getType();
	    List<List<NameValue>> l = null;
	    try {
	        l = _gson.fromJson(strCollection, collectionType);
	    } catch (JsonSyntaxException e) {
	        l = null;
	        throw new M3ModelException(modelName, fieldName, strCollection, "parsing Json from", e);
		}
	    l.forEach(nv -> {
	    	@SuppressWarnings("unchecked")
			NameValue nvt = (NameValue)nv;
	        int ix = nameCheck(nvt.getName());
	        // Assume valid index; exception already thrown if not found
	        _collection[ix] = checkAndGetValue(nvt.getValue());
	    });
    }

    @SuppressWarnings("unchecked")
	private T checkAndGetValue(Object val) {
        if (_valueClass.isAssignableFrom(val.getClass())) {
            return (T)val;
        }
        throw new IllegalArgumentException("Value [" + val + "] is not of type [" + _valueClass.getSimpleName() + "]");
    }

    /**
     * Checks if the given name is registered.
     * Returns the index if it is, or Exception if not.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(nm==null&nbsp;?&nbsp;names[i]==null&nbsp;:&nbsp;nm.equals(names[i]))</tt>,
     * or Exception if there is no such index.
     */
    private int nameCheck(String nm) {
        if (nm != null) {
        	E enumForName = null;
            for (int i = 0; i < _size; i++)
                if (nm.equals(_names[i])) {
                    enumForName = _names[i];
                }
            if (enumForName != null)
                return enumForName.ordinal();
        }
        throw new M3ModelException(modelName, fieldName, nm, "matching name in");
    }

    public boolean isParsedBefore(LocalDateTime currTime) {
	    return parsedAt.isBefore(currTime);
    }

    public boolean isParsedAfter(LocalDateTime currTime) {
	    return parsedAt.isAfter(currTime);
    }

    public boolean isUpdatedBefore(LocalDateTime currTime) {
	    return updatedAt.isBefore(currTime);
    }

    public boolean isUpdatedAfter(LocalDateTime currTime) {
	    return updatedAt.isAfter(currTime);
    }

}
