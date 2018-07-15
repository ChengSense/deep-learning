package com.deep.modle;

import com.deep.module.data.flow.Session;
import com.deep.module.data.flow.TensorFlow;
import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.ImageKit;

import junit.framework.TestCase;

public class RNNTest extends TestCase {

	public void test() {

		double[][][][] input = ImageKit.getInput();
		double[][][] label = new double[][][] { { { 1 } },{ { 1 } },{ { 1 } },{ { 1 } },{ { 1 } },{ { 1 } },{ { 1 } },{ { 1 } },{ { 1 } },{ { 1 } },{ { 1 } },{ { 1 } } };

		TensorFlow tf = new TensorFlow();

		// 卷积层1
		Node node1 = tf.conv(new Shape("weight", new double[6][5][5], "R"), new Shape("input", new double[3][32][32], "R"));
		Node node2 = tf.add3(node1.output(), new Shape("bias", new double[6][28][28], "R"));// 6*28
		Node node3 = tf.relu(node2.output());// 6*28
		Node node4 = tf.maxpool(node3.output());// 6*14

		// 卷积层2
		Node node5 = tf.conv(new Shape("weight", new double[16][5][5], "R"), node4.output());// 6*14
		Node node6 = tf.add3(node5.output(), new Shape("bias", new double[16][10][10], "R"));// 16*10
		Node node7 = tf.relu(node6.output());// 16*10
		Node node8 = tf.maxpool(node7.output());// 16*5

		// 卷积层3
		Node node9 = tf.conv(new Shape("weight", new double[16][5][5], "R"), node8.output());

		// 全连接层5
		Node node10 = tf.matmul(new Shape("weight", new double[1][16], "R"), node9.output());
		Node node11 = tf.add(node10.output(), new Shape("bias", new double[1][1], "R"));
		Node node12 = tf.sigmoid(node11.output());

		// 损失节点
		Node node13 = tf.reduce(new Shape("lable", new double[1][1]), node12.output());

		Session session = new Session(tf, input, label);
		session.run(node1.get("input"), node13.get("lable"), 50);

	}
}
