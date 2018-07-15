package com.deep.modle;

import com.deep.module.data.flow.Session;
import com.deep.module.data.flow.TensorFlow;
import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;

import junit.framework.TestCase;

public class NNTest extends TestCase {

	public void test() {

		TensorFlow tf = new TensorFlow();

		// 第一
		Node node1 = tf.matmul(new Shape("weight", new double[4][2], "R"), new Shape("input", new double[2][1]));
		Node node2 = tf.add(node1.output(), new Shape("bias", new double[4][1], "R"));
		Node node3 = tf.sigmoid(node2.output());

		// 第二层
		Node node4 = tf.matmul(new Shape("weight", new double[6][4], "R"), node3.output());
		Node node5 = tf.add(node4.output(), new Shape("bias", new double[6][1], "R"));
		Node node6 = tf.sigmoid(node5.output());

		// 第三层
		Node node7 = tf.matmul(new Shape("weight", new double[1][6], "R"), node6.output());
		Node node8 = tf.add(node7.output(), new Shape("bias", new double[1][1], "R"));
		Node node9 = tf.sigmoid(node8.output());

		// 损失节点
		Node node10 = tf.reduce(new Shape("lable", new double[1][1]), node9.output());

		double[][][] input = new double[][][] { { { 0.1 }, { 0.1 } }, { { 0.1 }, { 0.2 } }, { { 0.2 }, { 0.2 } }, { { 0.2 }, { 0.3 } }, { { 0.3 }, { 0.7 } }, { { 0.4 }, { 0.8 } }, { { 0.5 }, { 0.9 } }, { { 0.8 }, { 0.9 } }, { { 0.6 }, { 0.8 } } };
		double[][][] label = new double[][][] { { { 0.01 } }, { { 0.02 } }, { { 0.04 } }, { { 0.06 } }, { { 0.21 } }, { { 0.32 } }, { { 0.45 } }, { { 0.72 } }, { { 0.48 } } };

		Session session = new Session(tf, input, label);

		session.run(node1.get("input"), node10.get("lable"), 10000000);

		System.out.println(tf);

	}
}
