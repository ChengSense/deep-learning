package com.deep.gradient.diff;

import java.util.Map;

import com.deep.gradient.AutoDiff;
import com.deep.gradient.Function;

public class SigmoidDiff extends AutoDiff {

	public SigmoidDiff(Map<String, Double> param, Double diff) {

		super(param, Function.sigmoid(diff));

	}
}
