package com.krawczyk.maciej.travellingsalesmanproblem.data;

import java.util.ArrayList;

/**
 * Created by maciek on 30.05.17.
 */
public class Graph {

    private int edgesCount;
    private int verticesCount;
//    private List<Integer>[] adjacency;

    private ArrayList<ArrayList<MapPoint>> adjacency;

    @SuppressWarnings("unchecked")
    public Graph(int vertices) {
        this.verticesCount = vertices;
        this.edgesCount = 0;

        adjacency = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjacency.add(new ArrayList<>());
        }
    }

    public void addEdge(MapPoint vertexStart, MapPoint vertexEnd) {
        adjacency.get(vertexStart.getIndex()).add(vertexEnd);
        adjacency.get(vertexEnd.getIndex()).add(vertexStart);
        edgesCount++;
    }

    public int getEdgesCount() {
        return edgesCount;
    }

    public int getVerticesCount() {
        return verticesCount;
    }

    public ArrayList<MapPoint> getAdjacencyOf(MapPoint vertex) {
        return adjacency.get(vertex.getIndex());
    }

//    /**
//     * @return opis grafu.
//     */
//    public String toString() {
//        StringBuilder s = new StringBuilder();
//        String newLine = System.getProperty("line.separator");
//        s.append("wierzcholki: ").append(verticesCount).append("; krawedzie: ").append(edgesCount).append(newLine);
//
//        for (int i = 0; i < verticesCount; i++) {
//            s.append(adjacency.get(i)).append(": ");
//
//            for (MapPoint mapPoint : adjacency.get(i))
//
//            for (int w : adjacency[i]) {
//                s.append(w).append(" ");
//            }
//            s.append(newLine);
//        }
//        return s.toString();
//    }
}
