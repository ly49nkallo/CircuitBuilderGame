package com.circuit_builder.tree_utils;

import com.badlogic.gdx.utils.Array;
import com.circuit_builder.game.Component;

public abstract class Node {

    public Array<Node> children;
    public Component reference;
    public String name;

    public double I, R, V;

    public Node() {
        children = new Array<Node>();
        this.reference = null;
        I = -1; R = -1; V = -1;
    }

    public Node(Component reference) {
        children = new Array<Node>();
        this.reference = reference;
        I = -1; R = -1; V = -1;
    }

    public void add(Node child) {
        children.add(child);
    }

    public boolean isLeaf() {
        return (children.size > 0);
    }

    public void solve() {
        int flag = 0;
        if (this.R < 0) flag++;
        if (this.V < 0) flag++;
        if (this.I < 0) flag++;
        if (flag == 1) {
            if (this.R < 0) {
                this.R  = this.V / this.I;
            }
            else if (this.I < 0) {
                this.I = this.V / this.R;
            }
            else if (this.V < 0) {
                this.V = this.I * this.R;
            }
        }
        for (Node child : this.children)
            child.solve();
    }

    public boolean solved() {
        for (Node child : children) {
            if (!child.solved())
                return false;
        }
        if (this.I < 0 || this.R < 0 || this.V < 0)
            return false;
        return true;
    }

    public String repr(int tabs) {
        String out = this.name + "(";
        if (reference.name != null) out += "name = " + reference.name;
        if (this.R > 0) out += "R = " + this.R;
        if (this.V > 0) out += "V = " + this.V;
        if (this.I > 0) out += "I = " + this.I;
        if (this.children.size != 0) out += '\n';
        for (Node child: this.children) {
            int i = 0;
            while (true) {
                if (!(i < this.children.size)) break;
                out += "    ";
                i++;
            }
            out += child.repr(tabs + 1);
            out += '\n';
        }
        if (this.children.size != 0) {
            int i = 0;
            while (true) {
                if (!(i < this.children.size)) break;
                out += "    ";
                i++;
            }
        }
        out += ')';
        return out;
    }
}
