package com.deep.gradient.diff;

import java.util.Map;

import com.deep.gradient.AutoDiff;
import com.deep.gradient.Function;

public class SoftmaxDiff extends AutoDiff {

	public SoftmaxDiff(Map<String, Double> param, double diff) {

		super(param, Function.softmax(diff));

	}

}
