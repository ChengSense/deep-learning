package com.deep.framework.flow;

import com.alibaba.fastjson.JSONObject;
import com.deep.framework.function.Func1;
import com.deep.framework.function.Func2;
import com.deep.framework.graph.Shape;
import com.deep.framework.graph.Tenser;
import com.deep.framework.operation.None;
import com.deep.framework.util.BeanUtil;
import lombok.Data;

@Data
public class Engine extends Shape {

    public void forward(Tenser tenser) {
        if (BeanUtil.isOperation(tenser)) {
            Func1<None> func = out -> {
                out.getGraph().forEach(o -> {
                    _forward(o);
                });
            };
            forEach(tenser.getOutput(), func);
        } else {
            Func1<Tenser<None>> func = node -> {
                node.getOutput().getGraph().forEach(o -> {
                    _forward(o);
                });
                _forward(node);
            };
            forEach(tenser.getFunction(), func);
        }
    }

    private void _forward(Tenser<None> tenser) {
        None nones = tenser.compute(), outputs = tenser.getOutput();
        Func2<None, None> func = (none, out) -> {
            out.setValue(none.getValue());
        };
        forEach(nones, outputs, func);
    }

    public void backward(Tenser tenser) {
        if (BeanUtil.isOperation(tenser)) {
            tenser.gradient();
            Func1<None> func = out -> {
                out.getGraph().farEach(o -> {
                    o.gradient();
                });
            };
            forEach(tenser.getOutput(), func);
        } else {
            Func1<Tenser<None>> func = node -> {
                node.gradient();
                node.getOutput().getGraph().farEach(o -> {
                    o.gradient();
                });
            };
            forEach(tenser.getFunction(), func);
        }
    }

    public void toString(Tenser tenser) {
        System.out.println(JSONObject.toJSONString(tenser));
    }

}
