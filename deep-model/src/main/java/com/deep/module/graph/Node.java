package com.deep.module.graph;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.deep.gradient.Option;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public abstract class Node implements Serializable {

	private static final long serialVersionUID = 1L;
	public final Option option;
	private Map<String, Shape> map;

	public abstract void compute();

	public abstract void gradient();

	public Node(Option option, Shape... shapes) {
		this.option = option;
		this.map = new LinkedHashMap<String, Shape>();
		Arrays.stream(shapes).forEach(shape -> {
			map.put("output".equals(shape.getName()) ? "input" : shape.getName(), shape);
		});
		map.put("output", new Shape("output"));
	}

	public Shape get(String name) {
		return map.get(name);
	}

	public <E> void output(E data) {
		map.get("output").set(data);
	}

	public Shape output() {
		return map.get("output");
	}

	public String toString() {
		Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
		StringBuilder builder = new StringBuilder();
		builder.append("{\"name\":\"" + option.toString().toLowerCase() + "\"}").append("\n       ");
		map.forEach((key, value) -> {
			JsonObject json = gson.toJsonTree(value).getAsJsonObject();
			json.addProperty("name", key);
			builder.append(json.toString()).append("\n       ");
		});
		return builder.toString();
	}

}
