package model.decisiontree;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import model.decisiontree.node.DecisionTreeNode;
import model.decisiontree.node.FeatureType;

public class DecisionTreePath {

    private final List<String> nodes = new ArrayList<>();

    public DecisionTreePath(LinkedHashMap<DecisionTreeNode, Boolean> nodesToInvertedThreshold) {
        for (Map.Entry<DecisionTreeNode, Boolean> entry : nodesToInvertedThreshold.entrySet()) {
            this.nodes.add(getNodeInfo(entry.getKey(), entry.getValue()));
        }
    }

    public List<String> getNodesAsString() {
        return nodes;
    }

    @Override
    public String toString() {
        return String.join(" -- ", nodes);
    }

    private String getNodeInfo(DecisionTreeNode node, boolean hasRevertedThreshold) {
        if (node.isLeaf())
            return "class: " + node.getPredict().getPrediction();
        StringBuilder sb = new StringBuilder();
        sb.append(node.getSplit().getFeature());
        if (node.getSplit().getFeatureType() == FeatureType.CONTINUOUS) {
            if (hasRevertedThreshold)
                sb.append(" > ");
            else
                sb.append(" <= ");
            sb.append(node.getSplit().getThreshold());
        } else {
            if (hasRevertedThreshold)
                sb.append(" not");
            sb.append(" in ");
            sb.append("(");
            sb.append(String.join(", ", node.getSplit().getCategories()));
            sb.append(")");
        }
        return sb.toString();
    }
}
