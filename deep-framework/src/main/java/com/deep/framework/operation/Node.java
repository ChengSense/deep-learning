package com.deep.framework.operation;

public interface Node<N> {
    N compute();

    void gradient();

    N getOutput();

    void setOutput(N o);
}
