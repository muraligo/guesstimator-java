package m3.guesstimator.model.reference;

/**
 * Type of the application
 * 
 * @author mugopala
 *
 */
public enum ApplicationType {
	Conceptual(1L), 
	SOASuite(20L), 
	Web(4L), 
	Spring(4L), 
	Plain(2L), 
	Database(4L), 
	AnalyticsBigData(10L), 
	AnalyticsBI(20L);

	final Long multiplier;

	private ApplicationType(Long m) {
		multiplier = m;
	}

	public Long getMultiplier() {
		return multiplier;
	}
}
