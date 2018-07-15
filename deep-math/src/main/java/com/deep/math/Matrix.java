package com.deep.math;

import java.math.BigDecimal;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Matrix {
	static Logger log = Logger.getLogger(Matrix.class);

	public static double[] random(double[] a) {
		RandomDataGenerator random = new RandomDataGenerator();
		for (int i = 0; i < a.length; i++)
			a[i] = random.nextGaussian(0, 1);
		return a;
	}

	public static double[][] random(double[][] A) {
		RandomDataGenerator random = new RandomDataGenerator();
		for (int i = 0; i < A.length; i++)
			for (int l = 0; l < A[i].length; l++)
				A[i][l] = random.nextGaussian(0, 1);
		return A;
	}

	public static double[][][] random(double[][][] A) {
		for (int i = 0; i < A.length; i++) {
			A[i] = random(A[i]);
			A[i] = div(A[i], A.length * A[0].length * A[0][0].length);
		}
		return A;
	}

	public static double[] fill(double[] a, double b) {
		double[] c = new double[a.length];
		for (int i = 0; i < a.length; i++)
			c[i] = b;
		return c;
	}

	public static double[][] fill(double[][] A, double b) {
		double[][] C = new double[A.length][A[0].length];
		for (int i = 0; i < A.length; i++)
			C[i] = fill(A[i], b);
		return C;
	}

	public static double[][][] fill(double[][][] A, double b) {
		double[][][] C = new double[A.length][A[0].length][A[0][0].length];
		for (int i = 0; i < A.length; i++)
			C[i] = fill(A[i], b);
		return C;
	}

	public static double sum(double[][] A) {
		double c = 0;
		for (int i = 0; i < A.length; i++)
			for (int l = 0; l < A[i].length; l++)
				c += A[i][l];
		return c;
	}

	public static double[][] div(double[][] A, double b) {
		double[][] C = new double[A.length][A[0].length];
		for (int i = 0; i < A.length; i++)
			for (int l = 0; l < A[0].length; l++)
				C[i][l] = A[i][l] / b;
		return C;
	}

	public static double[][] add(double[][] A, double[][] B) {
		double[][] C = new double[A.length][A[0].length];
		for (int i = 0; i < A.length; i++)
			for (int l = 0; l < A[i].length; l++)
				C[i][l] = A[i][l] + B[i][l];
		return C;
	}

	public static double[][][] add(double[][][] A, double[][][] B) {
		double[][][] C = new double[A.length][A[0].length][A[0][0].length];
		for (int i = 0; i < A.length; i++)
			C[i] = add(A[i], B[i]);
		return C;
	}

	public static double[][] minus(double[][] A, double[][] B) {
		double[][] C = new double[A.length][A[0].length];
		for (int i = 0; i < A.length; i++)
			for (int l = 0; l < A[i].length; l++)
				C[i][l] = A[i][l] - B[i][l];
		return C;
	}

	public static double[][] mult(double[][] A, double[][] B) {
		double[][] C = new double[A.length][B[0].length];
		for (int i = 0; i < A.length; i++)
			for (int l = 0; l < B[0].length; l++)
				for (int j = 0; j < A[0].length; j++)
					C[i][l] += A[i][j] * B[j][l];
		return C;
	}

	public static double[][] tran(double[][] A) {
		double[][] B = new double[A[0].length][A.length];
		for (int i = 0; i < A.length; i++)
			for (int l = 0; l < A[i].length; l++)
				B[l][i] = A[i][l];
		return B;
	}

	public static double sigmoid(double a) {
		return 1 / (1 + Math.exp(-a));
	}

	public static double[] sigmoid(double[] a) {
		double[] b = new double[a.length];
		for (int i = 0; i < a.length; i++)
			b[i] = sigmoid(a[i]);
		return b;
	}

	public static double[][] sigmoid(double[][] A) {
		double[][] B = new double[A.length][A[0].length];
		for (int i = 0; i < A.length; i++)
			B[i] = sigmoid(A[i]);
		return B;
	}

	public static double relu(double a) {
		return a < 0 ? 0 : a;
	}

	public static double[][] relu(double[][] A) {
		double[][] B = new double[A.length][A[0].length];
		for (int i = 0; i < A.length; i++)
			for (int l = 0; l < A[i].length; l++)
				B[i][l] = relu(A[i][l]);
		return B;
	}

	public static double[][][] relu(double[][][] A) {
		double[][][] B = new double[A.length][A[0].length][A[0][0].length];
		for (int i = 0; i < A.length; i++)
			B[i] = relu(A[i]);
		return B;
	}

	public static double[][] conv(double[][] A, double[][] B) {
		int height = B.length - A.length + 1, width = B[0].length - A[0].length + 1;
		double[][] C = new double[height][width];
		for (int i = 0; i < height; i++)
			for (int l = 0; l < width; l++)
				for (int m = 0; m < A.length; m++)
					for (int n = 0; n < A[m].length; n++)
						C[i][l] += B[i + m][l + n] * A[m][n];
		return C;
	}

	public static double[][][] conv(double[][][] A, double[][][] B) {
		double[][][] C = new double[A.length][B[0].length - A[0].length + 1][B[0][0].length - A[0][0].length + 1];
		for (int i = 0; i < A.length; i++)
			for (int l = 0; l < B.length; l++)
				C[i] = add(C[i], conv(A[i], B[l]));
//		print(A);
//		print(B);
//		print(C);
		return C;
	}

	public static double[][] maxpool(double[][] A) {
		double[][] B = new double[(int) Math.ceil(A.length / 2.0)][(int) Math.ceil(A[0].length / 2.0)];
		for (int y = 0; y < A.length; y++)
			for (int x = 0; x < A[y].length; x++)
				if (B[y / 2][x / 2] < A[y][x])
					B[y / 2][x / 2] = A[y][x];
		return B;
	}

	public static double[][][] maxpool(double[][][] A) {
		double[][][] B = new double[A.length][][];
		for (int i = 0; i < A.length; i++)
			B[i] = maxpool(A[i]);
		return B;
	}

	public static String format(double[][] A) {
		StringBuilder sb = new StringBuilder();
		for (double[] b : A) {
			for (double d : b) {
				BigDecimal g = new BigDecimal(d);
				g.setScale(6, BigDecimal.ROUND_HALF_UP);
				sb.append(g.doubleValue() + ",");
			}
			sb.delete(sb.length() - 1, sb.length()).append("\n");
		}
		return sb.toString();
	}

	public static String print(Object o) {
		Gson gson = new GsonBuilder().create();
		log.info(gson.toJson(o));
		return gson.toJson(o);
	}

	public static void main(String[] args) {
		double[][] A = new double[][] {

				{ 1, 2, 3, 4, 4 },

				{ 1, 2, 3, 4, 4 },

				{ 1, 7, 3, 6, 4 },

				{ 1, 2, 3, 4, 4 }

		};
		System.out.println(format(maxpool(A)));
	}

}
