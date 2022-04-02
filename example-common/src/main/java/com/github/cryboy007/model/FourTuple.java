package com.github.cryboy007.model;

/**
 * 
 * 四元组数据结构
 * @author LeoChan 
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 */
public class FourTuple <A,B,C,D> extends ThreeTuple<A, B, C> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -66644540774668428L;
	
	private D four;
	
	public FourTuple(A first,B second,C third,D four){
		super(first, second,third);
		this.four = four;
	}

	public D getFour() {
		return four;
	}

	public void setFour(D four) {
		this.four = four;
	}
	
	
}
