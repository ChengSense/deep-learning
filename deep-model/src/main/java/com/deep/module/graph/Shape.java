package com.deep.module.graph;

import java.io.Serializable;

import com.deep.math.Matrix;

public class Shape<E> implements Serializable {

	private String name;
	private E array;
	private E diff;

	public Shape(String name) {
		this.name = name;
	}

	public Shape(String name, E array) {
		this.name = name;
		this.array = array;
	}

	public Shape(String name, E array, String random) {
		this.name = name;
		this.array = array;
		Matrix.random(array);
	}

	public E get() {
		return array;
	}

	public void set(E array) {
		this.array = array;
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
