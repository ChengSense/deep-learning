package com.deep.module.data.flow;

import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.Each;
import com.deep.util.Feach;
import com.deep.util.Model;

public class Session<E> extends Model {

	private Integer epoch, batch;
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
		
		log(0);

	}

	public void run(E[] input, E[] label, int epochs, Integer batch) {
		
		this.batch = batch;

		IntStream.range(0, epochs).forEach(epoch -> {

			this.epoch = epoch;

			new Feach<E>(input, label) {

				public void each(E input, E label) {

					forward(input);

					log(index());

					backward(label);

				}

			};

		});

	}

	public void log(int i) {

		if (batch != null && epoch % batch == 0) {

			Logger log = Logger.getLogger(Session.class);

			new Each<Node>(tf.list) {

				public void each(Node node) {

					log.debug("epoch :" + epoch + ":" + (i + 1) + ":" + (index() + 1));
					log.debug("epoch :" + node.toString());

				}

			};

		}

	}

}
