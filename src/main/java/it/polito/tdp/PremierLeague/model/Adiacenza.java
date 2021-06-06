package it.polito.tdp.PremierLeague.model;

public class Adiacenza
{
	private Team t1; 
	private Team t2; 
	private Integer diff;
	
	public Adiacenza(Team t1, Team t2, Integer diff)
	{
		this.t1 = t1;
		this.t2 = t2;
		this.diff = diff;
	}
	public Team getT1()
	{
		return t1;
	}
	public void setT1(Team t1)
	{
		this.t1 = t1;
	}
	public Team getT2()
	{
		return t2;
	}
	public void setT2(Team t2)
	{
		this.t2 = t2;
	}
	public Integer getDiff()
	{
		return diff;
	}
	public void setDiff(Integer diff)
	{
		this.diff = diff;
	}
	@Override public String toString()
	{
		return "Adiacenza [t1=" + t1 + ", t2=" + t2 + ", diff=" + diff + "]";
	}
	@Override public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((t1 == null) ? 0 : t1.hashCode());
		result = prime * result + ((t2 == null) ? 0 : t2.hashCode());
		return result;
	}
	@Override public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Adiacenza other = (Adiacenza) obj;
		if (t1 == null)
		{
			if (other.t1 != null) return false;
		}
		else if (!t1.equals(other.t1)) return false;
		if (t2 == null)
		{
			if (other.t2 != null) return false;
		}
		else if (!t2.equals(other.t2)) return false;
		return true;
	} 
}
