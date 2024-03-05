package runnables;

import matrix.Matrix;
import matrix.MatrixMultiplication;

public class MultiplyByRowKthRunnable implements Runnable {
    private final Matrix a;
    private final Matrix b;
    private final Matrix c;
    private final int k;
    private final int orderNo;

    public MultiplyByRowKthRunnable(Matrix a, Matrix b, Matrix c, int k, int orderNo) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.k = k;
        this.orderNo = orderNo;
    }
    @Override
    public void run() {
        try {
            MatrixMultiplication.multiplyByRowKthElem(a, b, c, k, orderNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
