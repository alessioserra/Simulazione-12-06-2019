package it.polito.tdp.food.model;

public class Accoppiamenti {
	
	private int c1;
	private int c2;
	
	private double peso;

	public Accoppiamenti(int c1, int c2, double peso) {
		this.c1 = c1;
		this.c2 = c2;
		this.peso = peso;
	}

	public int getC1() {
		return c1;
	}

	public int getC2() {
		return c2;
	}

	public double getPeso() {
		return peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + c1;
		result = prime * result + c2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Accoppiamenti other = (Accoppiamenti) obj;
		if (c1 != other.c1)
			return false;
		if (c2 != other.c2)
			return false;
		return true;
	}	

}
