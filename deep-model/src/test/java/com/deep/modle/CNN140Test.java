package com.deep.modle;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.deep.gradient.Option;
import com.deep.module.data.flow.Prediction;
import com.deep.module.data.flow.Session;
import com.deep.module.data.flow.TensorFlow;
import com.deep.module.graph.Node;
import com.deep.module.graph.Shape;
import com.deep.util.DataSet;
import com.deep.util.Model;

public class CNN140Test {

	Logger log = Logger.getLogger(CNN140Test.class);
	String path = "./src/main/resources/modle/DeepFace.ml";
	double[][][][] input = {

			DataSet.img2rgb("./src/main/resources/DataSet/a-140.jpg"),

			DataSet.img2rgb("./src/main/resources/DataSet/b-140.jpg"),

			DataSet.img2rgb("./src/main/resources/DataSet/c-140.jpg"),

			DataSet.img2rgb("./src/main/resources/DataSet/d-140.jpg"),

			DataSet.img2rgb("./src/main/resources/DataSet/e-140.jpg"),

			DataSet.img2rgb("./src/main/resources/DataSet/f-140.jpg"),

			DataSet.img2rgb("./src/main/resources/DataSet/g-140.jpg")

	};
	double[][][] label = {

			{ { 0.1 } },

			{ { 0.3 } },

			{ { 0.5 } },

			{ { 0.5 } },

			{ { 0.5 } },

			{ { 0.5 } },

			{ { 0.8 } }

	};

	@Test
	public void Train() {

		TensorFlow tf = new TensorFlow();

		// 卷积层1
		Node node1 = tf.conv(new Shape("weight", new double[10][5][5], "R"), new Shape("input", new double[3][140][140], "R"));// 10*3*136
		Node node2 = tf.softmax(new Shape("input", new double[10][3], "R"));// 10*3
		Node node3 = tf.prod(node2.output(), node1.output());// 10*136
		Node node4 = tf.add3(node3.output(), new Shape("bias", new double[10], "R"));// 10*136
		Node node5 = tf.relu(node4.output());// 10*136
		Node node6 = tf.maxpool(node5.output());// 10*68

		// 卷积层2
		Node node7 = tf.conv(new Shape("weight", new double[20][5][5], "R"), node6.output());// 20*10*64
		Node node8 = tf.softmax(new Shape("input", new double[20][10], "R"));// 20*10
		Node node9 = tf.prod(node8.output(), node7.output());// 20*64
		Node node10 = tf.add3(node9.output(), new Shape("bias", new double[20], "R"));// 20*64
		Node node11 = tf.relu(node10.output());// 20*64
		Node node12 = tf.maxpool(node11.output());// 20*32

		// 卷积层3
		Node node13 = tf.conv(new Shape("weight", new double[30][5][5], "R"), node12.output());// 30*20*28
		Node node14 = tf.softmax(new Shape("input", new double[30][20], "R"));// 30*20
		Node node15 = tf.prod(node14.output(), node13.output());// 30*28
		Node node16 = tf.add3(node15.output(), new Shape("bias", new double[30], "R"));// 30*28
		Node node17 = tf.relu(node16.output());// 30*28
		Node node18 = tf.maxpool(node17.output());// 30*14

		// 卷积层4
		Node node19 = tf.conv(new Shape("weight", new double[60][5][5], "R"), node18.output());// 60*30*14
		Node node20 = tf.softmax(new Shape("input", new double[60][30], "R"));// 60*30
		Node node21 = tf.prod(node20.output(), node19.output());// 60*30
		Node node22 = tf.add3(node21.output(), new Shape("bias", new double[60], "R"));// 60*14
		Node node23 = tf.relu(node22.output());// 60*14
		Node node24 = tf.maxpool(node23.output());// 60*5

		// 卷积层5
		Node node25 = tf.conv(new Shape("weight", new double[120][5][5], "R"), node24.output());// 120*60*1
		Node node26 = tf.softmax(new Shape("input", new double[120][60], "R"));// 120*60
		Node node27 = tf.prod(node26.output(), node25.output());// 120*1

		// 全连接层7
		Node node28 = tf.matmul(new Shape("weight", new double[1][120], "R"), node27.output());
		Node node29 = tf.add(node28.output(), new Shape("bias", new double[1][1], "R"));
		Node node30 = tf.sigmoid(node29.output());

		// 损失节点
		Node node31 = tf.reduce(new Shape("lable", new double[1][1], "R"), node30.output());

		Session session = new Session(tf, node1.get("input"), node31.get("lable"));
		session.feach(a -> {
			Node node = (Node) session.tf.list.end();
			log.debug("epoch :" + session.epoch + ":" + session.index);
			log.debug("epoch :" + node);
			session.inStore(path);
		});
		session.run(input, label, 1000);

	}

	// @Test
	public void Traing() {

		Model<Session> model = new Model<Session>();
		Session session = model.outStore(path);
		session.feach(a -> {
			Node node = (Node) session.tf.list.end();
			log.debug("epoch :" + session.epoch + ":" + session.index);
			log.debug("epoch :" + node);
			Shape output = node.get("output");
			label[session.index] = (double[][]) output.get();
			session.inStore(path);
		});
		session.run(input, label, 500);

	}

	// @Test
	public void ImgTest() {

		double[][][] input = DataSet.img2rgb("./src/main/resources/DataSet/a-140.jpg");

		Model<Session> model = new Model<Session>();
		Session session = model.outStore(path);
		session.feach(a -> {
			session.tf.list.forEach(o -> {

				Node node = (Node) o;
				if (node.option.equals(Option.CONV)) {
					DataSet.gray2img((double[][][][]) node.output().get());
				}

				log.debug("epoch :" + session.epoch);
				log.debug("epoch :" + node);

			});
		});
		session.run(input);

	}

	// @Test
	public void Recognition() {
		double[][][] input1 = DataSet.img2rgb("./src/main/resources/DataSet/c-140.jpg");
		double[][][] input2 = DataSet.img2rgb("./src/main/resources/DataSet/a-140.jpg");

		Model<Session> model = new Model<Session>();
		Session session = model.outStore(path);
		session.feach(a -> {
			Node node = (Node) session.tf.list.end();
			log.debug("epoch :" + node);
		});
		session.run(input1);
		Node node = (Node) session.tf.list.end();
		double[][] output = (double[][]) node.get("output").get();
		double cost = new Prediction(session).feed(input2).eval(output);
		log.debug("cost :" + BigDecimal.valueOf(cost).toString());
	}

	// @Test
	public void Siamese() {
		double[][][] input1 = DataSet.img2rgb("./src/main/resources/DataSet/c-140.jpg");
		double[][][] input2 = DataSet.img2rgb("./src/main/resources/DataSet/a-140.jpg");
		double[][][] input3 = DataSet.img2rgb("./src/main/resources/DataSet/e-140.jpg");
		Model<Session> model = new Model<Session>();
		Session session = model.outStore(path);
		session.run(input1);
		session.run(input2);
		session.run(input3);
	}

}
