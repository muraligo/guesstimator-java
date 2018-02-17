package m3.guesstimator.model.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import m3.guesstimator.model.M3ModelException;

public class ComponentTypeTest {
	private static final boolean DEBUG = false;
	private static final String COMPT_NAME = "Dev.AppFramework.DataStoresSW";
	private static final String COMPT_DESC = "per application type - installation and configuration";
	private static final ComponentContext COMPT_CTX = ComponentContext.DevOps;
	private static final Layer COMPT_LAYER = Layer.Technical;
	private static final String COMPT_DATA = 
			"[ {\"phase\": \"Analysis\", \"value\": \"5\"}, {\"phase\": \"Design\", \"value\": \"10\"}, "
					+ " {\"phase\": \"Build\", \"value\": \"10\"}, {\"phase\": \"Test\", \"value\": \"5\"} ]";

    private ComponentType target = null;
    private LocalDateTime currTime = null;

    @Before
    public void setUp() throws Exception {
        currTime = LocalDateTime.now();
        target = new ComponentType(currTime);
    }

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetStrConstructCosts() {
//		System.out.println("Initial Time = " + currTime);
		target.setName(COMPT_NAME);
		target.setDescription(COMPT_DESC);
		target.setContext(COMPT_CTX);
		target.setArchitecturalLayer(COMPT_LAYER);
		assertTrue(target.isConstructParseTimeSameAs(currTime));
		assertTrue(target.isConstructUpdateTimeSameAs(currTime));
		try {
			Thread.sleep(10);
		} catch (InterruptedException ie) {
			//
		}
		target.setStrConstructCosts(COMPT_DATA);
		assertTrue(target.isConstructParseTimeSameAs(currTime));
		assertFalse(target.isConstructUpdateTimeSameAs(currTime));
//		printTimeCompare("Update", updateTime, target.constructCostUpdatedAt);
	}

	@Test
	public void testGetConstructCostOkInitially() {
		fillInFieldValues();
		Map<ConstructionPhase, Long> costs = new HashMap<ConstructionPhase, Long>();
		try {
			Thread.sleep(100);
		} catch (InterruptedException ie) {
			//
		}
		for (ConstructionPhase p : ConstructionPhase.values()) {
			Long c = target.getConstructCost(p);
			costs.put(p, c);
		}
//		printTimeCompare("Parse", parseTime, target.constructCostParsedAt);
		assertFalse(target.isConstructParseTimeSameAs(currTime));
		assertEquals(5L, costs.get(ConstructionPhase.Analysis).longValue());
		assertEquals(10L, costs.get(ConstructionPhase.Design).longValue());
		assertEquals(10L, costs.get(ConstructionPhase.Develop).longValue());
		assertEquals(5L, costs.get(ConstructionPhase.Verify).longValue());
	}

	@Test
	public void testGetConstructCostOkOnUpdate() {
		fillInFieldValues();
		Map<ConstructionPhase, Long> inicosts = new HashMap<ConstructionPhase, Long>();
		for (ConstructionPhase p : ConstructionPhase.values()) {
			Long c = target.getConstructCost(p);
			inicosts.put(p, c);
		}
		Map<ConstructionPhase, Long> modcosts = new HashMap<ConstructionPhase, Long>();
		String modData = 
				"[ {\"phase\": \"Analysis\", \"value\": 7}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 12}, {\"phase\": \"Test\", \"value\": 6} ]";
		try {
			Thread.sleep(100);
		} catch (InterruptedException ie) {
			//
		}
		target.setStrConstructCosts(modData);
		for (ConstructionPhase p : ConstructionPhase.values()) {
			Long c = target.getConstructCost(p);
			modcosts.put(p, c);
		}
		assertFalse(target.isConstructParseTimeSameAs(currTime));
		assertNotEquals(inicosts.get(ConstructionPhase.Analysis).longValue(), modcosts.get(ConstructionPhase.Analysis).longValue());
		assertEquals(inicosts.get(ConstructionPhase.Design).longValue(), modcosts.get(ConstructionPhase.Design).longValue());
		assertNotEquals(inicosts.get(ConstructionPhase.Develop).longValue(), modcosts.get(ConstructionPhase.Develop).longValue());
		assertNotEquals(inicosts.get(ConstructionPhase.Verify).longValue(), modcosts.get(ConstructionPhase.Verify).longValue());
	}

	@Test
	public void testGetConstructCostErrorOnUpdate() {
		fillInFieldValues();
		Map<ConstructionPhase, Long> inicosts = new HashMap<ConstructionPhase, Long>();
		for (ConstructionPhase p : ConstructionPhase.values()) {
			Long c = target.getConstructCost(p);
			inicosts.put(p, c);
		}
		Map<ConstructionPhase, Long> modcosts = new HashMap<ConstructionPhase, Long>();
		String modData = 
				"[ {\"phase\": \"Design\", \"value\": 8}, "
						+ " {\"phase\": \"Build\", \"value\": 12}, {\"phase\": \"QA\", \"value\": 6} ]";
		try {
			Thread.sleep(100);
		} catch (InterruptedException ie) {
			//
		}
		target.setStrConstructCosts(modData);
		try {
			for (ConstructionPhase p : ConstructionPhase.values()) {
				Long c = target.getConstructCost(p);
				modcosts.put(p, c);
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

	private void fillInFieldValues() {
		target.setName(COMPT_NAME);
		target.setDescription(COMPT_DESC);
		target.setContext(COMPT_CTX);
		target.setArchitecturalLayer(COMPT_LAYER);
		target.setStrConstructCosts(COMPT_DATA);
	}

	private static void printTimeCompare(String activity, LocalDateTime exp, LocalDateTime act) {
		if (DEBUG) {
			System.out.println(activity + "d expected = " + exp + ", actual = " + act);
		}
	}

	private static void printException(Exception ex) {
		if (DEBUG) {
			ex.printStackTrace();
		}
	}
}
