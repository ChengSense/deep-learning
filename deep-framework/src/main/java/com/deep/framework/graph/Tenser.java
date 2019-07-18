package com.deep.framework.graph;

import com.deep.framework.operation.Node;
import com.deep.framework.operation.None;
import com.deep.framework.util.BeanUtil;
import lombok.Data;

@Data
public class Tenser<N> implements Node<N> {

    public Tenser(Double input) {
        this.name = "None";
        this.output = (N) new None(input);
    }

    public Tenser(int[] shape) {
        this.name = "None";
        this.output = Shape.random(shape);
    }

    public Tenser(String name, int[] shape) {
        this.name = "None::".concat(name);
        this.output = Shape.random(this.name, shape);
    }

    public Tenser(None input) {
        this.name = input.getName();
        this.output = (N) input;
    }

    public Tenser(String name, Node... input) {
        this.name = this.name.concat(name);
        this.input = input;
        Builder.create(this);
    }

    public N compute() { return null; }

    public void gradient() { }

    public N getOutput(Tenser n) {
        if (BeanUtil.isOperation(n)) return output;
        if (BeanUtil.isNone(this)) return Shape.tensers(output);
        if (BeanUtil.isOperation(this)) return (N) this;
        return function;
    }

    public N getOutput() {
        if (output != null) return output;
        if (function != null) return Shape.nones(function);
        return output;
    }

    private String name = "Tenser::";
    private Node[] input;
    private N function;
    private N output;
}
