package m3.guesstimator.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import m3.guesstimator.model.reference.ComponentType;
import m3.guesstimator.model.reference.ConstructionPhase;
import m3.guesstimator.service.ApplicationContext;

@RunWith(MockitoJUnitRunner.class)
public class GuesstimatorInitializerTest {
	private static final boolean DEBUG = true;

	private GuesstimatorInitializer target;
	private HashMap<String, ComponentType> data = new HashMap<String, ComponentType>();

	@Mock
	ApplicationContext ctx;
	@Mock
	EntityManager em;
	@Mock
	EntityTransaction tx;

	@Before
	public void setUp() throws Exception {
		data.clear();
		target = new GuesstimatorInitializer();
		target.ctx = ctx;
		when(ctx.getEntityManager()).thenReturn(em);
		when(em.getTransaction()).thenReturn(tx);
		doNothing().when(tx).begin();
		doNothing().when(tx).commit();
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				ComponentType ct = (ComponentType) invocation.getArguments()[0];
				assertNotNull(ct);
				data.put(ct.getName(), ct);
				return null;
			}
			
		}).when(em).persist(isA(ComponentType.class));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitializeComponentType() {
		try {
			target.initializeComponentType();
		} catch (Exception ex) {
			printException(ex);
			fail("Exception in parsing " + ex.getMessage());
		}
		verify(tx, times(5)).commit();
		assertEquals(89, data.size());
		ComponentType ct = data.get("Dev.Physical.Build.Storage");
		Long res = null;
		try {
			res = ct.getConstructCost(ConstructionPhase.Build);
			assertEquals(25L, res.longValue());
		} catch (Exception ex) {
			printException(ex);
			fail("Exception in parsing " + ex.getMessage());
		}
		ct = data.get("Dev.AppFramework.JSE");
		try {
			res = ct.getConstructCost(ConstructionPhase.Test);
			assertEquals(2L, res.longValue());
		} catch (Exception ex) {
			printException(ex);
			fail("Exception in parsing " + ex.getMessage());
		}
	}

	private static void printException(Exception ex) {
		if (DEBUG) {
			ex.printStackTrace();
		}
	}
}
