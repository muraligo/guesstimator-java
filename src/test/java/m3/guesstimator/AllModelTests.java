package m3.guesstimator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import m3.guesstimator.internal.GuesstimatorInitializerTest;
import m3.guesstimator.model.ModelTest;
import m3.guesstimator.model.reference.ComponentTypeTest;

@RunWith(Suite.class)
@SuiteClasses({ ModelTest.class, ComponentTypeTest.class, GuesstimatorInitializerTest.class })
public class AllModelTests {

}
