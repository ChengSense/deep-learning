package com.deep.framework.bean;

import com.deep.framework.graph.Tenser;

public interface Node<N> {
    <M> M compute();

    void gradient();

    N getInput(int i);

    N getOutput(Tenser o);

    void setOutput(N o);
}
