package com.deep.module.graph;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

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
		if (a.getClass().isArray()) {
			try {
				Object[] c = (Object[]) a;
				for (Object o : c) {
					each(o);
				}
			} catch (Exception e) {
				double[] c = (double[]) a;
				for (double o : c) {
					each(o);
				}
			}
		} else {
			queue.add(a);
		}
	}

	private void each(int l, Object o, Object a) {
		if (a != null && a.getClass().isArray()) {
			try {
				Object[] c = (Object[]) a;
				for (int i = 0; i < c.length; i++) {
					each(i, c, c[i]);
				}
			} catch (Exception e) {
				double[] c = (double[]) a;
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
		E[] o = (E[]) new Object[1];
		fill(a, o, 0, b);
		return o[0];
	}

	private static <E> E fill(E a, E o, int l, double b) {
		if (a.getClass().isArray()) {
			if (a instanceof double[]) {
				double[] c = (double[]) a, m = c.clone();
				E[] n = (E[]) o;
				n[l] = (E) m;
				for (int i = 0; i < c.length; i++) {
					fill(c[i], m, i, b);
				}
			} else {
				E[] c = (E[]) a, m = c.clone();
				E[] n = (E[]) o;
				n[l] = (E) m;
				for (int i = 0; i < c.length; i++) {
					fill(c[i], m, i, b);
				}
			}
		} else {
			double[] e = (double[]) o;
			e[l] = b;
		}
		return o;
	}

}