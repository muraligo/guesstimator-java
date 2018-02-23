package m3.guesstimator.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isA;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import m3.guesstimator.internal.data.EstimatorComponentTypeDao;
import m3.guesstimator.model.reference.M3ComponentType;
import m3.guesstimator.model.reference.ConstructionPhase;
import m3.guesstimator.service.ApplicationContext;

@RunWith(MockitoJUnitRunner.class)
public class GuesstimatorInitializerTest {
    private static final boolean DEBUG = true;

    private GuesstimatorInitializer target;
    private HashMap<String, M3ComponentType> data = new HashMap<String, M3ComponentType>();

    @Mock
    ApplicationContext ctx;
    @Mock
    EstimatorComponentTypeDao dao;
    @Mock
    GuesstimatorApplication app;

    @Before
    public void setUp() throws Exception {
        data.clear();
        target = new GuesstimatorInitializer(app);
        target.ctx = ctx;
        doNothing().when(dao).removeAll();
        doAnswer(new Answer<M3ComponentType>() {

            @Override
            public M3ComponentType answer(InvocationOnMock invocation) throws Throwable {
                M3ComponentType ct = (M3ComponentType) invocation.getArguments()[0];
                assertNotNull(ct);
                data.put(ct.getName(), ct);
                return ct;
            }
			
        }).when(dao).put(isA(M3ComponentType.class));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInitializeComponentType() {
        try {
            target.initializeComponentType(dao);
        } catch (Exception ex) {
            printException(ex);
            fail("Exception in parsing " + ex.getMessage());
        }
//		verify(tx, times(5)).commit();
        assertEquals(89, data.size());
        M3ComponentType ct = data.get("Dev.Physical.Build.Storage");
        Long res = null;
        try {
            res = ct.getConstructCost(ConstructionPhase.Develop);
            assertEquals(25L, res.longValue());
        } catch (Exception ex) {
            printException(ex);
            fail("Exception in parsing " + ex.getMessage());
        }
        ct = data.get("Dev.AppFramework.JSE");
        try {
            res = ct.getConstructCost(ConstructionPhase.Verify);
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
