package com.deep.framework.graph;

import com.deep.framework.operation.Node;
import com.deep.framework.operation.None;
import com.deep.framework.util.BeanUtil;

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
            operator(tenser);
        }
    }

    static void operator(Tenser tenser) {
        None none = new None(0d);
        Graph graph = none.getGraph();
        tenser.setOutput(none);
        Func1<Tenser> func = node -> {
            if (BeanUtil.isNone(node)) return;
            None out = (None) node.getOutput();
            graph.addAll(out.getGraph());
            graph.add(node);
        };
        forEach(tenser.getInput(), func);
    }

    static void tenser(Tenser tenser, Object obj) {
        Object nones = Shape.nones(obj, tenser.getName());
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
