package com.deep.util;

public class ReShape {

	public static double[] reshape(double[][] A, double[] b) {
		for (int i = 0; i < b.length; i++) {
			b[i] = A[i][0];
		}
		return b;
	}

	public static double[][] reshape(double[][][] A, double[][] B) {
		for (int i = 0; i < B.length; i++) {
			for (int l = 0; l < B[0].length; l++) {
				B[i][l] = A[i][l][0];
			}
		}
		return B;
	}

	public static double[][] reshape(double[] a, double[][] B) {
		for (int i = 0; i < B.length; i++) {
			for (int l = 0; l < B[0].length; l++) {
				B[i][0] = a[i];
			}
		}
		return B;
	}

	public static double[][][] reshape(double[][] A, double[][][] B) {
		for (int i = 0; i < B.length; i++) {
			for (int l = 0; l < B[0].length; l++) {
				B[i][l][0] = A[i][l];
			}
		}
		return B;
	}
}