package com.deep.util;

public abstract class Feach<E> {
	private E[][] x;
	private int index = 0;

	public Feach(E[]... x) {
		this.x = x;
		for (index = 0; index < x[0].length; index++) {
			each(x[0][index], x[1][index]);
		}
	}

	public void each(E x, E y) {

	};

	public Feach(int... x) {
		for (int i = 0; i < x[0]; i++)
			for (int l = 0; l < x[1]; l++)
				for (int m = 0; m < x[2]; m++)
					for (int n = 0; n < x[3]; n++)
						each(i + m, l + n, m, n);
	}

	public void each(int x0, int x1, int x2, int x3) {

	};

	public Feach(double[][] A) {
		for (int i = 0; i < A.length; i++)
			for (int l = 0; l < A[0].length; l++)
				each(i, l);
	}

	public void each(int x0, int x1) {

	};

}
