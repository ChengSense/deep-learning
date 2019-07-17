package com.deep.framework.function;

import com.deep.framework.graph.Graph;
import com.deep.framework.operation.Node;
import com.deep.framework.operation.None;

@FunctionalInterface
public interface Func3 {
    void apply(Node m, Graph n, None o);
}
