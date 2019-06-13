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

import com.zaxxer.hikari.metrics.dropwizard.CodahaleHealthChecker;

import it.polito.tdp.food.db.Condiment;
import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	FoodDao dao;
	Graph<Condiment, DefaultWeightedEdge> grafo;
	List<Condiment> ingredienti;
	Map<Integer, Condiment> idMap;
	
	//Ricorsione
	List<Condiment> best;
	
	
	public Model() {
		dao = new FoodDao();
		best = new ArrayList<Condiment>();
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
	
	/**
	 * Calocolo il numero di cibi in cui compare l'ingrediente
	 * @param c
	 * @return
	 */
	public int getGradoNodo(Condiment c) {		
		
		int risultato = 0;
		
		for (Condiment ccc : Graphs.neighborListOf(grafo, c)) {		
			risultato = (int)(risultato + (grafo.getEdgeWeight(grafo.getEdge(ccc, c))));			
		}
		
		return risultato;
	}
	
	
	public List<Condiment> ricorsione(Condiment iniziale){
		
		//Inizializzo liste per ricorsioni successive
		List<Condiment> parziale = new ArrayList<Condiment>();
		best = new ArrayList<>();
		
		//Aggiungo l'ingrediente iniziale
		best.add(iniziale);
		parziale.add(iniziale);
		
		//Avvio sotto ricorsione
		sub_ricorsione(parziale);
		
		//Risultato
		return this.best;
	}
	
	public void sub_ricorsione(List<Condiment> parziale) {
		
		//CONDIZIONE FINALE
		if ( getCalorie(parziale) > getCalorie(this.best)) {
			this.best = new ArrayList<Condiment>(parziale);
		}
		
		//CASO INTERMEDIO
		for (Condiment c1 : this.ingredienti) {	
			
			//Clono per non ciclare sulla lista stessa
			List<Condiment> parziale2 = new ArrayList<>(parziale);
			
			for (Condiment c2 : parziale2) {
				
				//Oggetti diversi --- //Non deve essere contenuto nella lista dei vicini --- //Il parziale non deve già contenere il condimento
 				if (!c1.equals(c2) && !Graphs.neighborListOf(this.grafo, c2).contains(c1) && !parziale.contains(c1)) {
 					
					//Aggiungo
 					parziale.add(c1);
 					//Ricorsione
					sub_ricorsione(parziale);
					//Backtracking
					parziale.remove(parziale.size()-1);
				}
				
 				//Altrimenti esco
				else return;
			}
			
		}
	}
	
	/**
	 * Metodo per calcolare il totale delle calorie della lista passata
	 * @param listaC
	 * @return TOT calorie della lista
	 */
	public double getCalorie(List<Condiment> listaC) {
		
		double res=0.0;
		
		//Primo ciclo
		if (listaC.size()>0) {
		
		//Calcolo totale delle calorie 
		for(Condiment c : listaC) res = res + c.getCondiment_calories();	
		}	
		
		return res;
	}
	
}
