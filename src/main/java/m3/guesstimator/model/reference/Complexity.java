package m3.guesstimator.model.reference;

/**
 * Complexity of the element
 * 
 * @author mugopala
 *
 */
public enum Complexity {
	Complex(5L), 
	Medium(2L), 
	Simple(1L);

	final Long multiplier;

	private Complexity(Long m) {
		multiplier = m;
	}

	public Long getMultiplier() {
		return multiplier;
	}
}
