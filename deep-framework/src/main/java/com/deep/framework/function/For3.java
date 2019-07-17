package com.deep.framework.function;

import com.deep.framework.graph.Tenser;
import com.deep.framework.operation.None;

@FunctionalInterface
public interface For3 {
    void apply(None l, None m, Tenser[] n, int i);
}