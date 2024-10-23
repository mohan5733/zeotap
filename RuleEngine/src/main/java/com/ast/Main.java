package com.ast;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        RuleEngine engine = new RuleEngine();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter rule 1:");
        String ruleString1 = scanner.nextLine();
        Node rule1 = engine.createRule(ruleString1);

        System.out.println("Enter rule 2:");
        String ruleString2 = scanner.nextLine();
        Node rule2 = engine.createRule(ruleString2);

        List<Node> rules = Arrays.asList(rule1, rule2);
        Node combinedRule = engine.combineRules(rules);

        Map<String, Object> userData = new HashMap<>();
        System.out.println("Enter age:");
        userData.put("age", scanner.nextInt());
        scanner.nextLine(); // Consume newline

        System.out.println("Enter department:");
        userData.put("department", scanner.nextLine());

        System.out.println("Enter salary:");
        userData.put("salary", scanner.nextInt());

        System.out.println("Enter experience:");
        userData.put("experience", scanner.nextInt());

        boolean isEligible = engine.evaluateRule(combinedRule, userData);
        System.out.println("User eligibility: " + isEligible);

        scanner.close();
    }
}
