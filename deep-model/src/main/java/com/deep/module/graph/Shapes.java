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

	public void each(Object a) {
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

	public void each(int l, Object o, Object a) {
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

}