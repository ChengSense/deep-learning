package com.deep.framework.graph;

import com.deep.framework.operation.Node;
import com.deep.framework.operation.None;
import com.deep.framework.util.BeanUtil;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class Builder extends Shape {

    public static void create(Tenser tenser) {
        if (BeanUtil.isNotOperation(tenser)) {
            Object node = tenser.compute();
            if (BeanUtil.isTenser(node)) {
                tenser(tenser, node);
            } else {
                scalar(tenser, (Tenser) node);
            }
        } else {
            operation(tenser);
        }
    }

    static void operation(Tenser tenser) {
        None none = new None(0d);
        Graph graph = none.getGraph();
        tenser.setOutput(none);
        Func1<Tenser> func = node -> {
            None out = (None) node.getOutput();
            graph.addAll(out.getGraph());
            if (BeanUtil.idNotTenserNone(node))
                graph.add(node);
        };
        Predicate<Node> filter = node -> BeanUtil.isNotNone((Tenser) node);
        forEach(Stream.of(tenser.getInput()).filter(filter).toArray(), func);
    }

    static void tenser(Tenser tenser, Object obj) {
        Object nones = Shape.nones(obj);
        tenser.setOutput(nones);
        Func2<Node, None> func = (node, none) -> {
            Graph graph = none.getGraph();
            None out = (None) node.getOutput();
            graph.addAll(out.getGraph());
            graph.add(node);
        };
        forEach(obj, nones, func);
    }

    static void scalar(Tenser tenser, Tenser node) {
        None none = new None(0d);
        Graph graph = none.getGraph();
        tenser.setOutput(none);

        None out = (None) node.getOutput();
        graph.addAll(out.getGraph());
        graph.add(node);
    }

}
