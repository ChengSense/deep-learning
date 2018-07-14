package com.deep.module.data.flow;

import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.Each;
import com.deep.util.Feach;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	public void run(Shape<E> inShape, Shape<E> labShape, Shape<E> outShape, int epoch) {

		IntStream.range(0, epoch).forEach(i -> {

			new Feach<E>(input, label) {

				public void each(E input, E label) {

					inShape.set((E[]) input);

					forward();

					labShape.set((E[]) label);

					backward();

					log(input, label, outShape.get(), i);

				}

			};

		});

	}

	private void log(E input, E label, E[] out, int epoch) {

		if (epoch % 1000 == 0) {

			log.info("epoch :" + epoch);

			log.info("input :" + string(input));

			log.info("label :" + string(label));

			log.info("output:" + string(out) + "\n");

		}

	}

	public String string(Object o) {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(o);
	}

}
