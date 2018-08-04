package com.deep.module.data.flow;

import java.util.function.Consumer;

import org.apache.log4j.Logger;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.Each;
import com.deep.util.Model;
import com.deep.util.Range;

public class Session<E> extends Model {
	public int epoch, index;
	private Shape<E> inShape, labShape;
	private transient Consumer<Session> feach;
	public final TensorFlow tf;
	private static final long serialVersionUID = 1L;

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

		Range.Each(epoch, i -> {

			this.epoch = i;

			Range.Each(input.length, l -> {

				this.index = l;

				forward(input[l]);

				backward(label[l]);

				feach(null);

			});

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
