package com.deep.framework.function;

import com.deep.framework.graph.Tenser;
import com.deep.framework.operation.None;

@FunctionalInterface
public interface For2 {
    void apply(None l, Tenser[] n, int i);
}

