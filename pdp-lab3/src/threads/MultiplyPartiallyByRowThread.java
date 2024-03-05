package threads;

import matrix.Matrix;
import matrix.MatrixMultiplication;

public class MultiplyPartiallyByRowThread extends Thread {
    private Matrix a;
    private Matrix b;
    private Matrix c;
    private int capacity;
    private int startRowIdx;
    private int startColIdx;

    public MultiplyPartiallyByRowThread(Matrix a, Matrix b, Matrix c, int capacity, int startRowIdx, int startColIdx) {
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
            MatrixMultiplication.multiplyByRow(a, b, c, capacity, startRowIdx, startColIdx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
