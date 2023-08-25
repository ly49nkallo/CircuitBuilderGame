package com.circuit_builder.tree_utils;

public class Parellel extends Node {
    
    public Parellel() {
        super();
        this.name = "Parellel";
    }

    public String repr() {
        return super.repr(1);
    }

    public void solve() {
        // 1/R = 1/R_1 + 1/R_2 ...
        {
            boolean b = true;
            for (Node child : children)
                if (child.R < 0)
                    b = false;
            if (b && (this.R < 0)) {
                    double sum = 0;
                    for (Node child : children) {
                        sum += 1 / child.R;
                    }
                    this.R = 1 / sum;
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
                            sum += 1 / child1.R;
                    }
                    child.R = (1 / ( 1 / this.R)) - sum;
                }
            }
        }
        // V = V_1 = V_2 ...
        {
            boolean b = false;
            for (Node child : children) {
                if (!(child.V < 0))
                    b = true;
            }
            if (b && (this.V < 0)) {
                double i = -1;
                for (Node child : children) {
                    if (!(child.V < 0))
                        i = child.V;
                }
                this.V = i;
            }

            if (!(this.V < 0)) {
                for (Node child : children) {
                    if (!(child.V < 0)) continue;
                    child.V = this.V;
                }
            }
        }
        // I = I_1 + I_2 ...
        {
            boolean b = true;
            for (Node child : children) {
                if (child.I < 0)
                    b = false;
            }
            if (b && (this.I < 0)) {
                double sum = 0;
                for (Node child : children) {
                    sum += child.I;
                }
                this.I = sum;
            }

            // one unknown child
            int flag = 0;
            for (Node child : children) {
                if (!(child.I < 0))
                    flag++;
            }
            if (flag == 1 && (this.I < 0)) {
                for (Node child : children) {
                    if (!(child.I < 0)) continue;
                    double sum = 0;
                    for (Node child1 : children) {
                        if (!(child1.I < 0))
                            sum += child1.I;
                    }
                    child.I = this.I - sum;
                }
            }
        }
        super.solve();
    }
}
