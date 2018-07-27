package com.deep.module.data.flow;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;

public class Prediction<E> {
	private Session session;

	public Prediction(Session se) {
		this.session = se;
	}

	public Prediction feed(E input) {
		session.run(input);
		return this;
	}

	public double eval(E lab) {

		TensorFlow<Node> tf = session.tf;
		Node node = tf.list.end();

		Shape label = node.get("lable");
		label.set(lab);

		Shape input = node.get("input");
		node.gradient();

		return Shape.mean(input.diff());

	}

}
