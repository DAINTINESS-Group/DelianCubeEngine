package model.decisiontree.datapreparation;

import model.decisiontree.node.DecisionTreeNode;
import model.decisiontree.DecisionTreePath;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class DecisionTreePathsFinder {

    private final DecisionTreeNode rootNode;
    private final List<DecisionTreePath> paths = new ArrayList<>();

    public DecisionTreePathsFinder(DecisionTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public List<DecisionTreePath> getPaths() {
        if (paths.isEmpty()) {
            traverseTreeDFS(new LinkedHashMap<>(), rootNode);
        }
        return paths;
    }

    private void traverseTreeDFS(LinkedHashMap<DecisionTreeNode, Boolean> nodes, DecisionTreeNode node) {
        nodes.put(node, false);
        if (node.isLeaf()) {
            paths.add(new DecisionTreePath(nodes));
            nodes.remove(node);
            return;
        }
        traverseTreeDFS(nodes, node.getLeftNode());
        nodes.put(node, true);
        traverseTreeDFS(nodes, node.getRightNode());
        nodes.remove(node);
    }






}
