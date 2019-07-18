package com.deep.framework.operation;

import com.deep.framework.graph.Tenser;

public interface Node<N> {
    N compute();

    void gradient();

    N getOutput(Tenser o);

    void setOutput(N o);
}
