package model.decisiontree.visualization;

import model.decisiontree.DecisionTree;
import model.decisiontree.node.DecisionTreeNode;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.model.Factory.*;
import static guru.nidi.graphviz.model.Factory.graph;


public class DecisionTreeGraphvizVisualizer implements IDecisionTreeVisualizer {
    private final Color rootNodeColor = Color.BROWN2;
    private final Color internalNodeColor = Color.LIGHTSKYBLUE;
    private final Color leafNodeColor = Color.PALEGREEN2;

    @Override
    public String exportDecisionTreeToPNG(DecisionTree decisionTree, String directory, String fileName) throws IOException {
        Graph graph = getGraph(decisionTree);
        File image = new File(directory + File.separator + fileName + ".png");
        Graphviz.fromGraph(graph).height(1000)
                .render(Format.PNG)
                .toFile(image);
        return image.getAbsolutePath();
    }

    private Graph getGraph(DecisionTree decisionTree) {
        DecisionTreeNode dtNode = decisionTree.getRootNode();
        return graph("decisionTree").directed()
                .nodeAttr().with(Font.name("Arial"))
                .linkAttr().with("class", "link-class")
                .with(getNodes(dtNode));
    }

    private Node getNodes(DecisionTreeNode dtNode) {
        Node graphNode = createNode(dtNode);
        if (dtNode.isLeaf())
            return graphNode.with(leafNodeColor);

        if (dtNode.getId() == 1) {
            graphNode = graphNode.with(rootNodeColor);
        } else {
            graphNode = graphNode.with(internalNodeColor);
        }
        Node leftNode = getNodes(dtNode.getLeftNode());
        Node rightNode = getNodes(dtNode.getRightNode());

        graphNode = graphNode.link(to(leftNode).with(attr("label", "Yes")));
        graphNode = graphNode.link(to(rightNode).with(attr("label", "No")));
        return graphNode;
    }

    private Node createNode(DecisionTreeNode dtNode) {
        return node(Integer.toString(dtNode.getId()))
                .with(Style.FILLED, Label.of(getNodeInfo(dtNode)));
    }

    private String getNodeInfo(DecisionTreeNode dtNode) {
        return dtNode.getSimpleRepresentation().replace("<", " <");
    }
}
