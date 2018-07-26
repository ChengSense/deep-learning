package com.deep.module.data.flow;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.Each;
import com.deep.util.Feach;
import com.deep.util.Model;

public class Session<E> extends Model {
	public int epoch;
	private Shape<E> inShape, labShape;
	private Consumer<Session> feach;
	public final TensorFlow tf;

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

	public void feach(Consumer<Session> feach) {

		if (feach != null) {

			this.feach = feach;

		}

		else if (this.feach != null) {

			this.feach.accept(this);

		}

	}

	public void run(E input) {

		forward(input);

		feach(null);

	}

	public void run(E[] input, E[] label, int epoch) {

		IntStream.range(0, epoch).forEach(i -> {

			this.epoch = i;

			new Feach<E>(input, label) {

				public void each(E input, E label) {

					forward(input);

					backward(label);

					feach(null);

				}

			};

		});

	}

	public void log() {

		Logger log = Logger.getLogger(Session.class);

		new Each<Node>(tf.list) {

			public void each(Node node) {

				log.debug("epoch :" + epoch + ":" + index());
				log.debug("epoch :" + node.toString());

			}

		};

	}

}
