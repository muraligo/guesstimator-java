package m3.guesstimator.internal;

public class GuesstimatorApplication {

	public static void main(String[] args) {
		GuesstimatorContext ctx = new GuesstimatorContext();
		ctx.initialize();
		GuesstimatorInitializer initializer = new GuesstimatorInitializer();
		initializer.ctx = ctx;
		initializer.initializeComponentType();
	}
}
