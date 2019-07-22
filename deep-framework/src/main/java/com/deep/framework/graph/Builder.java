package com.deep.framework.graph;

import com.deep.framework.bean.Node;
import com.deep.framework.bean.None;
import com.deep.framework.lang.function.Fill;
import com.deep.framework.lang.function.Func1;
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
            if (BeanUtil.isNotNone(node)) {
                None out = (None) node.getOutput();
                graph.addAll(out.getGraph());
                if (BeanUtil.isOperation(node)) {
                    graph.add(node);
                } else {
                    graph.add(node.getFunction());
                }
            }
        };
        forEach(tenser.getInput(), func);
    }

    public static Object[] build(Tenser tenser) {
        Node[] input = tenser.getInput();
        if (BeanUtil.isNotOperation(tenser)) {
            Fill<Tenser> fill = a -> {
                if (BeanUtil.isNone(a)) {
                    return Shape.tensers(a.getOutput());
                }
                if (BeanUtil.isOperation(a)) {
                    return a;
                }
                return a.getFunction();
            };
            return (Object[]) fill(input, shape(Object.class, input), fill);
        } else {
            Fill<Tenser> fill = a -> {
                return a.getOutput();
            };
            return (Object[]) fill(input, shape(None.class, input), fill);
        }
    }
}
