package com.deep.framework.flow;

import com.alibaba.fastjson.JSONObject;
import com.deep.framework.graph.Graph;
import com.deep.framework.graph.Shape;
import com.deep.framework.graph.Tenser;
import com.deep.framework.operation.Node;
import com.deep.framework.operation.None;
import com.deep.framework.util.BeanUtil;
import lombok.Data;

@Data
public class Engine extends Shape {

    public void forward(Tenser tenser) {
        Func1<None> func = out -> {
            out.getGraph().forEach(o -> {
                _forward((Tenser) o);
            });
        };
        forEach(tenser.getOutput(), func);
    }

    public void backward(Tenser tenser) {
        Func1<None> func = out -> {
            out.getGraph().forEach(o -> {
                _backward((Tenser) o);
            });
        };
        forEach(tenser.getOutput(), func);
    }

    private void _forward(Tenser tenser) {
        execute(tenser, o -> {
            tenserForward((Tenser) o);
        }, o -> {
            scalarForward((Tenser) o);
        }, o -> {
            operatorForward((Tenser) o);
        });
    }

    public void _backward(Tenser tenser) {
        execute(tenser, o -> {
            tenserBackward((Tenser) o);
        }, o -> {
            scalarBackward((Tenser) o);
        }, o -> {
            operatorBackward((Tenser) o);
        });
    }

    private void tenserForward(Tenser tenser) {
        Object output = tenser.getOutput();
        Func1<Node<None>> func = (node) -> {
            None none = node.getOutput();
            Graph<Tenser> graph = none.getGraph();
            Tenser<None> last = graph.getLast();
            none.setValue(last.getOutput().getValue());
        };
        forEach(output, func);
    }

    private void scalarForward(Tenser<None> tenser) {
        None none = tenser.getOutput();
        Graph<Tenser> graph = none.getGraph();
        Tenser<None> last = graph.getLast();
        none.setValue(last.getOutput().getValue());
    }

    private void operatorForward(Tenser<None> tenser) {
        None[] nones = {tenser.compute()};
        None[] outputs = {tenser.getOutput()};
        Func2<None, None> func = (none, out) -> {
            out.setValue(none.getValue());
        };
        forEach(nones, outputs, func);
    }


    private void tenserBackward(Tenser tenser) {
        Object output = tenser.getOutput();
        Func1<Node<None>> func = (node) -> {
            None none = node.getOutput();
            Graph<Tenser> graph = none.getGraph();
            Tenser<None> last = graph.getLast();
            none.setValue(last.getOutput().getValue());
        };
        forEach(output, func);
    }

    private void scalarBackward(Tenser<None> tenser) {
        None none = tenser.getOutput();
        Graph<Tenser> graph = none.getGraph();
        Tenser<None> last = graph.getLast();
        none.setValue(last.getOutput().getValue());
    }

    private void operatorBackward(Tenser<None> tenser) {
        None[] nones = {tenser.compute()};
        None[] outputs = {tenser.getOutput()};
        Func2<None, None> func = (none, out) -> {
            out.setValue(none.getValue());
        };
        forEach(nones, outputs, func);
    }

    private void execute(Tenser tenser, Func1<Node>... func) {
        if (BeanUtil.isNotOperation(tenser)) {
            if (BeanUtil.isTenser(tenser.getOutput())) {
                func[0].apply(tenser);
            } else {
                func[1].apply(tenser);
            }
        } else {
            func[2].apply(tenser);
        }
    }

    public void toString(Tenser tenser) {
        System.out.println(JSONObject.toJSONString(tenser));
    }

}
