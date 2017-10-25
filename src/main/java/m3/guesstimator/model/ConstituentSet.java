package m3.guesstimator.model;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConstituentSet extends ConcurrentSkipListMap<String, SortedSet<SolutionArtifact>> {
    private static final long serialVersionUID = 1L;

    public ConstituentSet() {
        super(Collections.synchronizedMap(new ConcurrentSkipListMap<String, SortedSet<SolutionArtifact>>()));
    }

    public ConstituentSet(List<SolutionArtifact> ls) {
        this();
        refreshElements(ls);
    }

    public void refreshElements(List<SolutionArtifact> ls) {
    	clear();
        ls.forEach(s -> {
            String key = s.getClass().getSimpleName();
            SortedSet<SolutionArtifact> sa = null;
            if (this.containsKey(key)) {
                sa = this.get(key);
            } else {
                sa = new ConcurrentSkipListSet<SolutionArtifact>();
            }
    	    sa.add(s);
        });
    }
}
