package com.ast;

public class Node {
 private String type; 
 private Node left;   
 private Node right;  
 private String value; // Value for operands (e.g., "age > 30")

 // Constructor
 public Node(String type, String value) {
     this.type = type;
     this.value = value;
 }

 // Getters and Setters
 public String getType() {
     return type;
 }

 public void setType(String type) {
     this.type = type;
 }

 public Node getLeft() {
     return left;
 }

 public void setLeft(Node left) {
     this.left = left;
 }

 public Node getRight() {
     return right;
 }

 public void setRight(Node right) {
     this.right = right;
 }

 public String getValue() {
     return value;
 }

 public void setValue(String value) {
     this.value = value;
 }
}

