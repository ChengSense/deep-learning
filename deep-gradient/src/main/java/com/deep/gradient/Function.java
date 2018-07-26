package com.deep.gradient;

public class Function {

	public static Node sigmoid(double diff) {

		Node X = new Node(Option.DIV,

				new Node(Option.NONE, Type.CONS, "1"),

				new Node(Option.ADD,

						new Node(Option.NONE, Type.CONS, "1"),

						new Node(Option.EXP,

								new Node(Option.NONE, Type.CONS, "E"),

								new Node(Option.MINUS, Type.VAR, "x")

						)

				)

		);

		X.setGradient(diff);

		return X;
	}

	public static Node softmax(double diff) {

		Node X = new Node(Option.DIV,

				new Node(Option.EXP,

						new Node(Option.NONE, Type.CONS, "E"),

						new Node(Option.NONE, Type.VAR, "x")

				),

				new Node(Option.ADD,

						new Node(Option.NONE, Type.CONS, "a"),

						new Node(Option.EXP,

								new Node(Option.NONE, Type.CONS, "E"),

								new Node(Option.NONE, Type.VAR, "x")

						)

				)

		);

		X.setGradient(diff);

		return X;
	}

	public static Node mul(double diff) {

		Node Z = new Node(Option.MUL,

				new Node(Option.NONE, Type.VAR, "w"),

				new Node(Option.NONE, Type.VAR, "x")

		);

		Z.setGradient(diff);

		return Z;
	}

	public static Node add(double diff) {

		Node Z = new Node(Option.ADD,

				new Node(Option.NONE, Type.VAR, "x"),

				new Node(Option.NONE, Type.VAR, "b")

		);

		Z.setGradient(diff);

		return Z;
	}

	public static Node smallLoss() {

		Node C = new Node(Option.MUL,

				new Node(Option.NONE, Type.CONS, "0.5"),

				new Node(Option.POW,

						new Node(Option.MINUS,

								new Node(Option.NONE, Type.CONS, "l"),

								new Node(Option.NONE, Type.VAR, "x")

						),

						new Node(Option.NONE, Type.CONS, "2")

				)

		);

		return C;
	}

	public static Node corssLoss() {

		Node C = new Node(Option.ADD,

				new Node(Option.MUL,

						new Node(Option.NONE, Type.CONS, "y"),

						new Node(Option.LOG,

								new Node(Option.NONE, Type.CONS, "E"),

								new Node(Option.DIV,

										new Node(Option.EXP,

												new Node(Option.NONE, Type.CONS, "E"),

												new Node(Option.NONE, Type.VAR, "x")

										),

										new Node(Option.ADD,

												new Node(Option.NONE, Type.CONS, "a"),

												new Node(Option.EXP,

														new Node(Option.NONE, Type.CONS, "E"),

														new Node(Option.NONE, Type.VAR, "x")

												)

										)

								)

						)

				),

				new Node(Option.MUL,

						new Node(Option.MINUS,

								new Node(Option.NONE, Type.CONS, "1"),

								new Node(Option.NONE, Type.CONS, "y")

						),

						new Node(Option.LOG,

								new Node(Option.NONE, Type.CONS, "E"),

								new Node(Option.MINUS,

										new Node(Option.NONE, Type.CONS, "1"),

										new Node(Option.DIV,

												new Node(Option.EXP,

														new Node(Option.NONE, Type.CONS, "E"),

														new Node(Option.NONE, Type.VAR, "x")

												),

												new Node(Option.ADD,

														new Node(Option.NONE, Type.CONS, "a"),

														new Node(Option.EXP,

																new Node(Option.NONE, Type.CONS, "E"),

																new Node(Option.NONE, Type.VAR, "x")

														)

												)

										)

								)

						)

				)

		);

		return C;
	}

}
