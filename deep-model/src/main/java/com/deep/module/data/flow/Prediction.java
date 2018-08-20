package com.deep.module.data.flow;

import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;

public class Prediction<E> {
	private Session session;

	public Prediction(Session session) {
		this.session = session;
	}

	public double eval(E input, E lable) {

		session.run(input);

		TensorFlow<Node> tf = session.tf;
		Node node = tf.list.end();

		Shape shape = node.get("lable");
		shape.set(lable);

		Shape output = node.output();
		node.gradient();

		return Math.abs(Shape.mean(output.diff()));

	}

}
