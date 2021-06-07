package it.polito.tdp.PremierLeague.model;

import java.util.*; 

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model
{
	private PremierLeagueDAO dao;
	private Map<Integer, Team> idMap; 
	private Graph<Team, DefaultWeightedEdge> grafo; 
	
	public Model()
	{
		this.dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo()
	{
		// ripulisco mappa e grafo
		this.idMap = new HashMap<>(); 
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		/// vertici 
		this.dao.getVertici(idMap); //riempio la mappa
		Graphs.addAllVertices(this.grafo, this.idMap.values()); 
		
		/// archi
		List<Adiacenza> adiacenze = new ArrayList<>(this.dao.getAdiacenze(this.idMap));
		for (Adiacenza a : adiacenze)
		{ 
			if(a.getDiff() < 0)
				Graphs.addEdge(this.grafo, a.getT2(), a.getT1(), Math.abs(a.getDiff()));
			else
				Graphs.addEdge(this.grafo, a.getT1(), a.getT2(), Math.abs(a.getDiff()));
		}
	}
	public int getNumVertici()
	{
		return this.grafo.vertexSet().size();
	}
	public int getNumArchi()
	{
		return this.grafo.edgeSet().size();
	}
	public Collection<Team> getVertici()
	{
		ArrayList<Team> result = new ArrayList<>(this.grafo.vertexSet()); 
		result.sort((a1,a2)->a1.getName().compareTo(a2.getName()));
		return result;
	}
	public Collection<DefaultWeightedEdge> getArchi()
	{
		return this.grafo.edgeSet();
	}
	
	public Map<Team, Double> getBattuteDa(Team s)
	{
		ArrayList<DefaultWeightedEdge> edges = new ArrayList<>(this.grafo.outgoingEdgesOf(s)); 
		edges.sort((e1,e2)->Double.compare(this.grafo.getEdgeWeight(e1), this.grafo.getEdgeWeight(e2)));
		Map<Team, Double> vicini = new LinkedHashMap<>();
		for (DefaultWeightedEdge e : edges)
			vicini.put(Graphs.getOppositeVertex(this.grafo, e, s), this.grafo.getEdgeWeight(e));
		return vicini; 
	}
	public Map<Team, Double> getHannoBattuto(Team s)
	{
		ArrayList<DefaultWeightedEdge> edges = new ArrayList<>(this.grafo.incomingEdgesOf(s)); 
		edges.sort((e1,e2)->Double.compare(this.grafo.getEdgeWeight(e1), this.grafo.getEdgeWeight(e2)));
		Map<Team, Double> vicini = new LinkedHashMap<>();
		for (DefaultWeightedEdge e : edges)
			vicini.put(Graphs.getOppositeVertex(this.grafo, e, s), this.grafo.getEdgeWeight(e));
		return vicini; 
	}
}