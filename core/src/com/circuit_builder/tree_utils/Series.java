package com.circuit_builder.tree_utils;

public class Series extends Node {
    
    public Series() {
        super();
    }

    public String repr() {
        return super.repr(1);
    }

    public void solve() {
        // R = R_1 + R_2 ...
        {
            boolean b = true;
            for (Node child : children)
                if (child.R < 0)
                    b = false;
            if (b && (this.R < 0)) {
                    double sum = 0;
                    for (Node child : children) {
                        sum += child.R;
                    }
                    this.R = sum;
            }

            // one unknown child
            int flag = 0;
            for (Node child : children) {
                if (child.R < 0) flag++;
            }
            if (flag == 1 && !(this.R < 0)) {
                for (Node child : children) {
                    if (!(child.R < 0))
                        continue;
                    double sum = 0;
                    for (Node child1 : children) {
                        if (!(child1.R < 0))
                            sum += child1.R;
                    }
                    child.R = this.R - sum;
                }
            }
        }
        // I = I_1 = I_2 ...
        {
            boolean b = false;
            for (Node child : children) {
                if (!(child.I < 0))
                    b = true;
            }
            if (b && (this.I < 0)) {
                double i = -1;
                for (Node child : children) {
                    if (!(child.I < 0))
                        i = child.I;
                }
                this.I = i;
            }

            if (!(this.I < 0)) {
                for (Node child : children) {
                    if (!(child.I < 0)) continue;
                    child.I = this.I;
                }
            }
        }
        // V = V_1 + V_2 ... 
        {
            boolean b = true;
            for (Node child : children) {
                if (child.V < 0)
                    b = false;
            }
            if (b && (this.V < 0)) {
                double sum = 0;
                for (Node child : children) {
                    sum += child.V;
                }
                this.V = sum;
            }

            // one unknown child
            int flag = 0;
            for (Node child : children) {
                if (!(child.V < 0))
                    flag++;
            }
            if (flag == 1 && (this.V < 0)) {
                for (Node child : children) {
                    if (!(child.V < 0)) continue;
                    double sum = 0;
                    for (Node child1 : children) {
                        if (!(child1.V < 0))
                            sum += child1.V;
                    }
                    child.V = this.V - sum;
                }
            }
        }
        super.solve();
    }
}
