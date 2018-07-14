package com.deep.gradient.diff;

import java.util.Map;

import com.deep.gradient.AutoDiff;
import com.deep.gradient.Function;

public class MeanDiff extends AutoDiff {

	public MeanDiff(Map<String, Double> param) {

		super(param, Function.smallLoss());

	}
}
