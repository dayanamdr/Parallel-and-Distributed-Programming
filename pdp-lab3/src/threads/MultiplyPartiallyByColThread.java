package threads;

import matrix.Matrix;
import matrix.MatrixMultiplication;

public class MultiplyPartiallyByColThread extends Thread {
    private Matrix a;
    private Matrix b;
    private Matrix c;
    private int capacity;
    private int startRowIdx;
    private int startColIdx;

    public MultiplyPartiallyByColThread(Matrix a, Matrix b, Matrix c, int capacity, int startRowIdx, int startColIdx) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        this.capacity = capacity;
        this.startRowIdx = startRowIdx;
        this.startColIdx = startColIdx;
    }

    @Override
    public void run() {
        super.run();
        try {
            MatrixMultiplication.multiplyByColumn(a, b, c, capacity, startRowIdx, startColIdx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
