package ucu.edu.aed.tda.grafo.model.implementacion;

import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

import java.util.List;
import java.util.Set;

public class GrafoDirigido implements IDirectedIGraph {
    @Override
    public Set successors(Comparable criteria) {
        return Set.of();
    }

    @Override
    public Set predecessors(Comparable criteria) {
        return Set.of();
    }

    @Override
    public boolean agregarVertice(Object vertex) {
        return false;
    }

    @Override
    public Object buscarVertice(Comparable criterio) {
        return null;
    }

    @Override
    public boolean agregarArista(Object source, Object target, Object dato) {
        return false;
    }

    @Override
    public boolean eliminarArista(Comparable source, Comparable target) {
        return false;
    }

    @Override
    public boolean removerVertice(Comparable criteria) {
        return false;
    }

    @Override
    public Set vertices() {
        return Set.of();
    }

    @Override
    public Set<Edge> aristas() {
        return Set.of();
    }

    @Override
    public boolean existeArista(Comparable sourceCriteria, Comparable targetCriteria) {
        return false;
    }

    @Override
    public Edge obtenerArista(Comparable sourceCriteria, Comparable targetCriteria) {
        return null;
    }

    @Override
    public List<Edge> adyacencias(Comparable verticeCriteria) {
        return List.of();
    }

    @Override
    public boolean esConexo() {
        return false;
    }

    @Override
    public void vaciar() {

    }

    @Override
    public boolean tieneCiclos() {
        return false;
    }
}
