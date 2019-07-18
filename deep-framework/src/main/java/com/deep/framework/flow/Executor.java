package com.deep.framework.flow;

import com.deep.framework.function.Func2;
import com.deep.framework.graph.Tenser;
import com.deep.framework.operation.None;
import lombok.Data;

@Data
public class Executor<E> extends Engine {
    private Tenser tenser;
    private Tenser input, label;
    private Func2<None, Double> func = (m, n) -> {
        m.setValue(n);
    };

    public Executor(Tenser tenser) {
        this.tenser = tenser;
    }

    public Executor(Tenser tenser, Tenser input, Tenser label) {
        this.tenser = tenser;
        this.input = input;
        this.label = label;
    }

    public void init(Object inSet, Object labSet) {
        forEach(input.getOutput(), inSet, func);
        forEach(label.getOutput(), labSet, func);
    }

    public void run(E inputSet, E labelSet) {
        each(inputSet, labelSet, (inSet, labSet) -> {
            init(inSet, labSet);
            run();
        });
    }

    public void run() {
        forward(tenser);
        backward(tenser);
        toString(tenser);
    }

}
