package com.deep.framework.graph;

import com.deep.framework.function.Func1;
import com.deep.framework.operation.None;
import com.deep.framework.util.BeanUtil;

public class Builder extends Shape {

    public static void create(Tenser tenser) {
        if (BeanUtil.isNotOperation(tenser)) {
            Object node = tenser.compute();
            if (BeanUtil.isTenser(node)) {
                tenser.setOutput(nones(node));
            } else {
                Tenser<None> o = (Tenser) node;
                if (BeanUtil.isOperation(o))
                    o.getOutput().getGraph().add(o);
                tenser.setOutput(((Tenser) node).getOutput());
            }
        } else {
            operator(tenser);
        }
    }

    private static void operator(Tenser tenser) {
        String name = tenser.getName().replace("Tenser::", "Operator::");
        None none = new None(0d, name);
        Graph graph = none.getGraph();
        tenser.setOutput(none);
        Func1<Tenser> func = node -> {
            if (BeanUtil.isNone(node)) return;
            None out = (None) node.getOutput();
            graph.addAll(out.getGraph());
            if (BeanUtil.isOperation(node))
                graph.add(node);
        };
        forEach(tenser.getInput(), func);
    }
}
