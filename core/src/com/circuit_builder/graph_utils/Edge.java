package com.circuit_builder.graph_utils;

public class Edge<T> {
    public boolean directed;
    Node<?> a, b;
    public T reference;
    public Edge(T reference, boolean directed, Node<?> a, Node<?> b){
        this.directed = directed;
        this.a = a;
        this.b = b;
        this.reference = reference;
    }
    public boolean equals(Object o) {
        if (!(o instanceof Edge)) 
            return false;
        Edge<?> e = (Edge<?>) o;
        if (this.directed != e.directed) return false;
        if (this.directed) {
            return (this.a == e.a && this.b == e.b);
        }
        else {
            return (this.a == e.a && this.b == e.b) || (this.a == e.b && this.b == e.a);
        }
    }
}
