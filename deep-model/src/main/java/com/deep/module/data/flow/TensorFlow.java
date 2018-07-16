package com.deep.module.data.flow;

import com.deep.gradient.Option;
import com.deep.math.Matrix;
import com.deep.module.gradient.Gradient;
import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.Array;
import com.deep.util.ReShape;

public class TensorFlow extends ReShape {

	Array<Node> list;

	Gradient grad;

	public TensorFlow() {

		list = new Array<Node>();

		grad = new Gradient();

	}

	public Node matmul(Shape... shapes) {

		Node node = new Node(Option.MATMUL, shapes) {

			public void compute() {

				if (shapes[1].get() instanceof double[][]) {

					Shape<double[][]> weight = shapes[0];
					Shape<double[][]> input = shapes[1];

					output(Matrix.mult(weight.get(), input.get()));
					return;

				}

				if (shapes[1].get() instanceof double[][][]) {

					Shape<double[][]> weight = shapes[0];
					Shape<double[][][]> input = shapes[1];
					shapes[1].set(reshape(input.get(), new double[input.get().length][1]));
					Shape<double[][]> inputx = shapes[1];

					output(Matrix.mult(weight.get(), inputx.get()));
					return;

				}

			}

			public void gradient() {

				grad.matmul(shapes[0], shapes[1], output());

			}

		};

		list.add(node);

		return node;

	}

	public Node add(Shape<double[][]>... shapes) {

		Node node = new Node(Option.ADD, shapes) {

			public void compute() {

				output(Matrix.add(shapes[0].get(), shapes[1].get()));

			}

			public void gradient() {

				grad.add(shapes[0], shapes[1], output());

			}

		};

		list.add(node);

		return node;

	}

	public Node add3(Shape... shapes) {

		Node node = new Node(Option.ADD, shapes) {

			public void compute() {
				
				Shape<double[][][]> input = shapes[0];
				Shape<double[]> bias = shapes[1];
				output(Matrix.add(input.get(), bias.get()));

			}

			public void gradient() {

				grad.add3(shapes[0], shapes[1], output());

			}

		};

		list.add(node);

		return node;

	}

	public Node sigmoid(Shape<double[][]>... shapes) {

		Node node = new Node(Option.SIGMOID, shapes) {

			public void compute() {

				output(Matrix.sigmoid(shapes[0].get()));

			}

			public void gradient() {

				grad.sigmoid(shapes[0], output());

			}

		};

		list.add(node);

		return node;

	}

	public Node reduce(Shape... shapes) {

		Node node = new Node(Option.SIGMOID, shapes) {

			public void compute() {

			}

			public void gradient() {

				grad.reduce(shapes);

			}

		};

		list.add(node);

		return node;

	}

	public Node conv(Shape... shapes) {

		Node node = new Node(Option.SIGMOID, shapes) {

			public void compute() {

				Shape<double[][][]> weight = shapes[0];
				Shape<double[][][]> input = shapes[1];

				output(Matrix.conv(weight.get(), input.get()));

			}

			public void gradient() {

				if (output().diff() instanceof double[][][]) {
					grad.conv(shapes[0], shapes[1], output());
					return;
				}

				if (output().diff() instanceof double[][]) {

					Shape<double[][]> output = output();
					output().diff(reshape(output.diff(), new double[output.diff().length][1][1]));
					grad.conv(shapes[0], shapes[1], output());

					return;

				}

			}

		};

		list.add(node);

		return node;

	}

	public Node relu(Shape<double[][][]>... shapes) {

		Node node = new Node(Option.SIGMOID, shapes) {

			public void compute() {

				output(Matrix.relu(shapes[0].get()));

			}

			public void gradient() {

				grad.relu(shapes[0], output());

			}

		};

		list.add(node);

		return node;

	}

	public Node maxpool(Shape<double[][][]>... shapes) {

		Node node = new Node(Option.SIGMOID, shapes) {

			public void compute() {

				output(Matrix.maxpool(shapes[0].get()));

			}

			public void gradient() {

				grad.maxpool(shapes[0], output());

			}

		};

		list.add(node);

		return node;

	}

}
