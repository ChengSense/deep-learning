package com.deep.framework.flow;

import com.alibaba.fastjson.JSONObject;
import com.deep.framework.graph.Graph;
import com.deep.framework.graph.Shape;
import com.deep.framework.graph.Tenser;
import com.deep.framework.operation.Node;
import com.deep.framework.util.BeanUtil;
import lombok.Data;

@Data
public class Executor<E> extends Shape {
    private Tenser tenser;

    public Executor(Tenser tenser) {
        this.tenser = tenser;
    }

    private void forward(E[] input) {
        forEach(tenser.getGraph(), (Graph<Node> graph) -> {
            graph.forEach(node -> {
                if (BeanUtil.isOperation(node)) {
                    node.setOutput(node.compute());
                } else {
                    Graph grap = (Graph) node.getGraph();
                    Tenser tenser = (Tenser) grap.getLast();
                    node.setOutput(tenser.getOutput());
                }
            });
        });
        toString(tenser);
    }

    private void backward() {
        tenser.gradient();
    }

    public void run(E[] input) {
        forward(input);
        backward();
    }

    public void toString(Tenser tenser) {
        System.out.println(JSONObject.toJSONString(tenser));
    }
}
