package com.deep.framework.flow;

import com.alibaba.fastjson.JSONObject;
import com.deep.framework.function.Func1;
import com.deep.framework.function.Func2;
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
        execute(tenser, a -> {
            Func1<None> func = out -> {
                out.getGraph().forEach(o -> {
                    _forward(o);
                });
            };
            forEach(a.getOutput(), func);
            operatorForward((Tenser<None>) a);
        }, a -> {
            Func1<None> func = out -> {
                out.getGraph().forEach(o -> {
                    _forward(o);
                });
                scalarForward(out);
            };
            forEach(a.getOutput(), func);
        });
    }

    public void backward(Tenser tenser) {
        execute(tenser, a -> {
            a.gradient();
            Func1<None> func = out -> {
                out.getGraph().farEach(o -> {
                    o.gradient();
                });
            };
            forEach(a.getOutput(), func);
        }, a -> {
            Func1<None> func = (out) -> {
                out.getGraph().farEach(o -> {
                    o.gradient();
                });
            };
            forEach(a.getOutput(), func);
        });
    }

    private void _forward(Tenser tenser) {
        execute(tenser, o -> {
            operatorForward((Tenser) o);
        }, o -> {
            scalarForward((None) o.getOutput());
        });
    }

    private void operatorForward(Tenser<None> tenser) {
        None nones = tenser.compute(), outputs = tenser.getOutput();
        Func2<None, None> func = (none, out) -> {
            out.setValue(none.getValue());
        };
        forEach(nones, outputs, func);
    }

    private void scalarForward(None none) {
        Graph<Tenser> graph = none.getGraph();
        Tenser<None> last = graph.getLast();
        none.setValue(last.getOutput().getValue());
    }

    private void execute(Tenser tenser, Func1<Node>... func) {
        if (BeanUtil.isOperation(tenser)) {
            if (func.length > 0) {
                func[0].apply(tenser);
            }
        } else {
            if (func.length > 1) {
                func[1].apply(tenser);
            }
        }
    }

    public void toString(Tenser tenser) {
        System.out.println(JSONObject.toJSONString(tenser));
    }

}
