package m3.guesstimator.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import m3.guesstimator.model.reference.ConstructionPhase;

public class ModelTest {
	private static final boolean DEBUG = false;
	private static final String ALL_OK_INPUT = "[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
					+ "{\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]";
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
		Long[] myArry = new Long[4];
		Long[] expected = new Long[4];
		expected[0] = 5L;
		expected[1] = 10L;
		expected[2] = 25L;
		expected[3] = 10L;
		input = ALL_OK_INPUT;
		try {
			model.parseConstructPhaseValues("dummy", input, myArry);
			printLoopStart("parseConstructPhaseValuesAllOk");
			for (int i = 0; i < 4; i++) {
				assertEquals(expected[i], myArry[i]);
				printLoopOutput(i, myArry[i]);
			}
			printLoopEnd();
		} catch (Exception ex) {
			printException(ex);
			fail("Exception in parsing " + ex.getMessage());
		}
	}

	@Test
	public void parseConstructPhaseValuesSomeOk() {
		Long[] myArry = new Long[4];
		Long[] expected = new Long[4];
		expected[0] = null;
		expected[1] = 10L;
		expected[2] = 25L;
		expected[3] = 10L;
		input = "[ {\"phase\": \"Design\", \"value\": 10}, "
				+ "{\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]";
		try {
			model.parseConstructPhaseValues("dummy", input, myArry);
			printLoopStart("parseConstructPhaseValuesSomeOk");
			for (int i = 0; i < 4; i++) {
				assertEquals(expected[i], myArry[i]);
				printLoopOutput(i, myArry[i]);
			}
			printLoopEnd();
		} catch (Exception ex) {
			printException(ex);
			fail("Exception in parsing " + ex.getMessage());
		}
	}

	@Test
	public void parseConstructPhaseValuesSomeWithBadTag() {
		Long[] myArry = new Long[4];
		Long[] expected = new Long[4];
		expected[0] = null;
		expected[1] = 10L;
		expected[2] = 25L;
		expected[3] = 10L;
		input = "[ {\"phase\": \"Design\", \"value\": 10}, "
				+ "{\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"QA\", \"value\": 10} ]";
		try {
			model.parseConstructPhaseValues("dummy", input, myArry);
			printLoopStart("parseConstructPhaseValuesSomeWithBadTag");
			for (int i = 0; i < 4; i++) {
				assertEquals(expected[i], myArry[i]);
				printLoopOutput(i, myArry[i]);
				printLoopEnd();
			}
		} catch (Exception ex) {
			boolean expectedException = false;
			if (ex instanceof M3ModelException) {
				if (ex.getMessage().startsWith("Exception matching enum with phase in") && 
						ex.getCause() instanceof IllegalArgumentException) {
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

	class TestModel implements Model {

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

		@Override
		public List<Model> getConstituents() {
			// Auto-generated method stub
			return null;
		}
		@Override
		public void setConstituents(List<Model> value) {
			// Auto-generated method stub
		}

		@Override
		public Long getConstructPhaseEstimate(ConstructionPhase phase) {
			// Auto-generated method stub
			return null;
		}

		@Override
		public Long getEstimate() {
			// Auto-generated method stub
			return null;
		}
	}
}
