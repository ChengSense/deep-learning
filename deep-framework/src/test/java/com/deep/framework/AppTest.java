package com.deep.framework;

import com.deep.framework.flow.Executor;
import com.deep.framework.flow.TensorFlow;
import com.deep.framework.graph.Tenser;
import org.junit.Test;

public class AppTest {

    @Test
    public void sigmoidTest() {
        TensorFlow tf = new TensorFlow();
        Tenser tenser = tf.sigmoid(new Tenser(3d));
        Executor executor = new Executor(tenser);
        executor.run(null);
        Double value = 1 / (1 + Math.exp(-3d));
        System.out.println(value);
        Double value1 = value * (1 - value);
        System.out.println(value1);
    }

    @Test
    public void appaTest() {
        TensorFlow tf = new TensorFlow();
        Tenser x = new Tenser(2d);
        Tenser m = tf.mul(tf.minus(new Tenser(6d), x), x);
        Executor executor = new Executor(m);
        executor.run(null);
    }

    @Test
    public void matmulTest() {
        TensorFlow tf = new TensorFlow();
        Tenser tenser = tf.matmul(new Tenser(new int[]{2, 4}), new Tenser(new int[]{4, 1}));
        Executor executor = new Executor(tenser);
        executor.run(null);
    }

    @Test
    public void appTest() {
        TensorFlow tf = new TensorFlow();

        Tenser tenser11 = tf.matmul(new Tenser(new int[]{4, 2}), new Tenser(new int[]{2, 1}));
        Tenser tenser12 = tf.addx(tenser11, new Tenser(new int[]{4, 1}));
        Tenser tenser13 = tf.sigmoidx(tenser12);

        Tenser tenser21 = tf.matmul(new Tenser(new int[]{6, 4}), tenser13);
        Tenser tenser22 = tf.addx(tenser21, new Tenser(new int[]{6, 1}));
        Tenser tenser23 = tf.sigmoidx(tenser22);

        Tenser tenser31 = tf.matmul(new Tenser(new int[]{1, 6}), tenser23);
        Tenser tenser32 = tf.addx(tenser31, new Tenser(new int[]{1, 1}));
        Tenser tenser33 = tf.sigmoidx(tenser32);
        Tenser tenser34 = tf.squarex(new Tenser(new int[]{1, 1}), tenser33);

        Executor executor = new Executor(tenser34);
        executor.run(null);

    }

}
