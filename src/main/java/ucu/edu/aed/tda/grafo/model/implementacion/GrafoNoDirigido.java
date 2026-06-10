package ucu.edu.aed.tda.grafo.model.implementacion;

import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.UndirectedEdge;

import java.util.*;

public class GrafoNoDirigido<V, D> implements IUndirectedGraph<V, D> {

    private final Set<V> vertices;
    private final Set<Edge<V, D>> aristas;

    public GrafoNoDirigido() {
        this.vertices = new HashSet<>();
        this.aristas = new HashSet<>();
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

        Edge<V, D> arista =
                new UndirectedEdge<>(verticeOrigen, verticeDestino, dato);

        return aristas.add(arista);
    }

    @Override
    public boolean eliminarArista(
            Comparable<V> source,
            Comparable<V> target) {

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
                arista.source().equals(vertice)
                        || arista.target().equals(vertice)
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
    public boolean existeArista(
            Comparable<V> sourceCriteria,
            Comparable<V> targetCriteria) {

        return obtenerArista(sourceCriteria, targetCriteria) != null;
    }

    @Override
    public Edge<V, D> obtenerArista(
            Comparable<V> sourceCriteria,
            Comparable<V> targetCriteria) {

        V source = buscarVertice(sourceCriteria);
        V target = buscarVertice(targetCriteria);

        if (source == null || target == null) {
            return null;
        }

        for (Edge<V, D> arista : aristas) {

            boolean directa =
                    arista.source().equals(source)
                            && arista.target().equals(target);

            boolean inversa =
                    arista.source().equals(target)
                            && arista.target().equals(source);

            if (directa || inversa) {
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

            } else if (arista.target().equals(vertice)) {

                adyacentes.add(
                        new UndirectedEdge<>(
                                vertice,
                                arista.source(),
                                arista.dato()
                        )
                );
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

    private void recorrerDesde(
            V actual,
            Set<V> visitados) {

        visitados.add(actual);

        for (Edge<V, D> arista :
                adyacencias(construirComparable(actual))) {

            V vecino = arista.target();

            if (!visitados.contains(vecino)) {
                recorrerDesde(vecino, visitados);
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

        for (V vertice : vertices) {

            if (!visitados.contains(vertice)) {

                if (tieneCiclosDesde(
                        vertice,
                        null,
                        visitados)) {

                    return true;
                }
            }
        }

        return false;
    }

    private boolean tieneCiclosDesde(
            V actual,
            V padre,
            Set<V> visitados) {

        visitados.add(actual);

        for (Edge<V, D> arista :
                adyacencias(construirComparable(actual))) {

            V vecino = arista.target();

            if (!visitados.contains(vecino)) {

                if (tieneCiclosDesde(
                        vecino,
                        actual,
                        visitados)) {

                    return true;
                }

            } else if (!vecino.equals(padre)) {

                return true;
            }
        }

        return false;
    }
}