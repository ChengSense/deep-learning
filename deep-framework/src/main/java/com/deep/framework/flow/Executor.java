package com.deep.framework.flow;

import com.deep.framework.graph.Tenser;
import lombok.Data;

@Data
public class Executor<E> extends Engine {
    private Tenser tenser;

    public Executor(Tenser tenser) {
        this.tenser = tenser;
    }

    public void run(E[] input) {
        forward(tenser);
        toString(tenser);
        backward(tenser);
        toString(tenser);
    }

}
