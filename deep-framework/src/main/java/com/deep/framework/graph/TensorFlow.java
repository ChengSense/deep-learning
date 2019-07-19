package com.deep.framework.graph;

import com.deep.framework.lang.annotation.Operator;
import com.deep.framework.graph.Shape;
import com.deep.framework.graph.Tenser;
import com.deep.framework.bean.Node;
import com.deep.framework.bean.None;


public class TensorFlow extends Shape {

    public Tenser add(Node<None>... input) {
        return new Tenser<None>("Add", input) {

            @Operator
            public None compute() {
                None inx = input[0].getOutput(this), iny = input[1].getOutput(this);
                Double valx = inx.getValue(), valy = iny.getValue();
                return new None(valx + valy);
            }

            public void gradient() {
                None inx = input[0].getOutput(this), iny = input[1].getOutput(this), out = getOutput(this);
                Double grad = out.getGrad();
                inx.setGrad(grad);
                iny.setGrad(grad);
            }

        };
    }

    public Tenser addx(Node... input) {
        return new Tenser("Addx", input) {

            public Object compute() {
                Object A = input[0].getOutput(this), B = input[1].getOutput(this), C = shape(Tenser.class, A);
                forEach(A, B, C, (m, n, o, i) -> {
                    o[i] = add(m, n);
                });
                return C;
            }

            public void gradient() {}

        };
    }

    public Tenser minus(Node<None>... input) {
        return new Tenser<None>("Minus", input) {

            @Operator
            public None compute() {
                if (input.length == 1) {
                    None inx = input[0].getOutput(this);
                    Double valx = inx.getValue();
                    return new None(-valx);
                } else {
                    None inx = input[0].getOutput(this), iny = input[1].getOutput(this);
                    Double valx = inx.getValue(), valy = iny.getValue();
                    return new None(valx - valy);
                }

            }

            public void gradient() {
                if (input.length == 1) {
                    None inx = input[0].getOutput(this), out = getOutput(this);
                    Double grad = out.getGrad();
                    inx.setGrad(-grad);
                } else {
                    None inx = input[0].getOutput(this), iny = input[1].getOutput(this), out = getOutput(this);
                    Double grad = out.getGrad();
                    inx.setGrad(grad);
                    iny.setGrad(-grad);
                }
            }

        };
    }

    public Tenser mul(Node<None>... input) {
        return new Tenser<None>("Mul", input) {

            @Operator
            public None compute() {
                None inx = input[0].getOutput(this), iny = input[1].getOutput(this);
                Double valx = inx.getValue(), valy = iny.getValue();
                return new None(valx * valy);
            }

            public void gradient() {
                None inx = input[0].getOutput(this), iny = input[1].getOutput(this), out = getOutput(this);
                Double valx = inx.getValue(), valy = iny.getValue();
                Double grad = out.getGrad();
                inx.setGrad(grad * valy);
                iny.setGrad(grad * valx);
            }

        };
    }

    public Tenser div(Node<None>... input) {
        return new Tenser<None>("Div", input) {

            @Operator
            public None compute() {
                None inx = input[0].getOutput(this), iny = input[1].getOutput(this);
                Double valx = inx.getValue(), valy = iny.getValue();
                return new None(valx / valy);
            }

            public void gradient() {
                None inx = input[0].getOutput(this), iny = input[1].getOutput(this), out = getOutput(this);
                Double valx = inx.getValue(), valy = iny.getValue();
                Double grad = out.getGrad();
                inx.setGrad(grad * valy / Math.pow(valy, 2));
                iny.setGrad(-grad * valx / Math.pow(valy, 2));
            }

        };
    }

    public Tenser exp(Node<None>... input) {
        return new Tenser<None>("Exp", input) {

            @Operator
            public None compute() {
                None inx = input[0].getOutput(this);
                Double valx = inx.getValue();
                return new None(Math.exp(valx));
            }

            public void gradient() {
                None inx = input[0].getOutput(this), out = getOutput(this);
                Double valx = inx.getValue();
                Double grad = out.getGrad();
                inx.setGrad(grad * Math.exp(valx));
            }

        };
    }

    public Tenser pow(Node<None>... input) {
        return new Tenser<None>("Pow", input) {

            @Operator
            public None compute() {
                None inx = input[0].getOutput(this), iny = input[1].getOutput(this);
                Double valx = inx.getValue(), valy = iny.getValue();
                return new None(Math.pow(valx, valy));
            }

            public void gradient() {
                None inx = input[0].getOutput(this), iny = input[1].getOutput(this);
                Double valx = inx.getValue(), valy = iny.getValue();
                inx.setGrad(valy * Math.pow(valx, valy - 1));
            }

        };
    }

    public Tenser matmul(Node<Node[][]>... input) {
        return new Tenser<Node[][]>("Matmul", input) {

            public Node[][] compute() {
                Node[][] A = input[0].getOutput(this), B = input[1].getOutput(this);
                Node[][] C = zeros(new Node[A.length][B[0].length]);
                forEach(A.length, B[0].length, A[0].length, (i, l, j) -> {
                    C[i][l] = add(C[i][l], mul(A[i][j], B[j][l]));
                });
                return C;
            }

            public void gradient() {}

        };
    }

    public Tenser prod(Node node) {
        return new Tenser<Node>("Prod", node) {

            public Node compute() {
                return null;
            }

            public void gradient() {}

        };
    }

    public Tenser sigmoid(Node<Tenser> input) {
        return new Tenser<Node>("Sigmoid", input) {

            public Node compute() {
                Tenser A = input.getOutput(this);
                return div(new Tenser(1d), add(new Tenser(1d), exp(minus(A))));
            }

            public void gradient() {}

        };
    }

    public Tenser sigmoidx(Node<None> input) {
        return new Tenser("Sigmoidx", input) {

            public Object compute() {
                Object A = input.getOutput(this), B = shape(Tenser.class, A);
                forEach(A, B, (m, o, i) -> {
                    o[i] = sigmoid(m) ;
                });
                return B;
            }

            public void gradient() {}

        };
    }

    public Tenser square(Node<None>... input) {
        return new Tenser<Node>("Square", input) {

            public Node compute() {
                return mul(new Tenser(0.5), pow(minus(input[0], input[1]), new Tenser(2d)));
            }

            public void gradient() {}

        };
    }

    public Tenser squarex(Node<None>... input) {
        return new Tenser("Squarex", input) {

            public Object compute() {
                Object A = input[0].getOutput(this), B = input[1].getOutput(this), C = shape(Tenser.class, A);
                forEach(A, B, C, (a, b, c, i) -> {
                    c[i] = square(a, b);
                });
                return C;
            }

            public void gradient() {}

        };
    }

    public Tenser sum(Node<None> input) {
        return new Tenser<Node>("Sum", input) {

            public Node compute() {
                Object A = input.getOutput(this);
                Node[] B = {new Tenser<None>(0d)};
                forEach(A,  a -> {
                    B[0] = add((Tenser) a, B[0]);
                });
                return B[0];
            }

            public void gradient() {}

        };
    }

    public Tenser conv(Node node) {
        return new Tenser<Node>("Conv", node) {

            public Node compute() {
                return null;
            }

            public void gradient() {}

        };
    }

    public Tenser relu(Node node) {
        return new Tenser<Node>("Relu", node) {

            public Node compute() {
                return null;
            }

            public void gradient() {}

        };
    }

    public Tenser maxpool(Node node) {
        return new Tenser<Node>("Maxpool", node) {

            public Node compute() {
                return null;
            }

            public void gradient() {}

        };
    }

    public Tenser softmax(Node node) {
        return new Tenser<Node>("Softmax", node) {

            public Node compute() {
                return null;
            }

            public void gradient() {}

        };
    }

}
