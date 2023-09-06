package com.circuit_builder.graph_utils;

import com.badlogic.gdx.utils.Array;

public class Graph {

    public Array<Node<?>> nodes;
    public Array<Edge> edges;

    public Graph() {
        this.nodes = new Array<Node<?>>();
        this.edges = new Array<Edge>();
    }

    public void addEdge(Edge edge) {
        if (!edges.contains(edge, false))
            this.edges.add(edge);
    }
    public void addEdges(Array<Edge> new_edges) {
        for (Edge e : new_edges) {
            if (!edges.contains(e, false))
                this.edges.add(e);
        }
    }
    public void addNode(Node<?> node) {
        if (!nodes.contains(node, false))
            this.nodes.add(node);
    }
    public void addNodes(Array<Node<?>> new_nodes) {
        for (Node<?> n : new_nodes) {
            if (!nodes.contains(n, false))
                this.nodes.add(n);
        }
    }
    public boolean isDirected() {
        for (Edge e : edges) {
            if (!e.directed) {
                return false;
            }
        }
        return true;
    }
    public boolean isUndirected() {
        for (Edge e : edges) {
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
