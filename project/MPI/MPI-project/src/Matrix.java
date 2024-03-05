import java.io.*;
import java.util.*;

public final class Matrix implements Serializable {

    // Directional movement in x and y coordinates
    private static final int[] dx = new int[]{0, -1, 0, 1};
    private static final int[] dy = new int[]{-1, 0, 1, 0};
    private static final String[] movesStrings = new String[]{"left", "up", "right", "down"};

    // 2D array to represent the matrix tiles
    private final byte[][] tiles;

    // Number of steps taken to reach the current state
    private final int numOfSteps; // g(x)
    // Position of the free (empty) cell
    private final int freePosI;
    private final int freePosJ;
    // Reference to the previous state in the puzzle
    private final Matrix previousState;
    // Minimum steps taken to reach the current state from the initial state
    private final int minSteps;
    // Estimation of the distance to the goal state (heuristic)
    private final int estimation;
    // Manhattan distance for heuristic evaluation
    private final int manhattan; // h(x)
    // The move that led to the current state
    private final String move;

    public Matrix(byte[][] tiles, int freePosI, int freePosJ, int numOfSteps, Matrix previousState, String move) {
        this.tiles = tiles;
        this.freePosI = freePosI;
        this.freePosJ = freePosJ;
        this.numOfSteps = numOfSteps;
        this.previousState = previousState;
        this.move = move;
        this.manhattan = manhattanDistance();
        this.minSteps = numOfSteps + manhattan;
        this.estimation = manhattan + numOfSteps;
    }

    // Static method for creating a Matrix object from a file
    public static Matrix fromFile() throws IOException {
        byte[][] tiles = new byte[4][4];
        int freeI = -1, freeJ = -1;
        Scanner scanner = new Scanner(new BufferedReader(new FileReader("matrix.in")));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                // Read tile values from the file
                tiles[i][j] = Integer.valueOf(scanner.nextInt()).byteValue();
                // Track the position of the free (empty) cell
                if (tiles[i][j] == 0) {
                    freeI = i;
                    freeJ = j;
                }
            }
        }
        // Create and return a new Matrix object
        return new Matrix(tiles, freeI, freeJ, 0, null, "");
    }

    // Calculate the Manhattan distance for the current state
    public int manhattanDistance() {
        int s = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tiles[i][j] != 0) {
                    int targetI = (tiles[i][j] - 1) / 4;
                    int targetJ = (tiles[i][j] - 1) % 4;
                    s += Math.abs(i - targetI) + Math.abs(j - targetJ);
                }
            }
        }
        return s;
    }

    // Generate possible moves from the current state
    public List<Matrix> generateMoves() {
        List<Matrix> moves = new ArrayList<>();
        for (int k = 0; k < 4; k++) {
            if (freePosI + dx[k] >= 0 && freePosI + dx[k] < 4 && freePosJ + dy[k] >= 0 && freePosJ + dy[k] < 4) {
                int movedFreePosI = freePosI + dx[k];
                int movedFreePosJ = freePosJ + dy[k];
                // Check if the move is valid based on the previous state
                if (previousState != null && movedFreePosI == previousState.freePosI
                        && movedFreePosJ == previousState.freePosJ) {
                    continue;
                }
                // Create a new state by swapping the free cell with an adjacent tile
                byte[][] movedTiles = Arrays.stream(tiles)
                        .map(byte[]::clone)
                        .toArray(byte[][]::new);
                movedTiles[freePosI][freePosJ] = movedTiles[movedFreePosI][movedFreePosJ];
                movedTiles[movedFreePosI][movedFreePosJ] = 0;
                // Add the new state to the list of possible moves
                moves.add(new Matrix(
                        movedTiles,
                        movedFreePosI,
                        movedFreePosJ,
                        numOfSteps + 1,
                        this,
                        movesStrings[k]
                ));

            }
        }
        return moves;
    }

    @Override
    public String toString() {
        Matrix current = this;
        List<String> strings = new ArrayList<>();
        while (current != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append(current.move);
            sb.append("\n");
            Arrays.stream(current.tiles).forEach(row -> sb.append(Arrays.toString(row)).append("\n"));
            sb.append("\n");
            strings.add(sb.toString());
            current = current.previousState;
        }
        Collections.reverse(strings);
        return "Moves {" +
                String.join("", strings) +
                "numOfSteps=" + numOfSteps +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;
        boolean flag = true;
        for (int i = 0; i < 4; i++)
            flag = flag && Arrays.equals(tiles[i], matrix.tiles[i]);
        return flag;
    }

    public int getEstimation() {
        return estimation;
    }

    public int getMinSteps() {
        return minSteps;
    }

    public int getNumOfSteps() {
        return numOfSteps;
    }

    public int getManhattan() {
        return manhattan;
    }

}