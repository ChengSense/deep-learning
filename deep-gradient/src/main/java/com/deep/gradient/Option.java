package com.deep.gradient;

public enum Option {

	ADD, MINUS, MUL, DIV, NONE, POW, EXP, LOG, LN,

	MATMUL, ADD3, SIGMOID, CONV, RELU, MAXPOOL, SOFTMAX, REDUCE, PROD;

	public String getName() {
		return name().toLowerCase();
	}

}
