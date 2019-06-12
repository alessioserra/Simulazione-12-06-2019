package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.Condiment;
import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	FoodDao dao;
	Graph<Condiment, DefaultWeightedEdge> grafo;
	List<Condiment> ingredienti;
	Map<Integer, Condiment> idMap;
	
	
	public Model() {
		dao = new FoodDao();
	}
	public List<Condiment> listAllCondiment(double calorie){
		
		idMap = new HashMap<Integer, Condiment>();
		ingredienti = new ArrayList<Condiment>(dao.listAllCondiment(calorie,this.idMap));
		Collections.sort(ingredienti);
		return ingredienti;
	}
	
	public void creaGrafo() {
		
		grafo = new SimpleWeightedGraph<Condiment, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//Aggiungo Nodi
		Graphs.addAllVertices(grafo, ingredienti);
		
		//Aggiungo gli archi 
		List<Accoppiamenti> listaArchi = dao.getArchi();
		
		for (Accoppiamenti acc : listaArchi) {
			
			if (this.idMap.containsKey(acc.getC1()) && this.idMap.containsKey(acc.getC2())) {
			
				Condiment source = idMap.get(acc.getC1());
				Condiment target = idMap.get(acc.getC2());
				double peso = acc.getPeso();		
				
				
				Graphs.addEdge(grafo, source, target, peso);			
			
			}
		}
			
		System.out.println("GRAFO CREATO");
		System.out.println("#NODI: "+grafo.vertexSet().size());
		System.out.println("#ARCHI: "+grafo.edgeSet().size());
	}
	
	public int getGradoNodo(Condiment c) {		
		
		int risultato = 0;
		
		for (Condiment ccc : Graphs.neighborListOf(grafo, c)) {		
			risultato = (int)(risultato + (grafo.getEdgeWeight(grafo.getEdge(ccc, c))));			
		}
		
		return risultato;
	}
	
}
