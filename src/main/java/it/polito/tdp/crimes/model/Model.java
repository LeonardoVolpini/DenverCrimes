package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private SimpleWeightedGraph<String,DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<String> bestPercorso;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public List<String> getCategories(){
		return dao.getCategories();
	}
	
	public void CreaGrafo(String categoria, int mese) {
		grafo = new SimpleWeightedGraph<String,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		Graphs.addAllVertices(grafo, dao.getVertici(categoria, mese));
		
		//aggiungo archi
		for (Adiacenza a : dao.getAdiacenze(categoria, mese)) {
			if (this.grafo.getEdge(a.getV1(), a.getV2())==null)
				Graphs.addEdge(grafo, a.getV1(), a.getV2(), a.getPeso());
		}
		
		System.out.println("Grafo Creato");
		System.out.println("# Vertici: "+grafo.vertexSet().size());
		System.out.println("# Archi: "+grafo.edgeSet().size());
	}
	
	public List<Adiacenza> getArchiSopraPesoMedio(){
		List<Adiacenza> result= new LinkedList<>();
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			if (grafo.getEdgeWeight(e)>this.pesoMedioGrafo())
				result.add(new Adiacenza(grafo.getEdgeSource(e),grafo.getEdgeTarget(e),grafo.getEdgeWeight(e)));
		}
		return result;
	}
	
	public double pesoMedioGrafo() {
		double pesoMedio=0.0;
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			pesoMedio += this.grafo.getEdgeWeight(e);
		}
		pesoMedio= pesoMedio/grafo.edgeSet().size();
		return pesoMedio;
	}
	
	public List<String> trovaPercorso(String sorgente, String destinazione) {
		this.bestPercorso= new ArrayList<>();
		List<String> parziale= new ArrayList<>();
		parziale.add(sorgente);
		cerca(parziale,destinazione);
		return this.bestPercorso;
		
	}
	
	private void cerca(List<String> parziale, String destinazione) {
		//casi terminali
		if (parziale.get(parziale.size()-1).equals(destinazione)) {
			if (parziale.size()>this.bestPercorso.size()) { //guardo se e' effetivamente il piu' lungo
				this.bestPercorso= new ArrayList<String>(parziale);
			}
			return;
		}
		//altrimenti scorro i vicini dell'ultimo inserito e provo ad aggiungerli un oad uno
		for (String vicino: Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			if (!parziale.contains(vicino)) {
				parziale.add(vicino);
				this.cerca(parziale, destinazione);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	
	
}
