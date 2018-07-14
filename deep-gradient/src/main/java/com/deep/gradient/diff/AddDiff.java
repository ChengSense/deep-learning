package com.deep.gradient.diff;

import java.util.Map;

import com.deep.gradient.AutoDiff;
import com.deep.gradient.Function;

public class AddDiff extends AutoDiff {

	public AddDiff(Map<String, Double> param,double diff) {

		super(param, Function.add(diff));

	}
	
}
