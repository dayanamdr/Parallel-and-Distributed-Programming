import mpi.MPI;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws IOException {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        if (me == 0) {
            // master process
            Matrix matrix = Matrix.fromFile();
            searchMaster(matrix);
        } else {
            // worker process
            searchWorker();
        }
        MPI.Finalize();
    }

    private static void searchMaster(Matrix root) {
        int size = MPI.COMM_WORLD.Size();
        int workers = size - 1;
        //System.out.println("WORKERS = " + workers);
        int minBound = root.getManhattan();
        boolean found = false;
        long time = System.currentTimeMillis();

        // Generate the starting configurations for the workers by adding initial state and its possible moves to the queue
        Queue<Matrix> q = new LinkedList<>();
        q.add(root);
        // until the total number of states in the queue + the number of possible moves from the front state is <= workers
        while (q.size() + q.peek().generateMoves().size() - 1 <= workers) {
            Matrix curr = q.poll();
            for (Matrix neighbour : curr.generateMoves()) {
                q.add(neighbour);
            }
        }

        while (!found) {
            // Send data to all workers
            Queue<Matrix> temp = new LinkedList<>();
            temp.addAll(q);
            for (int i = 0; i < q.size(); i++) {
                // For each worker, send a "root"
                Matrix current = temp.poll();
                MPI.COMM_WORLD.Send(new boolean[]{false}, 0, 1, MPI.BOOLEAN, i + 1, 0);
                MPI.COMM_WORLD.Send(new Object[]{current}, 0, 1, MPI.OBJECT, i + 1, 0);
                MPI.COMM_WORLD.Send(new int[]{minBound}, 0, 1, MPI.INT, i + 1, 0);
            }

            // Pairs containing the estimation and the state
            Object[] pairs = new Object[size + 5];
            // Receive data
            for (int i = 1; i <= q.size(); i++) {
                MPI.COMM_WORLD.Recv(pairs, i - 1, 1, MPI.OBJECT, i, 0);
            }

            // Check if any node found a solution
            int newMinBound = Integer.MAX_VALUE;
            for (int i = 0; i < q.size(); i++) {
                Pair<Integer, Matrix> p = (Pair<Integer, Matrix>) pairs[i];
                //System.out.println(p.toString());
                if (p.getFirst() == -1) { // the solution is found
                    System.out.println("Solution found in " + p.getSecond().getNumOfSteps() + " steps");
                    System.out.println("Solution is: ");
                    System.out.println(p.getSecond());
                    System.out.println("Execution time: " + (System.currentTimeMillis() - time) + "ms");
                    found = true;
                    break;
                } else if (p.getFirst() < newMinBound) {
                    // no found solution, the minimum bound gets updated based on the received results
                    newMinBound = p.getFirst();
                }
            }
            if(!found) {
                System.out.println("Depth " + newMinBound + " reached in " + (System.currentTimeMillis() - time) + "ms");
                minBound = newMinBound;
            }
        }

        for (int i = 1; i < size; i++) {
            // Shut down workers when solution was found
            Matrix current = q.poll();
            MPI.COMM_WORLD.Send(new boolean[]{true}, 0, 1, MPI.BOOLEAN, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{current}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{minBound}, 0, 1, MPI.INT, i, 0);
        }
    }

    private static void searchWorker() {
        while (true) {
            Object[] matrix = new Object[1];
            int[] bound = new int[1];
            boolean[] end = new boolean[1]; // true if the solution was found
            MPI.COMM_WORLD.Recv(end, 0, 1, MPI.BOOLEAN, 0, 0);
            MPI.COMM_WORLD.Recv(matrix, 0, 1, MPI.OBJECT, 0, 0);
            MPI.COMM_WORLD.Recv(bound, 0, 1, MPI.INT, 0, 0);
            if (end[0]) { // Shut down when solution was found
                //System.out.println("Node " + MPI.COMM_WORLD.Rank() + " is ending its execution");
                return;
            }
            int minBound = bound[0];
            Matrix current = (Matrix) matrix[0];
            Pair<Integer, Matrix> result = search(current, current.getNumOfSteps(), minBound);
            MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
        }
    }

    public static Pair<Integer, Matrix> search(Matrix current, int numSteps, int bound) {
        // Total cost of the current state
        int estimation = numSteps + current.getManhattan();

        // Check if estimation exceeds the current bound
        // If so, it won't lead to a solution within the current bound
        if (estimation > bound) {
            return new Pair<>(estimation, current);
        }

        // Additional termination condition for limiting the depth of search
        if (estimation > 80) {
            return new Pair<>(estimation, current);
        }

        // Check if the puzzle is already solved
        if (current.getManhattan() == 0) {
            return new Pair<>(-1, current);
        }

        int min = Integer.MAX_VALUE;
        Matrix solution = null;

        // Explore possible moves from the current state
        for (Matrix next : current.generateMoves()) { // go through the possible moves
            // Call the 'search' method recursively to explore the next state
            Pair<Integer, Matrix> result = search(next, numSteps + 1, bound);
            int t = result.getFirst();

            // Check if solution is found
            if (t == -1) {
                return new Pair<>(-1, result.getSecond());
            }

            // Update the minimum estimation and corresponding solution based on the results of the recursive calls
            if (t < min) {
                min = t;
                solution = result.getSecond();
            }
        }
        // Return the minimum estimation and corresponding solution
        return new Pair<>(min, solution);
    }

}