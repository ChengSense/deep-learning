package com.deep.gradient.diff;

import java.util.Map;

import com.deep.gradient.AutoDiff;
import com.deep.gradient.Function;

public class SmallAutoDiff extends AutoDiff {

	public SmallAutoDiff(Map<String, Double> param) {

		super(param, Function.smallLoss());

	}
}
