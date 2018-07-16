package com.deep.module.graph;

import com.deep.math.Matrix;
import com.deep.util.ReShape;

public class Shape<E> extends ReShape {

	private String name;
	private E data;
	private E diff;

	public Shape(String name) {
		this.name = name;
	}

	public Shape(String name, E data) {
		this.name = name;
		this.data = data;
	}

	public Shape(String name, E data, String random) {
		this.name = name;
		this.data = data;
		Matrix.random(data);
	}

	public E get() {
		return data;
	}

	public void set(E data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void diff(E diff) {
		this.diff = diff;
	}

	public E diff() {
		return diff;
	}

}
