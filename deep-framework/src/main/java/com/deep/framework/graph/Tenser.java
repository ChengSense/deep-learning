package com.deep.framework.graph;

import com.deep.framework.operation.Node;
import com.deep.framework.operation.None;
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

    public Tenser(None input) {
        this.name = this.name.concat("None");
        this.output = (N) input;
    }

    public Tenser(String name, Node... input) {
        this.name = this.name.concat(name);
        this.input = input;
        Builder.create(this);
    }

    public N compute() { return null; }

    public void gradient() { }

    private String name = "Tenser::";
    private Node[] input;
    private N output;
}
