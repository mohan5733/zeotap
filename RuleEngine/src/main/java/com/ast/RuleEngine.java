package com.ast;

import java.util.List;
import java.util.Map;
import java.util.Stack;


public class RuleEngine {
 // Parse the rule string into an AST
 public Node createRule(String ruleString) {
     // Simplified parsing logic using a stack to handle brackets and operators
     Stack<Node> stack = new Stack<>();
     String[] tokens = ruleString.split(" ");

     for (String token : tokens) {
         if (token.equals("AND") || token.equals("OR")) {
             Node operatorNode = new Node("operator", token);
             operatorNode.setRight(stack.pop());
             operatorNode.setLeft(stack.pop());
             stack.push(operatorNode);
         } else if (token.matches("[()]+")) {
             // Ignore brackets as we assume input is correctly formatted for simplicity
             continue;
         } else {
             // Treat token as an operand
             Node operandNode = new Node("operand", token);
             stack.push(operandNode);
         }
     }

     return stack.isEmpty() ? null : stack.pop();
 }
 
 public Node combineRules(List<Node> rules) {
     if (rules == null || rules.isEmpty()) {
         return null;
     }

     Node combinedRoot = rules.get(0);
     for (int i = 1; i < rules.size(); i++) {
         Node newRoot = new Node("operator", "AND");
         newRoot.setLeft(combinedRoot);
         newRoot.setRight(rules.get(i));
         combinedRoot = newRoot;
     }
     return combinedRoot;
 }
 
 public boolean evaluateRule(Node node, Map<String, Object> data) {
	    if (node == null) return false;

	    if (node.getType().equals("operand")) {
	        String[] condition = node.getValue().split(" ");

	        // Check if the condition array has enough elements
	        if (condition.length < 3) {
	            System.out.println(" operand format: " + node.getValue());
	            return false;
	        }

	        String attribute = condition[0];
	        String operator = condition[1];
	        int value;

	        try {
	            value = Integer.parseInt(condition[2]);
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid integer value in operand: " + node.getValue());
	            return false;
	        }

	        // Handle attribute not found in data map
	        if (!data.containsKey(attribute)) {
	            System.out.println("Attribute not found in data: " + attribute);
	            return false;
	        }

	        int userValue = (int) data.get(attribute);

	        switch (operator) {
	            case ">":
	                return userValue > value;
	            case "<":
	                return userValue < value;
	            case "=":
	                return userValue == value;
	            default:
	                return false;
	        }
	    }

	    boolean leftResult = evaluateRule(node.getLeft(), data);
	    boolean rightResult = evaluateRule(node.getRight(), data);

	    if (node.getType().equals("operator")) {
	        if (node.getValue().equals("AND")) {
	            return leftResult && rightResult;
	        } else if (node.getValue().equals("OR")) {
	            return leftResult || rightResult;
	        }
	    }

	    return false;
	}
}

