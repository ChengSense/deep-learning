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

        Tenser tenser11 = tf.matmul(new Tenser(new int[]{2, 4}), new Tenser(new int[]{4, 1}));
        Tenser tenser12 = tf.addx(tenser11, new Tenser(new int[]{2, 1}));
        Tenser tenser13 = tf.sigmoidx(tenser12);
        Tenser tenser14 = tf.squarex(tenser13 ,new Tenser(new int[]{2, 1}));

        Executor executor = new Executor(tenser14);
        executor.run(null);

    }

}
