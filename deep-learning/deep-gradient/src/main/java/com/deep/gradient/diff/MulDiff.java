package com.deep.gradient.diff;

import java.util.Map;

import com.deep.gradient.AutoDiff;
import com.deep.gradient.Function;

public class MulDiff extends AutoDiff {

	public MulDiff(Map<String, Double> param, Double diff) {

		super(param, Function.mul(diff));

	}

}
