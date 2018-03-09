package m3.guesstimator.model.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import m3.guesstimator.model.M3ModelException;
import m3.guesstimator.model.ParseablePrimaryCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import m3.guesstimator.model.reference.ConstructionPhase;

public class SolutionArtifactTest {
	private static final boolean DEBUG = false;
	private static final String ALL_OK_INPUT = "[ {\"name\": Analysis, \"value\": 5}, {\"name\": Design, \"value\": 10}, "
					+ "{\"name\": Build, \"value\": 25}, {\"name\": Verify, \"value\": 10} ]";
	private String input = null;
	private TestModel model = null;

	@Before
	public void setUp() throws Exception {
		model = new TestModel();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void parseConstructPhaseValuesAllOk() {
		Long[] expected = new Long[4];
		expected[0] = 5L;
		expected[1] = 10L;
		expected[2] = 25L;
		expected[3] = 10L;
		input = ALL_OK_INPUT;
		try {
            model.setStrConstructCosts(input);
			printLoopStart("parseConstructPhaseValuesAllOk");
            for (ConstructionPhase p : ConstructionPhase.values()) {
                Long actual = model.getConstructPhaseEstimate(p);
                int i = p.ordinal();
                assertEquals(expected[i], actual);
                printLoopOutput(i, actual);
            }
			printLoopEnd();
		} catch (Exception ex) {
			printException(ex);
			fail("Exception in parsing " + ex.getMessage());
		}
	}

	@Test
	public void parseConstructPhaseValuesSomeOk() {
		Long[] expected = new Long[4];
		expected[0] = 0L;
		expected[1] = 10L;
		expected[2] = 25L;
		expected[3] = 10L;
		input = "[ {\"name\": Design, \"value\": 10}, "
				+ "{\"name\": Build, \"value\": 25}, {\"name\": Verify, \"value\": 10} ]";
		try {
            model.setStrConstructCosts(input);
			printLoopStart("parseConstructPhaseValuesSomeOk");
            for (ConstructionPhase p : ConstructionPhase.values()) {
                Long actual = model.getConstructPhaseEstimate(p);
                int i = p.ordinal();
                assertEquals(expected[i], actual);
                printLoopOutput(i, actual);
            }
			printLoopEnd();
		} catch (Exception ex) {
			printException(ex);
			fail("Exception in parsing " + ex.getMessage());
		}
	}

	@Test
	public void parseConstructPhaseValuesSomeWithBadTag() {
		Long[] expected = new Long[4];
		expected[0] = 0L;
		expected[1] = 10L;
		expected[2] = 25L;
		expected[3] = 10L;
		input = "[ {\"name\": Design, \"value\": 10}, "
				+ "{\"name\": Build, \"value\": 25}, {\"name\": QA, \"value\": 10} ]";
		try {
			model.setStrConstructCosts(input);
			printLoopStart("parseConstructPhaseValuesSomeWithBadTag");
            for (ConstructionPhase p : ConstructionPhase.values()) {
                Long actual = model.getConstructPhaseEstimate(p);
                int i = p.ordinal();
                assertEquals(expected[i], actual);
                printLoopOutput(i, actual);
            }
		} catch (Exception ex) {
			boolean expectedException = false;
			if (ex instanceof M3ModelException) {
				if (ex.getMessage().startsWith("Exception matching name in")) {
					expectedException = true;
				}
			}
			if (! expectedException) {
				printException(ex);
				fail("Exception in parsing " + ex.getMessage());
			}
		}
	}

	private static void printLoopStart(String m) {
		if (DEBUG) {
			System.out.print(m + " :- ");
		}
	}

	private static void printLoopOutput(int i, Long v) {
		if (DEBUG) {
			if (i > 0) {
				System.out.print(", ");
			}
			System.out.print(i + ":" + v);
		}
	}

	private static void printLoopEnd() {
		if (DEBUG) {
			System.out.println();
		}
	}

	private static void printException(Exception ex) {
		if (DEBUG) {
			ex.printStackTrace();
		}
	}

	class TestModel extends AbstractSolutionArtifact {
		private String strConstructCosts;
		// fields below are non-persistent
		transient ParseablePrimaryCollection<ConstructionPhase, Long> constructCosts;
		private boolean constructCostComputed = false;

		public TestModel() {
			constructCosts = new ParseablePrimaryCollection<ConstructionPhase, Long>(getClass().getSimpleName(),
					"ConstructCosts", ConstructionPhase.class, Long.class, 0L);
		}

		@Override
		public String getName() {
			// Auto-generated method stub
			return null;
		}
		@Override
		public void setName(String value) {
			// Auto-generated method stub
		}

		@Override
		public String getDescription() {
			// Auto-generated method stub
			return null;
		}
		@Override
		public void setDescription(String value) {
			// Auto-generated method stub
		}

		@Override
		public String getVersion() {
			// Auto-generated method stub
			return null;
		}
		@Override
		public void setVersion(String value) {
			// Auto-generated method stub
		}

		public String getStrConstructCosts() {
			return strConstructCosts;
		}
		public void setStrConstructCosts(String value) {
			strConstructCosts = value;
			constructCosts.setStrCollection(value);
			constructCostComputed = false;
		}

		@Override
		public Long getConstructPhaseEstimate(ConstructionPhase phase) {
			return constructCosts.get(phase.ordinal());
		}

        @Override
        public Long getEstimate() {
            if (!constructCostComputed) {
                computeConstructEstimate();
			}
            return null;
        }

        private void computeConstructEstimate() {
            for (ConstructionPhase p : ConstructionPhase.values()) {
                getConstructPhaseEstimate(p);
            }
            constructCostComputed = true;
        }
	}
}
