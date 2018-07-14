package com.deep.module.graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.deep.gradient.Option;

public abstract class Node {

	Option option;

	private Shape ouput;

	Map<String, Shape> map;

	public Node(Option option, Shape... shapes) {

		this.option = option;

		this.ouput = new Shape("ouput");

		this.map = new HashMap<String, Shape>();

		Arrays.stream(shapes).forEach(shape -> {

			map.put(shape.getName(), shape);

		});

	}

	public abstract void compute();

	public abstract void gradient();

	public Shape get(String name) {

		return map.get(name);

	}

	public <E> void output(E[] data) {

		ouput.set(data);

	}

	public Shape output() {

		return ouput;

	}
}
