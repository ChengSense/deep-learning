package com.deep.framework.graph;

import com.deep.framework.lang.function.Func1;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Graph<E> extends ConcurrentLinkedDeque<E> {

    public boolean add(E obj) {
        if (obj == null)
            return false;
        return super.add(obj);
    }

    public void farEach(Func1<E> func1) {
        Iterator iter = this.descendingIterator();
        while (iter.hasNext()) {
            func1.apply((E) iter.next());
        }
    }

}
