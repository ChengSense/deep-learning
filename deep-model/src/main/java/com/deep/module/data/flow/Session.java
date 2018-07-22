package com.deep.module.data.flow;

import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.Each;
import com.deep.util.Feach;
import com.deep.util.Model;

public class Session<E> extends Model {

	private Integer epoch;
	private Shape<E> inShape, labShape;
	private TensorFlow tf;

	public Session(TensorFlow tf, Shape<E> inShape, Shape<E> labShape) {

		this.inShape = inShape;
		this.labShape = labShape;
		this.tf = tf;

	}

	private void forward(E input) {

		inShape.set(input);

		new Each<Node>(tf.list) {

			public void each(Node node) {

				node.compute();

			}

		};

	}

	private void backward(E label) {

		labShape.set(label);

		new Each<Node>(tf.list, null) {

			public void each(Node node) {

				node.gradient();

			}

		};

	}

	public void run(E[] input) {

		forward((E) input);

	}

	public void run(E[] input, E[] label, int epochs, Integer batch) {

		IntStream.range(0, epochs).forEach(i -> {

			this.epoch = i;

			new Feach<E>(input, label) {

				public void each(E input, E label) {

					log(batch, index());

					forward(input);

					backward(label);

				}

			};

		});

	}

	private void log(Integer batch, int i) {

		if (batch != null && epoch % batch == 0) {

			new Thread(new Runnable() {

				public void run() {

					Logger log = Logger.getLogger(Session.class);

					new Each<Node>(tf.list) {

						public void each(Node node) {

							log.debug("epoch :" + epoch + ":" + (i + 1) + ":" + (index() + 1));
							log.debug("epoch :" + node.toString());

						}

					};

				}

			}).start();
		}

	}

}
