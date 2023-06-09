package solvers;

import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxOutTaskLock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OutParallelLock implements BellmanFordSolver {

    private static final Lock lock = new ReentrantLock();

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

            // Relax the edges for each vertex in parallel using the RelaxOutTaskBad
            for (int v = 0; v < adjMatrix.length; v++) {
                // Copy values from D to D2 in parallel using the ArrayCopyTask
                int[] D2 = new int[D.length];
                pool.invoke(new ArrayCopyTask(D, D2, 0, D.length));
                pool.invoke(new RelaxOutTaskLock(D, P, D2, adjList, v, lock));
            }
        }

        return GraphUtil.getCycle(P);
    }

}