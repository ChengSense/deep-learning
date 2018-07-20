package com.deep.module.graph;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.deep.gradient.Option;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Node implements Serializable {

	private Option option;
	private Shape ouput;
	private Map<String, Shape> map;

	public abstract void compute();

	public abstract void gradient();

	public Node(Option option, Shape... shapes) {
		this.option = option;
		this.ouput = new Shape("ouput_" + String.valueOf(System.nanoTime()));
		this.map = new LinkedHashMap<String, Shape>();
		Arrays.stream(shapes).forEach(shape -> map.put(shape.getName(), shape));
	}

	public Shape get(String name) {
		return map.get(name);
	}

	public <E> void output(E data) {
		ouput.set(data);
	}

	public Shape output() {
		return ouput;
	}

	public String toString() {
		Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
		StringBuilder builder = new StringBuilder();
		builder.append("{\"name\":\"" + option.toString().toLowerCase() + "\"}").append("\n       ");
		map.forEach((key, value) -> {
			builder.append(gson.toJson(value)).append("\n       ");
		});
		builder.append(gson.toJson(ouput)).append("\n       ");
		return builder.toString();
	}

}
