package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model
{
	private PremierLeagueDAO dao;
	private Map<Integer, Team> teams; 
	private Graph<Team, DefaultWeightedEdge> grafo; 
	
	public Model()
	{
		this.dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo()
	{
		this.teams = new HashMap<>(); 
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici 
		this.dao.getTeams(teams);
		Graphs.addAllVertices(this.grafo, this.teams.values()); 
	}
}