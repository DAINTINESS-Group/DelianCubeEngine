package model.decisiontree;

import model.decisiontree.labeling.RuleSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LabeledColumn extends Column {

  private final List<DecisionTree> decisionTrees = new ArrayList<>();
  private final RuleSet ruleSet;

  public LabeledColumn(
          int position,
          String newColumnName,
          String datatype,
          RuleSet ruleSet) {
    super(position, newColumnName, datatype);
    this.ruleSet = ruleSet;
  }

  public List<DecisionTree> getDecisionTrees() {
    return decisionTrees;
  }

  public RuleSet getRuleSet() {
    return ruleSet;
  }

  public void addDecisionTree(DecisionTree decisionTree) {
    decisionTrees.add(decisionTree);
  }

  @Override
  public String toString() {
    String allDecisionTrees = decisionTrees.stream()
            .map(DecisionTree::toString)
            .collect(Collectors.joining("\n"));

    return super.toString()
            + allDecisionTrees
            + "\n";
  }
}
