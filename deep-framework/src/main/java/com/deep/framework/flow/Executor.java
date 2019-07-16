package com.deep.framework.flow;

import com.deep.framework.graph.Shape;
import com.deep.framework.graph.Tenser;
import com.deep.framework.operation.Node;
import com.deep.framework.operation.None;
import com.deep.framework.util.BeanUtil;
import lombok.Data;

@Data
public class Executor<E> extends Shape {
    private Tenser tenser;

    public Executor(Tenser tenser) {
        this.tenser = tenser;
    }

    private void forward(E[] input) {
        Func1<None> func = none -> {
            none.getGraph().forEach(n -> {
                Node node = (Node) n;
                if (BeanUtil.isOperation(node)) {
                    node.setOutput(node.compute());
                } else {
                    System.out.println(node);
                }
            });
        };
        forEach(tenser.getOutput(), func);
    }

    private void backward() {
        tenser.gradient();
    }

    public void run(E[] input) {
        forward(input);
        backward();
    }
}
