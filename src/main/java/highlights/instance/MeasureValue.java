package highlights.instance;

import cubemanager.cubebase.Measure;

/** An instance of a Measure Type: the cube {@link Measure} together with a concrete value. */
public final class MeasureValue {
    public final Measure measureType;
    public final double value;

    public MeasureValue(Measure measureType, double value) {
        this.measureType = measureType;
        this.value = value;
    }
}
