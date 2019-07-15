package com.deep.framework.operation;

import lombok.Data;

@Data
public class None<N> {

    public None(Double value) {
        this.value = value;
    }

    public None(Double value, N graph) {
        this.value = value;
        this.graph = graph;
    }

    private Double value;
    private Double grad = 1d;
    private N graph;

}
