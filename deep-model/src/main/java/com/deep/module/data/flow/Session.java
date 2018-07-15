package com.deep.module.data.flow;

import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.Each;
import com.deep.util.Feach;

public class Session<E> {

	Logger log = Logger.getLogger(Session.class);
	E[] input, label;
	TensorFlow tf;

	public Session(TensorFlow tf, E[] input, E[] label) {

		this.tf = tf;

		this.input = input;

		this.label = label;

	}

	public void forward() {

		new Each<Node>(tf.list) {

			public void each(Node node) {

				node.compute();

			}

		};

	}

	public void backward() {

		new Each<Node>(tf.list, null) {

			public void each(Node node) {

				node.gradient();

			}

		};

	}

	public void run(Shape<E> inShape, Shape<E> labShape, int epoch) {

		IntStream.range(0, epoch).forEach(i -> {

			new Feach<E>(input, label) {

				public void each(E input, E label) {

					inShape.set(input);

					forward();

					labShape.set(label);

					backward();

					log(i);

				}

			};

		});

	}

	private void log(int epoch) {

		if (epoch % 10 == 0)

			new Each<Node>(tf.list) {

				public void each(Node node) {

					log.info("epoch :" + epoch + ":" + index());
					log.info("epoch :" + node.toString());

				}

			};

	}

}
