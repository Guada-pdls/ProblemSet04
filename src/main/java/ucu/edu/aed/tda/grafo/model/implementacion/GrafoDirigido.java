package ucu.edu.aed.tda.grafo.model.implementacion;

import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GrafoDirigido<V, D> implements IDirectedIGraph<V, D> {

    private final Set<V> vertices;
    private final Set<Edge<V, D>> aristas;

    public GrafoDirigido() {
        this.vertices = new HashSet<>();
        this.aristas = new HashSet<>();
    }

    @Override
    public Set<V> successors(Comparable<V> criteria) {
        V vertice = buscarVertice(criteria);

        if (vertice == null) {
            return Set.of();
        }

        Set<V> sucesores = new HashSet<>();

        for (Edge<V, D> arista : aristas) {
            if (arista.source().equals(vertice)) {
                sucesores.add(arista.target());
            }
        }

        return Collections.unmodifiableSet(sucesores);
    }

    @Override
    public Set<V> predecessors(Comparable<V> criteria) {
        V vertice = buscarVertice(criteria);

        if (vertice == null) {
            return Set.of();
        }

        Set<V> predecesores = new HashSet<>();

        for (Edge<V, D> arista : aristas) {
            if (arista.target().equals(vertice)) {
                predecesores.add(arista.source());
            }
        }

        return Collections.unmodifiableSet(predecesores);
    }

    @Override
    public boolean agregarVertice(V vertex) {
        if (vertex == null) {
            return false;
        }

        return vertices.add(vertex);
    }

    @Override
    public V buscarVertice(Comparable<V> criterio) {
        if (criterio == null) {
            return null;
        }

        for (V vertice : vertices) {
            if (criterio.compareTo(vertice) == 0) {
                return vertice;
            }
        }

        return null;
    }

    @Override
    public boolean agregarArista(V source, V target, D dato) {
        if (source == null || target == null) {
            return false;
        }

        V verticeOrigen = buscarVertice(construirComparable(source));
        V verticeDestino = buscarVertice(construirComparable(target));

        if (verticeOrigen == null || verticeDestino == null) {
            return false;
        }

        Edge<V, D> arista = new DirectedEdge<>(verticeOrigen, verticeDestino, dato);
        return aristas.add(arista);
    }

    @Override
    public boolean eliminarArista(Comparable<V> source, Comparable<V> target) {
        Edge<V, D> arista = obtenerArista(source, target);

        if (arista == null) {
            return false;
        }

        return aristas.remove(arista);
    }

    @Override
    public boolean removerVertice(Comparable<V> criteria) {
        V vertice = buscarVertice(criteria);

        if (vertice == null) {
            return false;
        }

        vertices.remove(vertice);
        aristas.removeIf(arista ->
                arista.source().equals(vertice) || arista.target().equals(vertice)
        );

        return true;
    }

    @Override
    public Set<V> vertices() {
        return Collections.unmodifiableSet(vertices);
    }

    @Override
    public Set<Edge<V, D>> aristas() {
        return Collections.unmodifiableSet(aristas);
    }

    @Override
    public boolean existeArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        return obtenerArista(sourceCriteria, targetCriteria) != null;
    }

    @Override
    public Edge<V, D> obtenerArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        V source = buscarVertice(sourceCriteria);
        V target = buscarVertice(targetCriteria);

        if (source == null || target == null) {
            return null;
        }

        for (Edge<V, D> arista : aristas) {
            if (arista.source().equals(source) && arista.target().equals(target)) {
                return arista;
            }
        }

        return null;
    }

    @Override
    public List<Edge<V, D>> adyacencias(Comparable<V> verticeCriteria) {
        V vertice = buscarVertice(verticeCriteria);

        if (vertice == null) {
            return List.of();
        }

        List<Edge<V, D>> adyacentes = new ArrayList<>();

        for (Edge<V, D> arista : aristas) {
            if (arista.source().equals(vertice)) {
                adyacentes.add(arista);
            }
        }

        return Collections.unmodifiableList(adyacentes);
    }

    @Override
    public boolean esConexo() {
        if (vertices.isEmpty()) {
            return true;
        }

        Set<V> visitados = new HashSet<>();
        V inicial = vertices.iterator().next();

        recorrerDesde(inicial, visitados);

        return visitados.size() == vertices.size();
    }

    private void recorrerDesde(V actual, Set<V> visitados) {
        visitados.add(actual);

        for (V sucesor : successors(construirComparable(actual))) {
            if (!visitados.contains(sucesor)) {
                recorrerDesde(sucesor, visitados);
            }
        }
    }

    @Override
    public void vaciar() {
        aristas.clear();
        vertices.clear();
    }

    @Override
    public boolean tieneCiclos() {
        Set<V> visitados = new HashSet<>();
        Set<V> enProceso = new HashSet<>();

        for (V vertice : vertices) {
            if (!visitados.contains(vertice) && tieneCiclosDesde(vertice, visitados, enProceso)) {
                return true;
            }
        }

        return false;
    }

    private boolean tieneCiclosDesde(V actual, Set<V> visitados, Set<V> enProceso) {
        visitados.add(actual);
        enProceso.add(actual);

        for (V sucesor : successors(construirComparable(actual))) {
            if (!visitados.contains(sucesor)) {
                if (tieneCiclosDesde(sucesor, visitados, enProceso)) {
                    return true;
                }
            } else if (enProceso.contains(sucesor)) {
                return true;
            }
        }

        enProceso.remove(actual);
        return false;
    }
}