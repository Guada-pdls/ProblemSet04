package ucu.edu.aed.tda.grafo.model.implementacion;

import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

import java.util.List;
import java.util.Set;

public class GrafoNoDirigido <V, D> implements IUndirectedGraph<V, D> {
    @Override
    public boolean agregarVertice(V vertex) {
        return false;
    }

    @Override
    public V buscarVertice(Comparable<V> criterio) {
        return null;
    }

    @Override
    public boolean agregarArista(V source, V target, D dato) {
        return false;
    }

    @Override
    public boolean eliminarArista(Comparable<V> source, Comparable<V> target) {
        return false;
    }

    @Override
    public boolean removerVertice(Comparable<V> criteria) {
        return false;
    }

    @Override
    public Set<V> vertices() {
        return Set.of();
    }

    @Override
    public Set<Edge<V, D>> aristas() {
        return Set.of();
    }

    @Override
    public boolean existeArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        return false;
    }

    @Override
    public Edge<V, D> obtenerArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        return null;
    }

    @Override
    public List<Edge<V, D>> adyacencias(Comparable<V> verticeCriteria) {
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
