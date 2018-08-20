package com.deep.module.gradient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.deep.gradient.AutoDiff;
import com.deep.gradient.diff.AddDiff;
import com.deep.gradient.diff.MeanDiff;
import com.deep.gradient.diff.MulDiff;
import com.deep.gradient.diff.SigmoidDiff;
import com.deep.gradient.diff.SoftmaxDiff;
import com.deep.math.Matrix;
import com.deep.module.graph.Shape;
import com.deep.util.Each;
import com.deep.util.Range;

public class Gradient<E> extends Range implements Serializable {

	static double rate = 0.003;

	public void prod(Shape... shapes) {

		Shape<double[][]> weight = shapes[0];
		Shape<double[][][][]> input = shapes[1];
		Shape<double[][][]> output = shapes[2];

		weight.diff(Shape.fill(weight.get(), 0));
		input.diff(Shape.fill(input.get(), 0));

		new Each<double[]>(weight.get()) {

			public void each(double[] we, int ix) {

				new Each<Double>(we) {

					public void each(Double w, int lx) {

						double[][] in = input.get()[ix][lx];

						Each(in, (i, l) -> {

							double x = input.get()[ix][lx][i][l];
							double d = output.diff()[ix][i][l];

							Map map = new HashMap();
							map.put("x", x);
							map.put("w", w);

							AutoDiff diff = new MulDiff(map, d);
							input.diff()[ix][lx][i][l] = diff.getDiff("x");
							weight.diff()[ix][lx] += diff.getDiff("w");

							weight.get()[ix][lx] += -rate * diff.getDiff("w");

						});

					}

				};

			}

		};

	}

	public void matmul(Shape... shapes) {

		Shape<double[][]> weight = shapes[0];
		Shape<double[][]> input = shapes[1];
		Shape<double[][]> output = shapes[2];

		weight.diff(Shape.fill(weight.get(), 0));
		input.diff(Shape.fill(input.get(), 0));

		new Each<double[]>(weight.get()) {

			public void each(double[] we, int ix) {

				new Each<Double>(we) {

					public void each(Double w, int lx) {

						double x = input.get()[lx][0];
						double d = output.diff()[ix][0];

						Map map = new HashMap();
						map.put("x", x);
						map.put("w", w);

						AutoDiff diff = new MulDiff(map, d);
						input.diff()[lx][0] += diff.getDiff("x");
						weight.diff()[ix][lx] = diff.getDiff("w");

						weight.get()[ix][lx] += -rate * diff.getDiff("w");

					}

				};

			}

		};

	}

	public void add(Shape... shapes) {

		Shape<double[][]> input = shapes[0];
		Shape<double[][]> bias = shapes[1];
		Shape<double[][]> output = shapes[2];

		input.diff(Shape.fill(input.get(), 0));
		bias.diff(Shape.fill(bias.get(), 0));

		new Each<double[]>(input.get()) {

			public void each(double[] in, int ix) {

				double x = in[0];
				double b = bias.get()[ix][0];
				double d = output.diff()[ix][0];

				Map map = new HashMap();
				map.put("x", x);
				map.put("b", b);

				AutoDiff diff = new AddDiff(map, d);
				bias.diff()[ix][0] = diff.getDiff("b");
				input.diff()[ix][0] = diff.getDiff("x");

				bias.get()[ix][0] += -rate * diff.getDiff("b");
			}

		};

	}

	public void add3(Shape... shapes) {

		Shape<double[][][]> input = shapes[0];
		Shape<double[]> bias = shapes[1];
		Shape<double[][][]> output = shapes[2];

		input.diff(Shape.fill(input.get(), 0));
		bias.diff(Shape.fill(bias.get(), 0));

		new Each<double[][]>(input.get()) {

			public void each(double[][] in, int ix) {

				double b = bias.get()[ix];

				Each(in, (i, l) -> {

					double x = in[i][l];
					double d = output.diff()[ix][i][l];

					Map map = new HashMap();
					map.put("x", x);
					map.put("b", b);

					AutoDiff diff = new AddDiff(map, d);
					input.diff()[ix][i][l] = diff.getDiff("x");
					bias.diff()[ix] += diff.getDiff("b");

					bias.get()[ix] += -rate * diff.getDiff("b");

				});

			}

		};

	}

	public void sigmoid(Shape... shapes) {

		Shape<double[][]> input = shapes[0];
		Shape<double[][]> output = shapes[1];

		input.diff(Shape.fill(input.get(), 0));

		new Each<double[]>(input.get()) {

			public void each(double[] in, int ix) {

				double z = in[0];
				double d = output.diff()[ix][0];

				Map map = new HashMap();
				map.put("E", Math.E);
				map.put("x", z);

				AutoDiff diff = new SigmoidDiff(map, d);
				input.diff()[ix][0] = diff.getDiff("x");

			}

		};

	}

	public void reduce(Shape... shapes) {

		Shape<double[][]> lable = shapes[0];
		Shape<double[][]> output = shapes[1];

		output.diff(Shape.fill(output.get(), 0));

		new Each<double[]>(lable.get()) {

			public void each(double[] lab, int ix) {

				double l = lab[0];
				double x = output.get()[ix][0];

				Map map = new HashMap();
				map.put("l", l);
				map.put("x", x);

				AutoDiff diff = new MeanDiff(map);
				output.diff()[ix][0] = diff.getDiff("x");

			}

		};

	}

	public void conv(Shape... shapes) {

		Shape<double[][][]> weight = shapes[0];
		Shape<double[][][]> input = shapes[1];
		Shape<double[][][][]> output = shapes[2];

		weight.diff(Shape.fill(weight.get(), 0));
		input.diff(Shape.fill(input.get(), 0));

		new Each<double[][]>(weight.get()) {

			public void each(double[][] we, int ix) {

				double[][] wei = Shape.copy(we);

				new Each<double[][]>(input.get()) {

					public void each(double[][] in, int lx) {

						int height = in.length - we.length + 1;
						int width = in[0].length - we[0].length + 1;

						forEach(height, width, (i, l) -> {

							Each(we, (m, n) -> {

								double w = wei[m][n];
								double x = in[i + m][l + n];
								double d = output.diff()[ix][lx][i][l];

								Map map = new HashMap();
								map.put("w", w);
								map.put("x", x);

								AutoDiff diff = new MulDiff(map, d);
								weight.diff()[ix][m][n] += diff.getDiff("w");
								input.diff()[lx][i + m][l + n] += diff.getDiff("x");
								synchronized (this) {we[m][n] += -rate * diff.getDiff("w");}

							});

						});

					}

				};

			}

		};

	}

	public void relu(Shape... shapes) {

		Shape<double[][][]> input = shapes[0];
		Shape<double[][][]> output = shapes[1];

		input.diff(Shape.fill(input.get(), 0));

		new Each<double[][]>(input.get()) {

			public void each(double[][] in, int ix) {

				Each(in, (i, l) -> {

					double a = in[i][l];
					double d = output.diff()[ix][i][l];

					input.diff()[ix][i][l] = d * (a < 0 ? 0 : 1);

				});

			}

		};

	}

	public void maxpool(Shape... shapes) {

		Shape<double[][][]> input = shapes[0];
		Shape<double[][][]> output = shapes[1];

		input.diff(Shape.fill(input.get(), 0));

		new Each<double[][]>(input.get()) {

			public void each(double[][] in, int ix) {

				Each(in, (i, l) -> {

					int m = i / 2, n = l / 2;

					double d = output.diff()[ix][m][n];

					input.diff()[ix][i][l] = d;

				});

			}

		};

	}

	public void softmax(Shape... shapes) {

		Shape<double[][]> input = shapes[0];
		Shape<double[][]> output = shapes[1];

		input.diff(Shape.fill(input.get(), 0));

		new Each<double[]>(input.get()) {

			public void each(double[] in, int ix) {

				double s = Matrix.sum(in);

				new Each<Double>(in) {

					public void each(Double x, int lx) {

						double d = output.diff()[ix][lx];

						Map map = new HashMap();
						map.put("E", Math.E);
						map.put("a", s - Math.exp(x));
						map.put("x", x);

						AutoDiff diff = new SoftmaxDiff(map, d);

						input.diff()[ix][lx] = diff.getDiff("x");

						input.get()[ix][lx] += -rate * diff.getDiff("x");

					}

				};

			}

		};

	}

}
