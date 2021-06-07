package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.List;

public class Prova
{
	public static void main(String[] args)
	{
		List<Integer> lista = new ArrayList<>(); 
		lista.add(1);
		lista.add(2);
		lista.add(3);
		System.out.println("size : " + lista.size());
		lista.remove(0); 
		System.out.println(lista);
		System.out.println("size : " + lista.size());
	}
}
