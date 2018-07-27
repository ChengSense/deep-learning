package com.deep.modle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.deep.module.data.flow.Prediction;
import com.deep.module.data.flow.Session;
import com.deep.module.data.flow.TensorFlow;
import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.DataSet;
import com.deep.util.Model;

public class CNNTest {

	Session session;
	Logger log = Logger.getLogger(CNNTest.class);
	double[][][][] input = DataSet.loadImageData();
	double[][][] label = new double[][][] { { { 0.9 } }, { { 0.9 } }, { { 0.9 } }, { { 0.9 } }, { { 0.9 } }, { { 0.9 } }, { { 0.9 } }, { { 0.9 } }, { { 0.9 } }, { { 0.9 } }, { { 0.9 } }, { { 0.9 } }, { { 0.1 } }, { { 0.1 } }, { { 0.1 } }, { { 0.1 } }, { { 0.1 } }, { { 0.1 } }, { { 0.1 } }, { { 0.1 } }, { { 0.1 } }, { { 0.1 } }, { { 0.1 } }, { { 0.1 } } };
	String path = "D:/cnn-session.ml";

	@Test
	public void Train() {

		TensorFlow tf = new TensorFlow();

		// 卷积层1
		Node node1 = tf.conv(new Shape("weight", new double[6][5][5], "R"), new Shape("input", new double[3][32][32], "R"));
		Node node2 = tf.softmax(new Shape("input", new double[6][3], "R"));// 6
		Node node3 = tf.prod(node2.output(), node1.output());// 6*32
		Node node4 = tf.add3(node3.output(), new Shape("bias", new double[6], "R"));// 6*28
		Node node5 = tf.relu(node4.output());// 6*28
		Node node6 = tf.maxpool(node5.output());// 6*14

		// 卷积层2
		Node node7 = tf.conv(new Shape("weight", new double[16][5][5], "R"), node6.output());// 6*14
		Node node8 = tf.softmax(new Shape("input", new double[16][6], "R"));// 16
		Node node9 = tf.prod(node8.output(), node7.output());// 16*14
		Node node10 = tf.add3(node9.output(), new Shape("bias", new double[16], "R"));// 16*10
		Node node11 = tf.relu(node10.output());// 16*10
		Node node12 = tf.maxpool(node11.output());// 16*5

		// 卷积层3
		Node node13 = tf.conv(new Shape("weight", new double[32][5][5], "R"), node12.output());
		Node node14 = tf.softmax(new Shape("input", new double[32][16], "R"));// 16
		Node node15 = tf.prod(node14.output(), node13.output());// 16*14

		// 全连接层5
		Node node16 = tf.matmul(new Shape("weight", new double[1][32], "R"), node15.output());
		Node node17 = tf.add(node16.output(), new Shape("bias", new double[1][1], "R"));
		Node node18 = tf.sigmoid(node17.output());

		// 损失节点
		Node node19 = tf.reduce(new Shape("lable", new double[1][1]), node18.output());

		session = new Session(tf, node1.get("input"), node19.get("lable"));
		session.feach(a -> {
			Node node = (Node) session.tf.list.end();
			log.debug("epoch :" + session.epoch);
			log.debug("epoch :" + node);
		});
		session.run(input, label, 500);
		session.inStore(path);

	}

	// @Test
	public void Traing() {

		Model<Session> model = new Model<Session>();
		session = model.outStore(path);
		session.feach(a -> {
			Node node = (Node) session.tf.list.end();
			log.debug("epoch :" + session.epoch);
			log.debug("epoch :" + node);
		});
		session.run(input, label, 500);
		session.inStore(path);

	}

	// @Test
	public void Test() {
		// double[][][] input1 = DataSet.img2rgb("E:\\imgs\\23_200.jpg");
		// double[][][] input2 = DataSet.img2rgb("E:\\imgs\\270_191.jpg");
		double[][][] input3 = DataSet.img2rgb("E:\\imgs\\270_191.jpg");

		Model<Session> model = new Model<Session>();
		session = model.outStore(path);
		session.feach(a -> {
			Node node = (Node) session.tf.list.end();
			log.debug("epoch :" + session.epoch);
			log.debug("epoch :" + node);
		});
		session.run(input3);

	}

	//@Test
	public void ImgTest() {

		Model<Session> model = new Model<Session>();
		session = model.outStore(path);

		try (Stream<Path> stream = Files.list(Paths.get("E:/imgs/"))) {

			stream.forEach(path -> {

				double[][][] input = DataSet.img2rgb(path.toString());

				double cost = new Prediction(session).feed(input).eval(new double[][] { { 0.1 } });

				if (cost < 0.006)

					DataSet.copy(path.toString(), "E:/pred-img/");

				log.debug(path.toString() + " -> " + cost);

			});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
