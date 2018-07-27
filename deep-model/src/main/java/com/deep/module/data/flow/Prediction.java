package com.deep.module.data.flow;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;

public class Prediction<E> {
	private Session session;
	private TensorFlow<Node> tf;

	public Prediction(Session se) {
		this.session = se;
		this.tf = se.tf;
	}

	public Prediction feed(E input) {
		session.run(input);
		return this;
	}

	public double eval(E lab) {

		Node node = tf.list.end();
		Shape label = node.get("lable");
		label.set(lab);

		Shape input = node.get("input");
		node.gradient();

		return Shape.mean(input.diff());

	}

}
