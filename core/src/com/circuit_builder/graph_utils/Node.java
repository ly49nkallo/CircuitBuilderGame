package com.circuit_builder.graph_utils;

public class Node<T> {
    public T ref;
    public Node(T reference) {
        this.ref = reference;
    }
    public boolean equals(Object o) {
        if (!(o instanceof Node<?>))
            return false;
        Node<?> n = (Node<?>) o;
        return this.ref.equals(n.ref);
    }
}
