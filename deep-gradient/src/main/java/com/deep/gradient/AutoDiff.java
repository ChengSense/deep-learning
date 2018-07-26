package com.deep.gradient;

import java.util.Map;

import com.deep.gradient.diff.CorssDiff;
import com.deep.gradient.diff.SigmoidDiff;
import com.google.common.collect.Maps;

public class AutoDiff {
	private Map<String, Double> param;
	private Map<String, Double> diff;
	public Node node;

	public static void main(String[] args) {
		Map<String, Double> sparam = Maps.newHashMap();
		sparam.put("E", Math.E);
		sparam.put("x", 0.46544853667912633);
		AutoDiff sdiff = new SigmoidDiff(sparam, 1d);
		System.out.println(sdiff.diff);
		sigmoidTest();

		Map<String, Double> cparam = Maps.newHashMap();
		cparam.put("E", Math.E);
		cparam.put("y", 2d);
		cparam.put("a", 2d - Math.exp(0.46544853667912633));
		cparam.put("x", 0.46544853667912633);
		AutoDiff cdiff = new CorssDiff(cparam);
		System.out.println(cdiff.diff);
		corssTest();
	}

	public static void sigmoidTest() {
		Double value = 1 / (1 + Math.exp(-0.46544853667912633));
		System.out.println(value);
		Double value1 = value * (1 - value);
		System.out.println(value1);
	}

	public static void corssTest() {
		Double value = 2 * Math.log(Math.exp(0.46544853667912633) / 2) + (1 - 2) * Math.log(1 - Math.exp(0.46544853667912633) / 2);
		System.out.println(value);
		Double value1 = 2 - Math.exp(0.46544853667912633) / 2;
		System.out.println(value1);
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
			node.setOutput(right == null ? -leftValue : leftValue - right.getOutput());
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
		case EXP:
			leftValue = left.getOutput();
			rightValue = right.getOutput();
			node.setOutput(Math.pow(leftValue, rightValue));
			break;
		case LOG:
			leftValue = left.getOutput();
			rightValue = right.getOutput();
			node.setOutput(Math.log(rightValue) / Math.log(leftValue));
			break;
		case LN:
			leftValue = left.getOutput();
			node.setOutput(Math.log(leftValue));
			break;
		default:
			break;
		}
	}

	public void eachGrad(Node node) {
		Node left = node.left();
		if (left != null) {
			gradientLeft(node);
			eachGrad(left);
		}

		Node right = node.right();
		if (right != null) {
			gradientRight(node);
			eachGrad(right);
		}
	}

	private void gradientLeft(Node node) {
		Double leftValue, leftGrad, rightValue, gradient;
		Node left = node.left(), right = node.right();
		switch (node.getOption()) {
		case ADD:
			leftGrad = left.gradient();
			gradient = leftGrad;
			left.setGradient(gradient * node.getGradient());
			break;
		case MINUS:
			leftGrad = left.gradient();
			gradient = right == null ? -leftGrad : leftGrad;
			left.setGradient(gradient * node.getGradient());
			break;
		case MUL:
			leftGrad = left.gradient();
			rightValue = right.getOutput();
			gradient = leftGrad * rightValue;
			left.setGradient(gradient * node.getGradient());
			break;
		case DIV:
			leftGrad = left.gradient();
			rightValue = right.getOutput();
			gradient = leftGrad * rightValue / Math.pow(rightValue, 2);
			left.setGradient(gradient * node.getGradient());
			break;
		case POW:
			leftValue = left.getOutput();
			leftGrad = left.gradient();
			rightValue = right.getOutput();
			gradient = leftGrad * rightValue * Math.pow(leftValue, rightValue - 1);
			left.setGradient(gradient * node.getGradient());
			break;
		case EXP:
			leftGrad = left.gradient();
			gradient = leftGrad;
			left.setGradient(gradient * node.getGradient());
			break;
		case LOG:
			gradient = 0d;
			left.setGradient(gradient * node.getGradient());
			break;
		case LN:
			leftValue = left.getOutput();
			leftGrad = left.gradient();
			gradient = leftGrad * 1 / leftValue;
			left.setGradient(gradient * node.getGradient());
			break;
		default:
			break;
		}

		if (param.containsKey(left.getValue()) && left.getType().equals(Type.VAR)) {
			Double output = diff.get(left.getValue());
			if (output == null) {
				diff.put(left.getValue(), left.getGradient());
			} else {
				diff.put(left.getValue(), output + left.getGradient());
			}
		}
	}

	private void gradientRight(Node node) {
		Double leftValue, rightValue, rightGrad, gradient;
		Node left = node.left(), right = node.right();
		switch (node.getOption()) {
		case ADD:
			rightGrad = right.gradient();
			gradient = rightGrad;
			right.setGradient(gradient * node.getGradient());
			break;
		case MINUS:
			rightGrad = -right.gradient();
			gradient = rightGrad;
			right.setGradient(gradient * node.getGradient());
			break;
		case MUL:
			leftValue = left.getOutput();
			rightGrad = right.gradient();
			gradient = rightGrad * leftValue;
			right.setGradient(gradient * node.getGradient());
			break;
		case DIV:
			leftValue = left.getOutput();
			rightValue = right.getOutput();
			rightGrad = right.gradient();
			gradient = -rightGrad * leftValue / Math.pow(rightValue, 2);
			right.setGradient(gradient * node.getGradient());
			break;
		case POW:
			rightGrad = right.gradient();
			gradient = rightGrad;
			right.setGradient(gradient * node.getGradient());
			break;
		case EXP:
			leftValue = left.getOutput();
			rightValue = right.getOutput();
			rightGrad = right.gradient();
			gradient = rightGrad * node.getOutput() * Math.log(leftValue);
			right.setGradient(gradient * node.getGradient());
			break;
		case LOG:
			leftValue = left.getOutput();
			rightValue = right.getOutput();
			rightGrad = right.gradient();
			gradient = rightGrad * 1 / (rightValue * Math.log(leftValue));
			right.setGradient(gradient * node.getGradient());
			break;
		default:
			break;
		}

		if (param.containsKey(right.getValue()) && right.getType().equals(Type.VAR)) {
			Double output = diff.get(right.getValue());
			if (output == null) {
				diff.put(right.getValue(), right.getGradient());
			} else {
				diff.put(right.getValue(), output + right.getGradient());
			}
		}

	}
}