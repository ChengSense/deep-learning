package com.deep.framework.function;

@FunctionalInterface
public interface Fill<N> {
    Object apply(N o);
}
