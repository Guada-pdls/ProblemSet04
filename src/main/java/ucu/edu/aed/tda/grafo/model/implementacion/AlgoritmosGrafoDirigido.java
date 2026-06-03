package ucu.edu.aed.tda.grafo.model.implementacion;

import ucu.edu.aed.tda.grafo.IDirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.IGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;
import ucu.edu.aed.tda.grafo.model.result.Path;

import java.util.*;
import java.util.function.Consumer;

public class AlgoritmosGrafoDirigido implements IDirectedGraphAlgorithms {

    @Override
    public <V, D extends WeightedEdge> IDijkstraResult<V> dijkstra(Comparable<V> source, IDirectedIGraph<V, D> grafo) {
        V origen = grafo.buscarVertice(source);

        Map<V, Double> costos = new HashMap<>();
        Map<V, V> anteriores = new HashMap<>();
        Set<V> visitados = new HashSet<>();

        for (V vertice : grafo.vertices()) {
            costos.put(vertice, Double.POSITIVE_INFINITY);
        }

        if (origen != null) {
            costos.put(origen, 0.0);
        }

        while (visitados.size() < grafo.vertices().size()) {
            V actual = null;
            double menorCosto = Double.POSITIVE_INFINITY;

            for (V vertice : grafo.vertices()) {
                if (!visitados.contains(vertice) && costos.get(vertice) < menorCosto) {
                    menorCosto = costos.get(vertice);
                    actual = vertice;
                }
            }

            if (actual == null) {
                break;
            }

            visitados.add(actual);

            for (Edge<V, D> arista : grafo.adyacencias(grafo.construirComparable(actual))) {
                V destino = arista.target();
                double nuevoCosto = costos.get(actual) + arista.dato().getWeight();

                if (nuevoCosto < costos.get(destino)) {
                    costos.put(destino, nuevoCosto);
                    anteriores.put(destino, actual);
                }
            }
        }

        return new IDijkstraResult<V>() {
            @Override
            public double getCost(V otherVertex) {
                return costos.getOrDefault(otherVertex, Double.POSITIVE_INFINITY);
            }

            @Override
            public List<V> getPath(V otherVertex) {
                if (origen == null || getCost(otherVertex) == Double.POSITIVE_INFINITY) {
                    return List.of();
                }

                LinkedList<V> camino = new LinkedList<>();
                V actual = otherVertex;

                while (actual != null) {
                    camino.addFirst(actual);

                    if (actual.equals(origen)) {
                        return camino;
                    }

                    actual = anteriores.get(actual);
                }

                return List.of();
            }
        };
    }

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> floyd(IDirectedIGraph<V, D> grafo) {
        List<V> vertices = new ArrayList<>(grafo.vertices());
        int n = vertices.size();

        double[][] costos = new double[n][n];
        int[][] siguientes = new int[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(costos[i], Double.POSITIVE_INFINITY);
            Arrays.fill(siguientes[i], -1);

            costos[i][i] = 0;
            siguientes[i][i] = i;
        }

        for (Edge<V, D> arista : grafo.aristas()) {
            int origen = vertices.indexOf(arista.source());
            int destino = vertices.indexOf(arista.target());

            costos[origen][destino] = arista.dato().getWeight();
            siguientes[origen][destino] = destino;
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double nuevoCosto = costos[i][k] + costos[k][j];

                    if (nuevoCosto < costos[i][j]) {
                        costos[i][j] = nuevoCosto;
                        siguientes[i][j] = siguientes[i][k];
                    }
                }
            }
        }

        return new IFloydWarshallResult<V>() {
            @Override
            public List<V> getPath(V source, V target) {
                int origen = vertices.indexOf(source);
                int destino = vertices.indexOf(target);

                if (origen == -1 || destino == -1 || siguientes[origen][destino] == -1) {
                    return List.of();
                }

                List<V> camino = new ArrayList<>();
                int actual = origen;

                while (actual != destino) {
                    camino.add(vertices.get(actual));
                    actual = siguientes[actual][destino];
                }

                camino.add(vertices.get(destino));
                return camino;
            }

            @Override
            public double getCost(V source, V target) {
                int origen = vertices.indexOf(source);
                int destino = vertices.indexOf(target);

                if (origen == -1 || destino == -1) {
                    return Double.POSITIVE_INFINITY;
                }

                return costos[origen][destino];
            }

            @Override
            public boolean connected(V source, V target) {
                return getCost(source, target) != Double.POSITIVE_INFINITY;
            }
        };
    }

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> warshall(IDirectedIGraph<V, D> grafo) {

        List<V> vertices = new ArrayList<>(grafo.vertices());
        int n = vertices.size();

        boolean[][] reach = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            reach[i][i] = true;
        }

        for (Edge<V, D> e : grafo.aristas()) {
            int i = vertices.indexOf(e.source());
            int j = vertices.indexOf(e.target());
            reach[i][j] = true;
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    reach[i][j] = reach[i][j] || (reach[i][k] && reach[k][j]);
                }
            }
        }

        return new IFloydWarshallResult<V>() {

            @Override
            public List<V> getPath(V source, V target) {
                return List.of();
            }

            @Override
            public double getCost(V source, V target) {
                int i = vertices.indexOf(source);
                int j = vertices.indexOf(target);

                if (i == -1 || j == -1) {
                    return Double.POSITIVE_INFINITY;
                }

                return reach[i][j] ? 1 : Double.POSITIVE_INFINITY;
            }

            @Override
            public boolean connected(V source, V target) {
                int i = vertices.indexOf(source);
                int j = vertices.indexOf(target);

                if (i == -1 || j == -1) {
                    return false;
                }

                return reach[i][j];
            }
        };
    }

    @Override
    public <V, D extends WeightedEdge> V obtenerCentroGrafo(IDirectedIGraph<V, D> grafo) {
        IFloydWarshallResult<V> resultado = floyd(grafo);
        V centro = null;

        double menorExcentricidad = Double.POSITIVE_INFINITY;

        for (V vertice : grafo.vertices()) {
            double excentricidad = 0;
            for (V otro : grafo.vertices()) {
                double dist = resultado.getCost(vertice, otro);
                if (dist != Double.POSITIVE_INFINITY && dist > excentricidad) {
                    excentricidad = dist;
                }
            }
            if (excentricidad < menorExcentricidad) {
                menorExcentricidad = excentricidad;
                centro = vertice;
            }
        }

        return centro;
    }

    @Override
    public <V, D extends WeightedEdge> double obtenerExcentricidad(IDirectedIGraph<V, D> grafo, Comparable<V> vertexCriteria) {
        IFloydWarshallResult<V> resultado = floyd(grafo);
        double excentricidad = 0;
        V verticeOrigen = grafo.buscarVertice(vertexCriteria);

        for (V vertice : grafo.vertices()) {
            double distancia = resultado.getCost(verticeOrigen, vertice);
            if (distancia != Double.POSITIVE_INFINITY && distancia > excentricidad) {
                excentricidad = distancia;
            }
        }

        return excentricidad;
    }

    @Override
    public <V, D extends WeightedEdge> List<Path<V>> obtenerTodosLosCaminos(
            Comparable<V> source,
            Comparable<V> target,
            IGraph<V, D> grafo) {

        List<Path<V>> resultado = new ArrayList<>();

        V origen = grafo.buscarVertice(source);
        V destino = grafo.buscarVertice(target);

        if (origen == null || destino == null) {
            return resultado;
        }

        Set<V> visitados = new HashSet<>();
        List<V> caminoActual = new ArrayList<>();

        dfsCaminos(
                origen,
                destino,
                grafo,
                visitados,
                caminoActual,
                0,
                resultado
        );

        return resultado;
    }

    private <V, D extends WeightedEdge> void dfsCaminos(
            V actual,
            V destino,
            IGraph<V, D> grafo,
            Set<V> visitados,
            List<V> caminoActual,
            double costoActual,
            List<Path<V>> resultado) {

        visitados.add(actual);
        caminoActual.add(actual);

        if (actual.equals(destino)) {

            resultado.add(
                    new Path<>(
                            new ArrayList<>(caminoActual),
                            costoActual
                    )
            );

        } else {

            for (Edge<V, D> arista :
                    grafo.adyacencias(grafo.construirComparable(actual))) {

                V siguiente = arista.target();

                if (!visitados.contains(siguiente)) {

                    dfsCaminos(
                            siguiente,
                            destino,
                            grafo,
                            visitados,
                            caminoActual,
                            costoActual + arista.dato().getWeight(),
                            resultado
                    );
                }
            }
        }

        caminoActual.remove(caminoActual.size() - 1);
        visitados.remove(actual);
    }

    @Override
    public <V, D> void recorridoEnProfundidad(
            IGraph<V, D> grafo,
            Comparable<V> sourceCriteria,
            Consumer<V> consumer) {

        V inicio = grafo.buscarVertice(sourceCriteria);

        if (inicio == null) {
            return;
        }

        Set<V> visitados = new HashSet<>();

        dfs((IDirectedIGraph<V, D>) grafo, inicio, visitados, consumer);
    }

    private <V, D> void dfs(
            IDirectedIGraph<V, D> grafo,
            V actual,
            Set<V> visitados,
            Consumer<V> consumer) {

        if (visitados.contains(actual)) {
            return;
        }

        visitados.add(actual);
        consumer.accept(actual);

        for (V v : grafo.successors(grafo.construirComparable(actual))) {
            dfs(grafo, v, visitados, consumer);
        }
    }

    @Override
    public <V, D> void recorridoEnAmplitud(IGraph<V, D> grafo, Comparable<V> sourceCriteria, Consumer<V> consumer) {
        Set<V> visitados = new HashSet<>();
        Queue<V> cola = new LinkedList<>();

        V nodoInicial = grafo.buscarVertice(sourceCriteria);
        if (nodoInicial == null) return;

        cola.add(nodoInicial);
        visitados.add(nodoInicial);

        while (!cola.isEmpty()) {
            V actual = cola.poll();
            consumer.accept(actual);

            for (V vecino : ((IDirectedIGraph<V,D>) grafo).successors(grafo.construirComparable(actual))) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }
    }

    @Override
    public <V, D> List<V> calcularClasificacionTopologica(IDirectedIGraph<V, D> grafo) {

        if (grafo.tieneCiclos()){
            throw new IllegalStateException(
                    "No se puede realizar clasificación topológica sobre un grafo con ciclos"
            );
        }

        Stack<V> pila = new Stack<>();
        Set<V> visitados = new HashSet<>();

        for (V vertice : grafo.vertices()) {
            if (!visitados.contains(vertice)) {
                dfsTopologico(vertice, grafo, visitados, pila);
            }
        }

        List<V> resultado = new ArrayList<>();

        while (!pila.isEmpty()) {
            resultado.add(pila.pop());
        }

        return resultado;
    }

    private <V, D> void dfsTopologico(
            V actual,
            IDirectedIGraph<V, D> grafo,
            Set<V> visitados,
            Stack<V> pila) {

        visitados.add(actual);

        for (V sucesor : grafo.successors(grafo.construirComparable(actual))) {

            if (!visitados.contains(sucesor)) {

                dfsTopologico(
                        sucesor,
                        grafo,
                        visitados,
                        pila
                );
            }
        }

        pila.push(actual);
    }

    @Override
    public <V, D> void recorridoEnProfundidad(IGraph<V, D> grafo, Consumer<V> consumer) {
        IDirectedGraphAlgorithms.super.recorridoEnProfundidad(grafo, consumer);
    }

    @Override
    public <V, D> void recorridoEnAmplitud(IGraph<V, D> grafo, Consumer<V> consumer) {
        IDirectedGraphAlgorithms.super.recorridoEnAmplitud(grafo, consumer);
    }
}
