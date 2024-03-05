package runnables;

import matrix.Matrix;
import matrix.MatrixMultiplication;

public class MultiplyByRowRunnable implements Runnable {
    private final Matrix a;
    private final Matrix b;
    private final Matrix c;
    private final int capacity;
    private final int startRowIdx;
    private final int startColIdx;

    public MultiplyByRowRunnable(Matrix a, Matrix b, Matrix c, int capacity, int startRowIdx, int startColIdx) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.capacity = capacity;
        this.startRowIdx = startRowIdx;
        this.startColIdx = startColIdx;
    }

    @Override
    public void run() {
        try {
            MatrixMultiplication.multiplyByRow(a, b, c, capacity, startRowIdx, startColIdx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
