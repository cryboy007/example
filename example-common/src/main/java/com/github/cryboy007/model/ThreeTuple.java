package com.github.cryboy007.model;

public class ThreeTuple <A,B,C> extends  TowTuple<A, B> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -66644540774668428L;
	
	public ThreeTuple(A first,B second,C third){
		super(first, second);
		this.third = third;
	}
	private C third;

	public C getThird() {
		return third;
	}

	public void setThird(C third) {
		this.third = third;
	}
	
	
	
}
