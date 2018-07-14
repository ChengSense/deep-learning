package com.deep.util;

import java.util.ArrayList;

public class Array<E> extends ArrayList<E> {

	public E begin() {
		return this.get(0);
	}

	public E end() {
		return this.get(this.size() - 1);
	}

}