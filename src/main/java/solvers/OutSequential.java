package solvers;

import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import java.util.*;

import java.util.List;

public class OutSequential implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {

        System.out.println(Arrays.deepToString(adjMatrix));
        ArrayList<HashMap<Integer, Integer>> adjList = Parser.parse(adjMatrix);
        int[] D = new int[adjMatrix.length];
        int[] P = new int[adjMatrix.length];

        // Initialize all nodes to be unreachable from the source
        for (int i = 0; i < adjMatrix.length; i++) {
            D[i] = Integer.MAX_VALUE;
            P[i] = -1;
        }
        // Initialize the source node to have distance 0
        D[source] = 0;

        // Iterate n - 1 times
        for (int n = 0; n < adjMatrix.length; n++) {
            // Copy values from D to D2
            int[] D2 = new int[D.length];

            // Relax the edges
            for (int v = 0; v < adjMatrix.length; v++) {
                for (int i = 0; i < D.length; i++) {
                    D2[i] = D[i];
                }
                HashMap<Integer, Integer> neighbors = adjList.get(v);

                for (int w : neighbors.keySet()) {

                    int edgeCost = neighbors.get(w);
                    if (D[v] < Integer.MAX_VALUE && D[w] > D2[v] + edgeCost) {
                        D[w] = D2[v] + edgeCost;
                        P[w] = v;
                    }
                }
            }
        }
        System.out.println(Arrays.toString(P));
        System.out.println(GraphUtil.getCycle(P));
        return GraphUtil.getCycle(P);
    }
}