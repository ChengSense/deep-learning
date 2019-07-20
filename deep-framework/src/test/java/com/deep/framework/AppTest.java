package com.deep.framework;

import com.deep.framework.framework.Executor;
import com.deep.framework.graph.TensorFlow;
import com.deep.framework.graph.Tenser;
import org.junit.Test;

public class AppTest {

    @Test
    public void sigmoidTest() {
        TensorFlow tf = new TensorFlow();
        Tenser tenser = tf.sigmoid(new Tenser(-0.6354469361189982));
        Executor executor = new Executor(tenser);
        executor.run();

        Double value = 1 / (1 + Math.exp(-(-0.6354469361189982)));
        System.out.println(value);
        Double value1 = value * (1 - value);
        System.out.println(value1*0.1694231856183997);
    }

    @Test
    public void appaTest() {
        TensorFlow tf = new TensorFlow();
        Tenser x = new Tenser(2d);
        Tenser m = tf.mul(tf.minus(new Tenser(6d), x), x);
        Executor executor = new Executor(m);
        executor.run();
    }

    @Test
    public void matmulTest() {
        TensorFlow tf = new TensorFlow();
        Tenser tenser = tf.matmul(new Tenser(new int[]{6, 4}), new Tenser(new int[]{4, 1}));
        Executor executor = new Executor(tenser);
        executor.run();
    }

    @Test
    public void squarexTest() {
        TensorFlow tf = new TensorFlow();
        Tenser tenser = tf.square(new Tenser(0.01), new Tenser(0.391249035007275));
        Executor executor = new Executor(tenser);
        executor.run();
    }

    @Test
    public void xTest() {

        System.out.println(-2.243732467029906E-4*0.6);
    }

}
