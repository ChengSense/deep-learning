package com.deep.framework.operation;

import com.deep.framework.graph.Graph;
import lombok.Data;

@Data
public class None {

    public None(Double value) {
        this.value = value;
        this.graph = new Graph();
    }

    private Double value;
    private Double grad = 1d;
    private Graph graph;

}
