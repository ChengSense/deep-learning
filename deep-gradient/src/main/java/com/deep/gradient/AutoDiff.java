package com.deep.gradient;

import java.util.Map;

import com.deep.gradient.diff.CorssDiff;
import com.deep.gradient.diff.SoftmaxDiff;
import com.google.common.collect.Maps;

public class AutoDiff {
	private Map<String, Double> param;
	private Map<String, Double> diff;
	public Node node;

	public static void main(String[] args) {
		// Map<String, Double> sigmoid = Maps.newHashMap();
		// sigmoid.put("E", Math.E);
		// sigmoid.put("x", 0.46544853667912633);
		// AutoDiff sigmoidDiff = new SigmoidDiff(sigmoid, 1d);
		// System.out.println(sigmoidDiff.getDiff("x"));
		// sigmoidTest();

		Map<String, Double> corss = Maps.newHashMap();
		corss.put("E", Math.E);
		corss.put("y", 2d);
		corss.put("a", 2d - Math.exp(0.46544853667912633));
		corss.put("x", 0.46544853667912633);
		AutoDiff corssDiff = new CorssDiff(corss);
		System.out.println(corssDiff.getDiff("x"));
		crossSoftmaxTest();

		// Map<String, Double> soft = Maps.newHashMap();
		// soft.put("E", Math.E);
		// soft.put("a", 2d - Math.pow(Math.E, 0.46544853667912633));
		// soft.put("x", 0.46544853667912633);
		// AutoDiff softDiff = new SoftmaxDiff(soft, corssDiff.getDiff("x"));
		// System.out.println(softDiff.getDiff("x"));
		// crossSoftmaxTest(corssDiff.getDiff("x"));
	}

	public static void sigmoidTest() {
		Double value = 1 / (1 + Math.pow(Math.E, -0.46544853667912633));
		System.out.println(value);
		Double value1 = value * (1 - value);
		System.out.println(value1);
	}

	public static void crossTest() {
		Double value = 2 / (Math.pow(Math.E, 0.46544853667912633) / 2d);
		System.out.println(value);
	}

	public static void crossSoftmaxTest() {
		Double x = Math.pow(Math.E, 0.46544853667912633) / 2;
		Double value = x - 2;
		System.out.println(value);
	}

	public Double getDiff(String key) {
		return diff.get(key);
	}

	public AutoDiff(Map<String, Double> param, Node node) {
		this.diff = Maps.newHashMap();
		this.param = param;
		this.node = node;
		eachOpt(node);
		eachGrad(node);
	}

	public void eachOpt(Node node) {
		Node left = node.left();
		if (left != null) {
			eachOpt(left);
			left.value(param);
		}

		Node right = node.right();
		if (right != null) {
			eachOpt(right);
			right.value(param);
		}

		if (left == null && right == null)
			return;

		option(node, left, right);
	}

	private void option(Node node, Node left, Node right) {
		Double leftValue, rightValue;
		switch (node.getOption()) {
		case ADD:
			leftValue = left.getOutput();
			rightValue = right.getOutput();
			node.setOutput(leftValue + rightValue);
			break;
		case MINUS:
			leftValue = left.getOutput();
			rightValue = right == null ? 0 : right.getOutput();
			node.setOutput(leftValue - rightValue);
			break;
		case MUL:
			leftValue = left.getOutput();
			rightValue = right.getOutput();
			node.setOutput(leftValue * rightValue);
			break;
		case DIV:
			leftValue = left.getOutput();
			rightValue = right.getOutput();
			node.setOutput(leftValue / rightValue);
			break;
		case POW:
			leftValue = left.getOutput();
			rightValue = right.getOutput();
			node.setOutput(Math.pow(leftValue, rightValue));
			break;
		case LOG:
			leftValue = left.getOutput();
			rightValue = right.getOutput();
			node.setOutput(Math.log(rightValue) / Math.log(leftValue));
			break;
		default:
			break;
		}
	}

	public void eachGrad(Node node) {
		Node left = node.left();
		if (left != null) {
			gradientLeft(left, node);
			eachGrad(left);
		}

		Node right = node.right();
		if (right != null) {
			gradientRight(right, node);
			eachGrad(right);
		}
	}

	private void gradientLeft(Node child, Node node) {
		Double leftValue, leftGrad, rightValue, rightGrad, gradient;
		Node left = node.left(), right = node.right();
		switch (node.getOption()) {
		case ADD:
			leftGrad = left.gradient();
			rightGrad = 0d;
			gradient = leftGrad + rightGrad;
			child.setGradient(gradient * node.getGradient());
			break;
		case MINUS:
			leftGrad = left.gradient();
			rightGrad = 0d;
			gradient = leftGrad - rightGrad;
			child.setGradient(gradient * node.getGradient());
			break;
		case MUL:
			leftValue = left.getOutput();
			leftGrad = left.gradient();
			rightValue = right.getOutput();
			rightGrad = 0d;
			gradient = leftGrad * rightValue + rightGrad * leftValue;
			child.setGradient(gradient * node.getGradient());
			break;
		case DIV:
			leftValue = left.getOutput();
			leftGrad = left.gradient();
			rightValue = right.getOutput();
			rightGrad = 0d;
			gradient = (leftGrad * rightValue - rightGrad * leftValue) / Math.pow(rightValue, 2);
			child.setGradient(gradient * node.getGradient());
			break;
		case POW:
			leftValue = left.getOutput();
			leftGrad = left.gradient();
			rightValue = right.getOutput();
			rightGrad = 0d;
			gradient = rightValue * Math.pow(leftValue, rightValue - 1);
			child.setGradient(gradient * node.getGradient());
			break;
		case LOG:
			leftValue = left.getOutput();
			leftGrad = left.gradient();
			rightValue = right.getOutput();
			rightGrad = 0d;
			gradient = 0d;
			child.setGradient(gradient * node.getGradient());
			break;
		default:
			break;
		}

		if (param.containsKey(child.getValue())) {
			Double output = diff.get(child.getValue());
			if (output == null) {
				diff.put(child.getValue(), child.getGradient());
			} else {
				diff.put(child.getValue(), output + child.getGradient());
			}
		}
	}

	private void gradientRight(Node child, Node node) {
		Double leftValue, leftGrad, rightValue, rightGrad, gradient;
		Node left = node.left(), right = node.right();
		switch (node.getOption()) {
		case ADD:
			leftGrad = 0d;
			rightGrad = right.gradient();
			gradient = leftGrad + rightGrad;
			child.setGradient(gradient * node.getGradient());
			break;
		case MINUS:
			leftGrad = 0d;
			rightGrad = right.gradient();
			gradient = leftGrad - rightGrad;
			child.setGradient(gradient * node.getGradient());
			break;
		case MUL:
			leftValue = left.getOutput();
			leftGrad = 0d;
			rightValue = right.getOutput();
			rightGrad = right.gradient();
			gradient = leftGrad * rightValue + rightGrad * leftValue;
			child.setGradient(gradient * node.getGradient());
			break;
		case DIV:
			leftValue = left.getOutput();
			leftGrad = 0d;
			rightValue = right.getOutput();
			rightGrad = right.gradient();
			gradient = (leftGrad * rightValue - rightGrad * leftValue) / Math.pow(rightValue, 2);
			child.setGradient(gradient * node.getGradient());
			break;
		case POW:
			leftValue = left.getOutput();
			leftGrad = 0d;
			rightValue = right.getOutput();
			rightGrad = right.gradient();
			gradient = Math.pow(leftValue, rightValue) * rightGrad;
			child.setGradient(gradient * node.getGradient());
			break;
		case LOG:
			leftValue = left.getOutput();
			leftGrad = 0d;
			rightValue = right.getOutput();
			rightGrad = right.gradient();
			gradient = 1 / (rightValue * Math.log(leftValue));
			child.setGradient(gradient * node.getGradient());
			break;
		default:
			break;
		}

		if (param.containsKey(child.getValue())) {
			Double output = diff.get(child.getValue());
			if (output == null) {
				diff.put(child.getValue(), child.getGradient());
			} else {
				diff.put(child.getValue(), output + child.getGradient());
			}
		}

	}
}
