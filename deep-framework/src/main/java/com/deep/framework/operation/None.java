package com.deep.framework.operation;

import com.deep.framework.graph.Graph;
import lombok.Data;

@Data
public class None {

    public None(Double value) {
        this.value = value;
        this.name = "None";
        this.graph = new Graph();
    }

    public None(Double value, String name) {
        this.value = value;
        this.name = name;
        this.graph = new Graph();
    }

    private String name;
    private Double value;
    private Double grad = 1d;
    private Graph graph;

}
