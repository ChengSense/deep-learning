package com.deep.framework.flow;

import com.deep.framework.annotation.Operation;
import com.deep.framework.graph.Shape;
import com.deep.framework.graph.Tenser;
import com.deep.framework.operation.Node;
import com.deep.framework.operation.None;


public class TensorFlow extends Shape {

    public Tenser add(Node<None>... input) {
        return new Tenser<None>("Add", input) {

            @Operation
            public None compute() {
                None inx = input[0].getOutput(), iny = input[1].getOutput();
                Double valx = inx.getValue(), valy = iny.getValue();
                return new None(valx + valy);
            }

            public void gradient() {
                None inx = input[0].getOutput(), iny = input[1].getOutput(), out = (None) getOutput();
                Double grad = out.getGrad();
                inx.setGrad(grad);
                iny.setGrad(grad);
            }

        };
    }

    public Tenser addx(Node... input) {
        return new Tenser("Addx", input) {

            public Object compute() {
                Object A = input[0].getOutput(), B = input[1].getOutput(), C = shape(Tenser.class, A);
                forEach(A, B, C, (m, n, o, i) -> {
                    o[i] = add(new Tenser(m), new Tenser(n));
                });
                return C;
            }

            public void gradient() {}

        };
    }

    public Tenser minus(Node<None>... input) {
        return new Tenser<None>("Minus", input) {

            @Operation
            public None compute() {
                if (input.length == 1) {
                    None inx = input[0].getOutput();
                    Double valx = inx.getValue();
                    return new None(-valx);
                } else {
                    None inx = input[0].getOutput(), iny = input[1].getOutput();
                    Double valx = inx.getValue(), valy = iny.getValue();
                    return new None(valx - valy);
                }

            }

            public void gradient() {
                if (input.length == 1) {
                    None inx = input[0].getOutput(), out = getOutput();
                    Double gradx = -out.getGrad();
                    inx.setGrad(gradx);
                } else {
                    None inx = input[0].getOutput(), iny = input[1].getOutput();
                    Double valx = inx.getValue(), valy = iny.getValue();
                    inx.setGrad(valx);
                    iny.setGrad(valy);
                }
            }

        };
    }

    public Tenser mul(Node<None>... input) {
        return new Tenser<None>("Mul", input) {

            @Operation
            public None compute() {
                None inx = input[0].getOutput(), iny = input[1].getOutput();
                Double valx = inx.getValue(), valy = iny.getValue();
                return new None(valx * valy);
            }

            public void gradient() {
                None inx = input[0].getOutput(), iny = input[1].getOutput(), out = getOutput();
                Double valx = inx.getValue(), valy = iny.getValue();
                Double grad = out.getGrad();
                inx.setGrad(grad * valy);
                iny.setGrad(grad * valx);
            }

        };
    }

    public Tenser div(Node<None>... input) {
        return new Tenser<None>("Div", input) {

            @Operation
            public None compute() {
                None inx = input[0].getOutput(), iny = input[1].getOutput();
                Double valx = inx.getValue(), valy = iny.getValue();
                return new None(valx / valy);
            }

            public void gradient() {
                None inx = input[0].getOutput(), iny = input[1].getOutput(), out = getOutput();
                Double valx = inx.getValue(), valy = iny.getValue();
                Double grad = out.getGrad();
                inx.setGrad(grad * valy / Math.pow(valy, 2));
                iny.setGrad(-grad * valx / Math.pow(valy, 2));
            }

        };
    }

    public Tenser exp(Node<None>... input) {
        return new Tenser<None>("Exp", input) {

            @Operation
            public None compute() {
                None inx = input[0].getOutput();
                Double valx = inx.getValue();
                return new None(Math.exp(valx));
            }

            public void gradient() {
                None inx = input[0].getOutput();
                Double valx = inx.getValue();
                inx.setGrad(Math.exp(valx));
            }

        };
    }

    public Tenser pow(Node<None>... input) {
        return new Tenser<None>("Pow", input) {

            @Operation
            public None compute() {
                None inx = input[0].getOutput(), iny = input[1].getOutput();
                Double valx = inx.getValue(), valy = iny.getValue();
                return new None(Math.pow(valx, valy));
            }

            public void gradient() {
                None inx = input[0].getOutput(), iny = input[1].getOutput();
                Double valx = inx.getValue(), valy = iny.getValue();
                inx.setGrad(valy * Math.pow(valx, valy - 1));
            }

        };
    }

    public Tenser matmul(Node<None[][]>... input) {
        return new Tenser<Node[][]>("Matmul", input) {

            public Node[][] compute() {
                None[][] A = input[0].getOutput(), B = input[1].getOutput();
                Node[][] C = zeros(new Node[A.length][B[0].length]);
                forEach(A.length, B[0].length, A[0].length, (i, l, j) -> {
                    C[i][l] = add(C[i][l], mul(new Tenser(A[i][j]), new Tenser(B[j][l])));
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

    public Tenser sigmoid(Node input) {
        return new Tenser<Node>("Sigmoid", input) {

            public Node compute() {
                return div(new Tenser(1d), add(new Tenser(1d), exp(minus(input))));
            }

            public void gradient() {

            }

        };
    }

    public Tenser sigmoidx(Node<None> input) {
        return new Tenser("Sigmoidx", input) {

            public Object compute() {
                Object A = input.getOutput(), B = shape(Tenser.class, A);
                forEach(A, B, (m, o, i) -> {
                    o[i] = sigmoid(new Tenser(m));
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
                Object A = input[0].getOutput(), B = input[1].getOutput(), C = shape(Tenser.class, A);
                forEach(A, B, C, (a, b, c, i) -> {
                    c[i] = square(new Tenser(a), new Tenser(b));
                });
                return C;
            }

            public void gradient() {}

        };
    }

    public Tenser sum(Node<None> input) {
        return new Tenser<Node>("Sum", input) {

            public Node compute() {
                Object A = input.getOutput();
                Node[] B = {new Tenser<None>(0d)};
                forEach(A,  a -> {
                    B[0] = add(new Tenser((None) a), B[0]);
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
