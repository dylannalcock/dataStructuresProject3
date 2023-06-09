package paralleltasks;

import java.util.concurrent.RecursiveAction;
import java.util.HashMap;
import java.util.ArrayList;

public class RelaxOutTaskBad extends RecursiveAction {

    // Array of distances from the source to each vertex
    private final int[] D;
    // Array of predecessor nodes in the shortest path from the source
    private final int[] P;
    // Array of adjacency lists for each vertex

    private final int[] D2;
    private final ArrayList<HashMap<Integer, Integer>> adjList;
    // The vertex to relax the outgoing edges for
    private final int v;

    public RelaxOutTaskBad(int[] D, int[] P, int[] D2, ArrayList<HashMap<Integer, Integer>> adjList, int v) {
        this.D = D;
        this.P = P;
        this.D2 = D2;
        this.adjList = adjList;
        this.v = v;
    }

    @Override
    protected void compute() {
        // Get the neighbors of the current vertex
        HashMap<Integer, Integer> neighbors = adjList.get(v);

        // Relax the edges for each neighbor of the current vertex
        for (int w : neighbors.keySet()) {

            int edgeCost = neighbors.get(w);
            if (D[v] < Integer.MAX_VALUE && D[w] > D[v] + edgeCost) {
                D[w] = D2[v] + edgeCost;
                P[w] = v;
            }
        }
    }
}
