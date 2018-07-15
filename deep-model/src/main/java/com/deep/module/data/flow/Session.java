package com.deep.module.data.flow;

import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.Each;
import com.deep.util.Feach;

public class Session<E> {

	Logger log = Logger.getLogger(Session.class);
  private	Shape<E> inShape, labShape;
  private TensorFlow tf;

	public Session(TensorFlow tf, Shape<E> inShape, Shape<E> labShape) {

		this.inShape = inShape;
		this.labShape = labShape;
		this.tf = tf;

	}

	public void forward(E input) {

		inShape.set(input);

		new Each<Node>(tf.list) {

			public void each(Node node) {

				node.compute();

			}

		};

	}

	public void backward(E label) {

		labShape.set(label);

		new Each<Node>(tf.list, null) {

			public void each(Node node) {

				node.gradient();

			}

		};

	}

	public void run(E[] input, E[] label) {

		new Feach<E>(input, label) {

			public void each(E input, E label) {

				forward(input);

				backward(label);

				log(0);

			}

		};

	}

	public void run(E[] input, E[] label, int epoch) {
		
		IntStream.range(0, epoch).forEach(i -> {

			new Feach<E>(input, label) {

				public void each(E input, E label) {

					forward(input);

					backward(label);

					log(i);

				}

			};

		});

	}

	private void log(int epoch) {

		if (epoch % 1000 != 0) return;

		new Each<Node>(tf.list) {

			public void each(Node node) {

				log.info("epoch :" + epoch + ":" + index());
				log.info("epoch :" + node.toString());

			}

		};

	}

}
