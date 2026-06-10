package ucu.edu.aed.tda.grafo.model.implementacion;

import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.IUndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

import java.util.*;
import java.util.function.Consumer;

public class AlgoritmosGrafoNoDirigido implements IUndirectedGraphAlgorithm {

    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> kruskal(
            IUndirectedGraph<V, D> graph) {

        if (graph == null) {
            return null;
        }

        List<Edge<V, D>> aristasDisponibles =
                new ArrayList<>(graph.aristas());

        IUndirectedGraph<V, D> resultado =
                new GrafoNoDirigido<V, D>();

        for (V vertice : graph.vertices()) {
            resultado.agregarVertice(vertice);
        }

        int cantidadAristas = 0;

        int necesarias = graph.cantidadDeVertices() - 1;

        while (cantidadAristas < necesarias
                && !aristasDisponibles.isEmpty()) {
            Edge<V, D> menor = null;

            for (Edge<V, D> arista : aristasDisponibles) {

                if (menor == null
                        || arista.dato().getWeight()
                        < menor.dato().getWeight()) {

                    menor = arista;
                }
            }

            aristasDisponibles.remove(menor);

            resultado.agregarArista(
                    menor.source(),
                    menor.target(),
                    menor.dato()
            );

            if (resultado.tieneCiclos()) {

                resultado.eliminarArista(
                        resultado.construirComparable(menor.source()),
                        resultado.construirComparable(menor.target())
                );
            } else {

                cantidadAristas++;
            }
        }
        return resultado;
    }
    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> prim(IUndirectedGraph<V, D> graph, Comparable<V> source) {
        return null;
    }

    @Override
    public <V, D extends WeightedEdge> Edge<V, D> searchMinEdge(IUndirectedGraph<V, D> graph, Collection<V> U, Collection<V> V) {
        return null;
    }

    @Override
    public <V, D> void bea(IUndirectedGraph<V, D> graph, Consumer<V> consumer) {
        HashSet<V> visitados = new HashSet<>();
        Queue<V> colaVertices = new ArrayDeque<>();

        for (V vertice : graph.vertices()) {
            if (!visitados.contains(vertice)) {
                colaVertices.add(vertice);
                visitados.add(vertice);

                while (!colaVertices.isEmpty()) {
                    V actual = colaVertices.poll();
                    consumer.accept(actual);

                    List<Edge<V, D>> adyacentes = graph.adyacencias(graph.construirComparable(actual));

                    for (Edge<V,D> arista : adyacentes){
                        V verticeAdy = arista.target();

                        if (!visitados.contains(verticeAdy)) {
                            colaVertices.add(verticeAdy);
                            visitados.add(verticeAdy);
                        }
                    }
                }
            }
        }

    }
}
