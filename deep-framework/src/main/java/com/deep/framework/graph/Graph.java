package com.deep.framework.graph;

import java.util.concurrent.ConcurrentLinkedDeque;

public class Graph<E> extends ConcurrentLinkedDeque<E> {

    public boolean add(E obj) {
        if (obj == null) return false;
        return super.add(obj);
    }

}
