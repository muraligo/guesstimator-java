package m3.guesstimator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import m3.guesstimator.internal.GuesstimatorInitializerTest;
import m3.guesstimator.model.functional.SolutionArtifactTest;
import m3.guesstimator.model.reference.ComponentTypeTest;

@RunWith(Suite.class)
@SuiteClasses({ SolutionArtifactTest.class, ComponentTypeTest.class, GuesstimatorInitializerTest.class })
public class AllModelTests {

}
