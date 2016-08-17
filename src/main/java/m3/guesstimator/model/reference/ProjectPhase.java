package m3.guesstimator.model.reference;

public enum ProjectPhase {
	Initiation, // provides initial analysis at - Application, Subsystem, Service levels
	Construction, // includes the ADBT phases for components; see ConstructionPhase enum
	Verification, // includes Integrated System Functional Test, Performance Test, and Security Test
	Deployment // where the application is rolled out to users
}
