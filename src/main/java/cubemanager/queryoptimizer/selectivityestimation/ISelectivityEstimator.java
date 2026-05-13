package cubemanager.queryoptimizer.selectivityestimation;

import java.util.List;

import cubemanager.cubebase.CubeQuery;

public interface ISelectivityEstimator {

	List<SelectivityResult> estimate(CubeQuery query, int factTableSize);
}
