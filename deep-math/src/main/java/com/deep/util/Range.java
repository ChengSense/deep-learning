package com.deep.util;

import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class Range {
	public static void forEach(int m, IntConsumer a) {
		IntStream.range(0, m).parallel().forEach(a);
	}

	public static void forEach(int m, int n, BiConsumer<Integer, Integer> a) {
		IntStream.range(0, m).parallel().forEach(i -> IntStream.range(0, n).parallel().forEach(l -> a.accept(i, l)));
	}

	public static void forEach(double[][] A, BiConsumer<Integer, Integer> a) {
		IntStream.range(0, A.length).parallel().forEach(i -> IntStream.range(0, A[i].length).parallel().forEach(l -> a.accept(i, l)));
	}

	public static void Each(int m, IntConsumer a) {
		IntStream.range(0, m).forEach(a);
	}

	public static void Each(int m, int n, BiConsumer<Integer, Integer> a) {
		IntStream.range(0, m).forEach(i -> IntStream.range(0, n).forEach(l -> a.accept(i, l)));
	}

	public static void Each(double[][] A, BiConsumer<Integer, Integer> a) {
		IntStream.range(0, A.length).forEach(i -> IntStream.range(0, A[i].length).forEach(l -> a.accept(i, l)));
	}
}
