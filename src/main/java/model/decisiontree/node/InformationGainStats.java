package model.decisiontree.node;

import org.apache.spark.mllib.tree.model.Node;

public class InformationGainStats {
    private final DecisionTreeNodeParams nodeParams;
    private double gain;
    private double leftImpurity;
    private double rightImpurity;
    private Predict leftPredict;
    private Predict rightPredict;

    public InformationGainStats(DecisionTreeNodeParams nodeParams) {
        this.nodeParams = nodeParams;
        Node node = nodeParams.getNode();
        if (node.isLeaf())
            return;
        this.gain = node.stats().get().gain();
        this.leftImpurity = node.stats().get().leftImpurity();
        this.rightImpurity = node.stats().get().rightImpurity();
        this.leftPredict = new Predict(nodeParams, node.stats().get().leftPredict());
        this.rightPredict = new Predict(nodeParams, node.stats().get().rightPredict());
    }

    public double getGain() {
        return gain;
    }

    public double getLeftImpurity() {
        return leftImpurity;
    }

    public double getRightImpurity() {
        return rightImpurity;
    }

    public Predict getLeftPredict() {
        return leftPredict;
    }

    public Predict getRightPredict() {
        return rightPredict;
    }

    @Override
    public String toString() {
        if (nodeParams.getNode().isLeaf())
            return "Stats = None";
        return "Stats (" +
                "Gain = " + gain +
                ", Left Impurity = " + leftImpurity +
                ", Left " + leftPredict +
                ", Right Impurity = " + rightImpurity +
                ", Right " + rightPredict +
                ")";
    }
}