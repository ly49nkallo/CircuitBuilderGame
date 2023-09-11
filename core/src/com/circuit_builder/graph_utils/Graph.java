package com.circuit_builder.graph_utils;

import com.badlogic.gdx.utils.Array;

public class Graph<T, U> {

    public Array<Node<T>> nodes;
    public Array<Edge<U>> edges;

    public Graph() {
        this.nodes = new Array<Node<T>>();
        this.edges = new Array<Edge<U>>();
    }

    public void addEdge(Edge<U> edge) {
        if (!edges.contains(edge, false))
            this.edges.add(edge);
    }
    public void addNode(Node<T> node) {
        if (!nodes.contains(node, false))
            this.nodes.add(node);
    }
    public boolean isDirected() {
        for (Edge<?> e : edges) {
            if (!e.directed) {
                return false;
            }
        }
        return true;
    }
    public boolean isUndirected() {
        for (Edge<?> e : edges) {
            if (e.directed) {
                return false;
            }
        }
        return true;
    }
    public String repr() {
        return ""
            + "Number of Nodes: " + nodes.size + '\n'
            + "Number of Edges: " + edges.size + '\n';
    }
}
