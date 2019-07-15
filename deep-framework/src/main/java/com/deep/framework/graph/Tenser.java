package com.deep.framework.graph;

import com.deep.framework.operation.Node;
import com.deep.framework.operation.None;
import com.deep.framework.util.BeanUtil;
import lombok.Data;

import java.util.Arrays;
import java.util.function.Predicate;

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
        if (input.getGraph() == null) {
            this.name = "None";
            this.output = (N) input;
        } else {
            this.name = "Scalar";
            this.output = (N) input;
            this.graph = (N) input.getGraph();
        }
    }

    public Tenser(String name, Node... input) {
        this.name = this.name.concat("::").concat(name);
        this.input = input;
        setGraph(input);
    }

    public N compute() { return null; }

    public void gradient() { }

    public void setGraph(Node... input) {
        if (BeanUtil.isOperation(this)) {
            graph = (N) new Graph();
            output = (N) new None(0d, graph);
            Predicate<Node> filter = node -> BeanUtil.isNotNone((Tenser) node);
            Arrays.stream(input).filter(filter).forEach(node -> {
                ((Graph) graph).addAll((Graph) node.getGraph());
                ((Graph) graph).add(node);
            });
        } else if (BeanUtil.isNotNone(this)) {
            Object node = compute();
            if (BeanUtil.isArray(node)) {
                Object graphs = graph = Shape.graphs(node);
                Object outputs = output = Shape.nones(node);
                graphs(node, graphs, outputs);
            } else {
                Object[] nodes = {node};
                Object[] graphs = {graph = (N) new Graph()};
                Object[] outputs = {output = (N) new None(0d)};
                graphs(nodes, graphs, outputs);
            }
        }
    }

    private void graphs(Object nodes, Object graphs, Object outputs) {
        Shape.forEach(nodes, graphs, outputs, (node, grap, out) -> {
            grap.addAll((Graph) node.getGraph());
            grap.add(node);
            out.setGraph(grap);
        });
    }

    private Node[] input;
    private N graph;
    private N output;
    private String name = "Tenser";
}
