package kr.hilu0318.domain;

public class ArrayDTO {
	
	private float[][] list;
	private int count;
	
	public void setList(float[][] list){ this.list = list; }
	public void setCount(int count){ this.count = count; }
	
	public float[][] getList(){ return this.list; }
	public int getCount(){ return this.count; }

}
