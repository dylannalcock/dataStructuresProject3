package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxOutTaskBad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class InParallel implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        ArrayList<HashMap<Integer, Integer>> adjList = Parser.parse(adjMatrix);

        int[] D = new int[adjMatrix.length]; // shortest distances from source to each node
        int[] P = new int[adjMatrix.length]; // predecessors for each node in the shortest path

        // Initialize all nodes to be unreachable from the source
        for (int i = 0; i < adjMatrix.length; i++) {
            D[i] = Integer.MAX_VALUE;
            P[i] = -1;
        }
        // Initialize the source node to have distance 0
        D[source] = 0;

        // Create a ForkJoin pool
        ForkJoinPool pool = new ForkJoinPool();

        // Iterate n - 1 times
        for (int n = 0; n < adjMatrix.length; n++) {
            // Copy values from D to D2 in parallel using the ArrayCopyTask
            int[] D2 = new int[D.length];
            pool.invoke(new ArrayCopyTask(D, D2, 0, D.length));

            // Relax the edges for each vertex in parallel using the RelaxOutTaskBad
            for (int w = 0; w < adjMatrix.length; w++) {

                pool.invoke(new RelaxOutTaskBad(D, P, D2, adjList, w));
            }
        }


        System.out.println(Arrays.toString(P));
        System.out.println(GraphUtil.getCycle(P));
        return GraphUtil.getCycle(P);
    }

}