package com.deep.framework.graph;

import com.deep.framework.lang.function.Func1;
import com.deep.framework.bean.None;
import com.deep.framework.lang.util.BeanUtil;

public class Builder extends Shape {

    public static void create(Tenser tenser) {
        if (BeanUtil.isNotOperation(tenser)) {
            Object function = tenser.compute();
            if (BeanUtil.isNotTenser(function)) {
                Tenser tense = (Tenser) function;
                if (BeanUtil.isNotOperation(tense)) {
                    tenser.setFunction(tense.getFunction());
                } else {
                    tenser.setFunction(function);
                }
            } else {
                tenser.setFunction(functions(function));
            }
        } else {
            operator(tenser);
        }
    }

    private static void operator(Tenser tenser) {
        None none = new None(0d);
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

    public static void build(Tenser tenser) {
        if (BeanUtil.isNotOperation(tenser)) {
            Object function = tenser.compute();
            if (BeanUtil.isTenser(function)) {
                tenser.setFunction(functions(function));
            } else {
                if (BeanUtil.isNotOperation((Tenser) function)) {
                    tenser.setFunction(((Tenser) function).getFunction());
                } else {
                    tenser.setFunction(function);
                }
            }
        } else {
            operator(tenser);
        }
    }
}
