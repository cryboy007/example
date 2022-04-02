package com.github.cryboy007.model;

import java.io.Serializable;

public class TowTuple<A, B> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4995237649864226508L;
	private A frist;
	private B second;

	public TowTuple(){}
	public TowTuple(A frist, B second) {
		this.frist = frist;
		this.second = second;
	}

	public A getFrist() {
		return frist;
	}

	public B getSecond() {
		return second;
	}

	public void setFrist(A frist) {
		this.frist = frist;
	}

	public void setSecond(B second) {
		this.second = second;
	}
	
	
}