package com.deep.module.data.flow;

import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.Each;
import com.deep.util.Feach;
import com.deep.util.Model;

public class Session<E> extends Model {

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

		log(0, 10);

	}

	public void run(E[] input, E[] label, int epoch, Integer step) {

		IntStream.range(0, epoch).forEach(i -> {

			new Feach<E>(input, label) {

				public void each(E input, E label) {

					forward(input);

					backward(label);

					log(i, step);

				}

			};

		});

	}

	private void log(int epoch, Integer step) {

		if (step !=null && epoch % step != 0) return;

		Logger log = Logger.getLogger(Session.class);

		new Each<Node>(tf.list) {

			public void each(Node node) {

				log.info("epoch :" + epoch + ":" + index());
				log.info("epoch :" + node.toString());

			}

		};

	}

}
