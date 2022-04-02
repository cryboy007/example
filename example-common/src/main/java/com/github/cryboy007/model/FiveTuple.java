package com.github.cryboy007.model;

/**
 * 
 * 五元组数据结构
 * 
 * @author LeoChan
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 */
public class FiveTuple<A, B, C, D,E> extends FourTuple<A, B, C, D> {

	private static final long serialVersionUID = 5543552466584083747L;
	
	private E five;

	public FiveTuple(A first, B second, C third, D four,E five) {
		super(first, second, third,four);
		this.five = five;
	}

	public E getFive() {
		return five;
	}

	public void setFive(E five) {
		this.five = five;
	}

}
