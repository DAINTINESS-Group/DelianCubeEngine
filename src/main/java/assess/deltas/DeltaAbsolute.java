package assess.deltas;

import java.lang.Math;

public class DeltaAbsolute extends AbstractDeltaClass{

	@Override
	public double compare(double argument1, double argument2) {
		return Math.abs(argument1 - argument2);
	}

		
}
