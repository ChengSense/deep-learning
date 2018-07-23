package com.deep.gradient.diff;

import java.util.Map;

import com.deep.gradient.AutoDiff;
import com.deep.gradient.Function;

public class CorssDiff extends AutoDiff {

	public CorssDiff(Map<String, Double> param) {

		super(param, Function.crossLoss());

	}
}
