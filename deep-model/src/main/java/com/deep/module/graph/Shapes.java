package com.deep.module.graph;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

public class Shapes implements Serializable {

	protected Queue<Object> queue;

	public Object[] reshape(Object[] a, Object[] b) {
		queue = new LinkedList<Object>();
		for (Object o : a) {
			each(o);
		}
		for (Object o : b) {
			each(0, null, o);
		}
		queue = null;
		return b;
	}

	private void each(Object a) {
		if (a instanceof double[]) {
			double[] c = (double[]) a;
			for (double o : c) {
				queue.add(o);
			}
		} else {
			Object[] c = (Object[]) a;
			for (Object o : c) {
				each(o);
			}
		}
	}

	private void each(int l, Object o, Object a) {
		if (a != null && a.getClass().isArray()) {
			if (a instanceof double[]) {
				double[] c = (double[]) a;
				for (int i = 0; i < c.length; i++) {
					each(i, c, c[i]);
				}
			} else {
				Object[] c = (Object[]) a;
				for (int i = 0; i < c.length; i++) {
					each(i, c, c[i]);
				}
			}
		} else if (o != null) {
			double[] e = (double[]) o;
			e[l] = (double) queue.poll();
		}
	}

	public static <E> E fill(E a, double b) {
		Object[] o = new Object[1];
		fill(a, o, 0, b);
		return (E) o[0];
	}

	private static <E> void fill(E a, Object[] o, int l, double b) {
		if (a instanceof double[]) {
			double[] c = (double[]) a, m = c.clone();
			o[l] = m;
			IntStream.range(0, c.length).parallel().forEach(i -> {
				m[i] = b;
			});
		} else {
			E[] c = (E[]) a, m = c.clone();
			o[l] = m;
			IntStream.range(0, c.length).parallel().forEach(i -> {
				fill(c[i], m, i, b);
			});
		}
	}

	public static <E> E copy(E a) {
		Object[] o = new Object[1];
		copy(a, o, 0);
		return (E) o[0];
	}

	private static <E> void copy(E a, Object[] o, int l) {
		if (a instanceof double[]) {
			double[] c = (double[]) a, m = c.clone();
			o[l] = m;
		} else {
			E[] c = (E[]) a, m = c.clone();
			o[l] = m;
			IntStream.range(0, c.length).parallel().forEach(i -> {
				copy(c[i], m, i);
			});
		}
	}

	public static double mean(Object a) {
		double[] m = { 0, 0 };
		sum(a, m);
		return m[0] / m[1];
	}

	private static void sum(Object a, double[] m) {
		if (a instanceof double[]) {
			double[] c = (double[]) a;
			Arrays.stream(c).parallel().forEach(o -> {
				m[0] += o;
				m[1]++;
			});
		} else {
			Object[] c = (Object[]) a;
			Arrays.stream(c).parallel().forEach(o -> {
				sum(o, m);
			});
		}
	}

}