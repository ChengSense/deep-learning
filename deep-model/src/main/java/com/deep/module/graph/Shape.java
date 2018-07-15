package com.deep.module.graph;

import com.deep.util.ReShape;

public class Shape<E> extends ReShape {

	transient public String init;

	public Shape(String name) {
		this.name = name;
	}

	public Shape(String name, E[] array) {
		this.name = name;
		this.array = array;
	}

	public Shape(String name, E[] array, String init) {
		this.name = name;
		this.array = array;
		this.init = init;
	}

	public E[] get() {
		return array;
	}

	public void set(E array) {
		this.array = (E[]) array;
	}

	private String name;
	private E[] array;
	private E[] diff;

	public String getName() {
		return name;
	}

	public void diff(E[] diff) {
		this.diff = diff;
	}

	public E[] diff() {
		return diff;
	}

}
