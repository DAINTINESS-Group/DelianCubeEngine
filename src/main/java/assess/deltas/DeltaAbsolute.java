package assess.deltas;

public class DeltaAbsolute extends AbstractDeltaClass{

	@Override
	public double compare(double actual, double benchmark) {
		return Math.abs(actual - benchmark);
	}
}
