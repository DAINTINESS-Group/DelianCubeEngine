package model.decisiontree.visualization;

import java.util.HashMap;
import java.util.Map;

public class DecisionTreeVisualizerFactory {

    private final Map<DecisionTreeVisualizerType, IDecisionTreeVisualizer> visualizers = new HashMap<>();

    public DecisionTreeVisualizerFactory() {
        createVisualizers();
    }

    private void createVisualizers() {
        visualizers.put(DecisionTreeVisualizerType.GRAPH_VIZ, new DecisionTreeGraphvizVisualizer());
    }

    public IDecisionTreeVisualizer getVisualizer(DecisionTreeVisualizerType type) {
        return visualizers.get(type);
    }
}
