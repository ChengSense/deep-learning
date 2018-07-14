package com.deep.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Each<E> {
	private List<E> list;
	private int index;

	public Each(double[] input) {
		List<Double> list = new ArrayList<Double>();
		this.list = (List<E>) list;
		for (index = 0; index < input.length; index++) {
			list.add(index, input[index]);
			each(this.list.get(index));
		}
	}
	

	public Each(double[][] input) {
		this.list = (List<E>) Arrays.asList(input);
		for (index = 0; index < list.size(); index++) {
			each(list.get(index));
		}
	}
	
	public Each(double[][][] input) {
		this.list = (List<E>) Arrays.asList(input);
		for (index = 0; index < list.size(); index++) {
			each(list.get(index));
		}
	}


	public Each(List<E> list) {
		this.list = list;
		for (index = 0; index < list.size(); index++) {
			each(list.get(index));
		}
	}

	public Each(List<E> list, String a) {
		this.list = list;
		for (index = list.size() - 1; -1 < index; index--) {
			each(list.get(index));
		}
	}

	abstract public void each(E node);

	public E next() {
		if (index < list.size() - 1)
			return list.get(index + 1);
		return null;
	}

	public E prev() {
		if (index > 0)
			return list.get(index - 1);
		return null;
	}

	public int index() {
		return index;
	}

}