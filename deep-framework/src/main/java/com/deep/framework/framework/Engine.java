package com.deep.framework.framework;

import com.deep.framework.bean.None;
import com.deep.framework.graph.Shape;
import com.deep.framework.graph.Tenser;
import com.deep.framework.lang.function.Func1;
import com.deep.framework.lang.function.Func2;
import com.deep.framework.lang.util.BeanUtil;
import lombok.Data;

@Data
public class Engine extends Shape {
    public static double rate = 0.0003;

    public void forward(Tenser tenser) {
        execute(tenser, a -> {
            Func1<None> func = out -> {
                out.getGraph().forEach(o -> {
                    _forward(o);
                });
            };
            forEach(a.getOutput(), func);
            _forward(a);
        }, a -> {
            Func1<Tenser<None>> func = node -> {
                node.getOutput().getGraph().forEach(o -> {
                    _forward(o);
                });
                _forward(node);
            };
            forEach(a.getFunction(), func);
        });
    }

    private void _forward(Tenser<None> tenser) {
        None nones = tenser.compute(), outputs = tenser.getOutput();
        Func2<None, None> func = (none, out) -> {
            out.setValue(none.getValue());
        };
        forEach(nones, outputs, func);
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
            Func1<Tenser<None>> func = node -> {
                node.gradient();
                node.getOutput().getGraph().farEach(o -> {
                    o.gradient();
                });
            };
            forEach(a.getFunction(), func);
        });

        _backward(tenser);
    }

    public void _backward(Tenser tenser) {
        execute(tenser, a -> {
            reduce(a);
            Func1<None> func = out -> {
                out.getGraph().farEach(o -> {
                    reduce(o);
                });
            };
            forEach(a.getOutput(), func);
        }, a -> {
            Func1<Tenser<None>> func = node -> {
                reduce(node);
                node.getOutput().getGraph().farEach(o -> {
                    reduce(o);
                });
            };
            forEach(a.getFunction(), func);
        });
    }

    public void reduce(Tenser tenser) {
        Func1<Tenser> func = node -> {
            if (BeanUtil.startsWithNone(node)) {
                forEach(node.getOutput(), (Func1<None>) a -> {
                    Double value = a.getValue();
                    a.setValue(value - rate * a.getGrad());
                    a.setGrad(null);
                });
            }
        };
        forEach(tenser.getInput(), func);
    }

    public void execute(Tenser tenser, Func1<Tenser>... func) {
        if (BeanUtil.isOperation(tenser)) {
            func[0].apply(tenser);
        } else {
            func[1].apply(tenser);
        }
    }
}
