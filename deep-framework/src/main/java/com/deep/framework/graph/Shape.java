package com.deep.framework.graph;

import com.deep.framework.operation.Node;
import com.deep.framework.operation.None;
import com.deep.framework.util.BeanUtil;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Shape {

    static RandomDataGenerator random = new RandomDataGenerator();

    public static void forEach(int a, Range1 e) {
        IntStream.range(0, a).forEach(i -> e.apply(i));
    }

    public static void forEach(int a, int b, Range2 e) {
        forEach(a, i -> forEach(b, l -> e.apply(i, l)));
    }

    public static void forEach(int a, int b, int c, Range3 e) {
        forEach(a, i -> forEach(b, l -> forEach(c, m -> e.apply(i, l, m))));
    }

    public static <E> E random(int... x) {
        return (E) fill(Array.newInstance(None.class, x), o -> new None(random.nextGaussian(0, 1)));
    }

    public static <E> E nones(Object a, String name) {
        String names = name.replace("Tenser::", "None::");
        return (E) fill(shape(None.class, a), o -> new None(0d, names));
    }

    public static <E> E graphs(Object a) {
        return (E) fill(shape(Graph.class, a), o -> new Graph());
    }

    public static <E> E zeros(Object a) {
        return (E) fill(a, o -> new Tenser(0d));
    }

    public static Object shape(Class clas, Object a) {
        return Array.newInstance(clas, shapes(a));
    }

    public static int[] shapes(Object a) {
        List<Integer> list = new ArrayList();
        shapes(a, list);
        int[] arr = new int[list.size()];
        forEach(list.size(), i -> arr[i] = list.get(i));
        return arr;
    }

    private static <E> void shapes(E a, List list) {
        if (BeanUtil.isTenser(a)) {
            list.add(Array.getLength(a));
            shapes(Array.get(a, 0), list);
        }
    }

    public static Object fill(Object a, Fill func) {
        if (BeanUtil.isTenser(a)) {
            forEach(Array.getLength(a), i -> {
                Object m = Array.get(a, i);
                if (BeanUtil.isNotTenser(m)) {
                    Array.set(a, i, func.apply(a));
                } else {
                    fill(m, func);
                }
            });
        }
        return a;
    }

    public static void forEach(Object a, Func1 func) {
        if (BeanUtil.isTenser(a)) {
            forEach(Array.getLength(a), i -> {
                Object m = Array.get(a, i);
                if (BeanUtil.isNotTenser(m)) {
                    func.apply(m);
                } else {
                    forEach(m, func);
                }
            });
        } else {
            func.apply(a);
        }
    }

    public static void forEach(Object a, Object b, Func2 func) {
        if (BeanUtil.isTenser(a)) {
            forEach(Array.getLength(a), i -> {
                Object m = Array.get(a, i), n = Array.get(b, i);
                if (BeanUtil.isNotTenser(m)) {
                    func.apply(m, n);
                } else {
                    forEach(m, n, func);
                }
            });
        } else {
            func.apply(a, b);
        }
    }

    public static void forEach(Object a, Object b, Object c, Func3 func) {
        if (BeanUtil.isTenser(a)) {
            forEach(Array.getLength(a), i -> {
                Object m = Array.get(a, i), n = Array.get(b, i), o = Array.get(c, i);
                if (BeanUtil.isNotTenser(m)) {
                    func.apply((Node) m, (Graph) n, (None) o);
                } else {
                    forEach(m, n, o, func);
                }
            });
        }
    }

    public static void forEach(Object a, Object b, For2 func) {
        if (BeanUtil.isTenser(a)) {
            forEach(Array.getLength(a), i -> {
                Object m = Array.get(a, i), n = Array.get(b, i);
                if (BeanUtil.isNotTenser(m)) {
                    func.apply((None) m, (Tenser[]) b, i);
                } else {
                    forEach(m, n, func);
                }
            });
        }
    }

    public static void forEach(Object a, Object b, Object c, For3 func) {
        if (BeanUtil.isTenser(a)) {
            forEach(Array.getLength(a), i -> {
                Object m = Array.get(a, i), n = Array.get(b, i), o = Array.get(c, i);
                if (BeanUtil.isNotTenser(m)) {
                    func.apply((None) m, (None) n, (Tenser[]) c, i);
                } else {
                    forEach(m, n, o, func);
                }
            });
        }
    }

    @FunctionalInterface
    public interface Range1 {
        void apply(int l);
    }

    @FunctionalInterface
    public interface Range2 {
        void apply(int l, int i);
    }

    @FunctionalInterface
    public interface Range3 {
        void apply(int l, int i, int m);
    }

    @FunctionalInterface
    public interface Fill<N> {
        Object apply(N o);
    }

    @FunctionalInterface
    public interface Func1<N> {
        void apply(N o);
    }

    @FunctionalInterface
    public interface Func2<M, N> {
        void apply(M m, N n);
    }

    @FunctionalInterface
    public interface Func3 {
        void apply(Node m, Graph n, None o);
    }

    @FunctionalInterface
    public interface For2 {
        void apply(None l, Tenser[] n, int i);
    }

    @FunctionalInterface
    public interface For3 {
        void apply(None l, None m, Tenser[] n, int i);
    }
}


